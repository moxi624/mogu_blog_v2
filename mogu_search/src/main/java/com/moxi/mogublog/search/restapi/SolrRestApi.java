//package com.moxi.mogublog.search.restapi;
//
//import com.moxi.mogublog.commons.entity.Blog;
//import com.moxi.mogublog.commons.feign.WebFeignClient;
//import com.moxi.mogublog.search.global.MessageConf;
//import com.moxi.mogublog.search.global.SysConf;
//import com.moxi.mogublog.search.service.SolrSearchService;
//import com.moxi.mogublog.utils.ResultUtil;
//import com.moxi.mogublog.utils.StringUtils;
//import com.moxi.mogublog.utils.WebUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.text.ParseException;
//import java.util.List;
//import java.util.Map;
//
///**
// * Solr搜索相关接口
// *
// * @author 陌溪
// * @date 2020/9/15 15:22
// */
//@RestController
//@RequestMapping("/search")
//@Api(value = "Solr相关接口", tags = {"Solr相关接口"})
//@Slf4j
//public class SolrRestApi {
//
//    @Autowired
//    private SolrSearchService solrSearchService;
//
//    @Autowired
//    private WebFeignClient webFeignClient;
//
//    @Value(value = "${spring.data.solr.core}")
//    private String collection;
//
//    @ApiOperation(value = "通过Solr搜索博客", notes = "通过Solr搜索博客", response = String.class)
//    @GetMapping("/solrSearchBlog")
//    public String solrSearchBlog(HttpServletRequest request,
//                                 @RequestParam(required = false) String keywords,
//                                 @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
//                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
//
//        if (StringUtils.isEmpty(keywords)) {
//            return ResultUtil.result(SysConf.ERROR, MessageConf.KEYWORD_IS_NOT_EMPTY);
//        }
//        log.error("使用Solr搜索：keywords:" + keywords);
//        Map<String, Object> map = solrSearchService.search(collection, keywords, currentPage, pageSize);
//        return ResultUtil.result(SysConf.SUCCESS, map);
//    }
//
//    @ApiOperation(value = "通过博客Uid添加Solr索引", notes = "通过博客Uid添加Solr索引", response = String.class)
//    @PostMapping("/addSolrIndexByUid")
//    public String addSolrIndexByUid(@RequestParam(required = true) String uid) {
//        log.info("通过博客Uid添加Solr索引");
//        String result = webFeignClient.getBlogByUid(uid);
//        Blog blog = WebUtils.getData(result, Blog.class);
//        if (blog == null) {
//            return ResultUtil.result(SysConf.ERROR, MessageConf.INSERT_SUCCESS);
//        }
//        solrSearchService.addIndex(collection, blog);
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_FAIL);
//    }
//
//    @ApiOperation(value = "通过博客Uid更新Solr索引", notes = "通过博客Uid更新Solr索引", response = String.class)
//    @PostMapping("/updateSolrIndexByUid")
//    public String updateSolrIndexByUid(@RequestParam(required = true) String uid) {
//        String result = webFeignClient.getBlogByUid(uid);
//        Blog blog = WebUtils.getData(result, Blog.class);
//        if (blog == null) {
//            return ResultUtil.result(SysConf.ERROR, MessageConf.UPDATE_FAIL);
//        }
//        solrSearchService.updateIndex(collection, blog);
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
//    }
//
//    @ApiOperation(value = "通过博客uid删除Solr博客索引", notes = "通过博客uid删除Solr博客索引", response = String.class)
//    @PostMapping("/deleteSolrIndexByUid")
//    public String deleteSolrIndexByUid(@RequestParam(required = true) String uid) {
//        solrSearchService.deleteIndex(collection, uid);
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
//    }
//
//    @ApiOperation(value = "通过uids删除Solr博客索引", notes = "通过uids删除Solr博客索引", response = String.class)
//    @PostMapping("/deleteSolrIndexByUids")
//    public String deleteSolrIndexByUids(@RequestParam(required = true) String uids) {
//
//        List<String> uidList = StringUtils.changeStringToString(uids, SysConf.FILE_SEGMENTATION);
//
//        solrSearchService.deleteBatchIndex(collection, uidList);
//
//        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
//    }
//
//    @ApiOperation(value = "Solr初始化索引", notes = "Solr初始化索引", response = String.class)
//    @PostMapping("/initSolrIndex")
//    public String initSolrIndex() throws ParseException {
//        log.info("使用Solr初始化全文索引");
//        //清除所有索引
//        solrSearchService.deleteAllIndex(collection);
//        Long page = 1L;
//        Long row = 15L;
//        Integer size = 0;
//        do {
//            // 查询blog信息
//            String result = webFeignClient.getBlogBySearch(page, row);
//            //构建blog
//            List<Blog> blogList = WebUtils.getList(result, Blog.class);
//            size = blogList.size();
//            //存入索引库
//            solrSearchService.initIndex(collection, blogList);
//            //翻页
//            page++;
//
//        } while (size == 15);
//        return ResultUtil.result(SysConf.SUCCESS, null);
//    }
//
//}
