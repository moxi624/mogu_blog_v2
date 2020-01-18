package com.moxi.blog.elasticsearch.restapi;

import com.moxi.blog.elasticsearch.client.BlogClient;
import com.moxi.blog.elasticsearch.global.SysConf;
import com.moxi.blog.elasticsearch.service.SolrSearchService;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;

import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.Blog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

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
            List<Blog> blogList = WebUtils.getList(result, Blog.class);

            //存入索引库
            solrSearchService.initIndex(collection, blogList);
            //翻页
            page++;

        } while (size == 15);
        return ResultUtil.result(SysConf.SUCCESS, null);
    }

}
