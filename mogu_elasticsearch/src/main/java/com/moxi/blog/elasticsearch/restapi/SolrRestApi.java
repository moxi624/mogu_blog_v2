package com.moxi.blog.elasticsearch.restapi;

import com.moxi.blog.elasticsearch.client.BlogClient;
import com.moxi.blog.elasticsearch.global.SysConf;
import com.moxi.blog.elasticsearch.pojo.ESBlogIndex;
import com.moxi.blog.elasticsearch.service.SolrSearchService;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;

import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.service.BlogSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
@Api(value = "SolrRestApi", tags = {"SolrRestApi"})
@Slf4j
public class SolrRestApi {

    @Autowired
    private SolrSearchService solrSearchService;

    @Autowired
    private BlogClient blogClient;

    @Value(value = "${spring.data.solr.core}")
    private String collection;

    @ApiOperation(value = "通过Solr搜索博客", notes = "通过Solr搜索博客", response = String.class)
    @GetMapping("/searchBlog")
    public String searchBlog(HttpServletRequest request,
                             @RequestParam(required = true) String keywords,
                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if (StringUtils.isEmpty(keywords)) {
            return ResultUtil.result(SysConf.ERROR, "关键字不能为空");
        }

        Map<String, Object> map = solrSearchService.search(collection, keywords, currentPage, pageSize);
        return ResultUtil.result(SysConf.SUCCESS, map);

    }

    @ApiOperation(value = "通过博客Uid添加Solr索引", notes = "通过博客Uid添加Solr索引", response = String.class)
    @PostMapping("/addSolrIndexByUid")
    public String addSolrIndexByUid(@RequestParam(required = true) String uid) {

        String result = blogClient.getBlogByUid(uid);

        Blog blog = WebUtils.getData(result, Blog.class);
        if(blog == null) {
            return ResultUtil.result(SysConf.ERROR, "索引添加失败");
        }
        solrSearchService.addIndex(collection, blog);
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @ApiOperation(value = "通过博客Uid更新Solr索引", notes = "通过博客Uid更新Solr索引", response = String.class)
    @PostMapping("/updateSolrIndexByUid")
    public String updateSolrIndexByUid(@RequestParam(required = true) String uid) {
        solrSearchService.deleteIndex(collection, uid);
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }

    @ApiOperation(value = "通过博客uid删除Solr博客索引", notes = "通过博客uid删除Solr博客索引", response = String.class)
    @PostMapping("/deleteSolrIndexByUid")
    public String deleteSolrIndexByUid(@RequestParam(required = true) String uid) {
        solrSearchService.deleteIndex(collection, uid);
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }


    @ApiOperation(value = "通过uids删除Solr博客索引", notes = "通过uids删除Solr博客索引", response = String.class)
    @PostMapping("/deleteSolrIndexByUids")
    public String deleteSolrIndexByUids(@RequestParam(required = true) String uids) {

        List<String> uidList = StringUtils.changeStringToString(uids, SysConf.FILE_SEGMENTATION);

        solrSearchService.deleteBatchIndex(collection, uidList);

        return ResultUtil.result(SysConf.SUCCESS, "批量删除成功");
    }


    @ApiOperation(value = "Solr初始化索引", notes = "Solr初始化索引", response = String.class)
    @PostMapping("/initSolrIndex")
    public String initSolrIndex() throws ParseException {

        long page = 1L;
        long row = 10L;
        int size = 0;
        do {
            // 查询blog信息
            String result = blogClient.getNewBlog(page, row);
            //构建blog

            List<Blog> blogList1 = WebUtils.getList(result, Blog.class);

            for(Blog bl : blogList1) {
                System.out.println(bl.getTitle());
            }
            //存入索引库
            solrSearchService.initIndex(collection, blogList1);
            //翻页
            page++;

//            Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
//            if ("success".equals(blogMap.get("code"))) {
//                Map<String, Object> blogData = (Map<String, Object>) blogMap.get("data");
//
//
//                List<Map<String, Object>> blogRecords = (List<Map<String, Object>>) blogData.get("records");
//                size = blogRecords.size();
//                List<com.moxi.mogublog.xo.entity.Blog> EBlogList = new ArrayList<>();
//                for (int i = 0; i < size; i++) {
//
//                    if (org.springframework.util.StringUtils.isEmpty(blogRecords.get(i).get("uid"))) {
//                        continue;
//                    }
//
//                    List<Map<String, Object>> tagList = (List<Map<String, Object>>) blogRecords.get(i).get("tagList");
//                    Map<String, Object> MapBlogSort = (Map<String, Object>) blogRecords.get(i).get("blogSort");
//                    List photoList = (List) blogRecords.get(i).get("photoList");
//                    com.moxi.mogublog.xo.entity.Blog EBlog = new com.moxi.mogublog.xo.entity.Blog();
//                    BlogSort blogSort = new BlogSort();
//                    blogSort.setSortName(MapBlogSort.get("sortName").toString());
//                    blogSort.setContent(MapBlogSort.get("content").toString());
//                    blogSort.setUid(MapBlogSort.get("uid").toString());
//                    EBlog.setUid((String) blogRecords.get(i).get("uid"));
//                    EBlog.setTitle((String) blogRecords.get(i).get("title"));
//                    EBlog.setSummary((String) blogRecords.get(i).get("summary"));
//                    EBlog.setBlogSortUid(blogSort.getUid());
//                    EBlog.setBlogSort(blogSort);
//                    EBlog.setIsPublish((String) blogRecords.get(i).get("isPublish"));
//                    EBlog.setAuthor((String) blogRecords.get(i).get("author"));
//                    Date createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(blogRecords.get(i).get("createTime").toString());
//                    EBlog.setCreateTime(createTime);
//                    EBlog.setPhotoList(photoList);
//                    EBlogList.add(EBlog);
//
//                }
//
//                //存入索引库
//                solrSearchService.initIndex(collection, EBlogList);
//                //翻页
//                page++;
//            }
            System.out.println(size);
        } while (size == 15);
        return ResultUtil.result(SysConf.SUCCESS, null);
    }

}
