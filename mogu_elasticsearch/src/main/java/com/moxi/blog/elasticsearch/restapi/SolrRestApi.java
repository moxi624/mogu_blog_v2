//package com.moxi.blog.elasticsearch.restapi;
//
//import com.moxi.blog.elasticsearch.global.SysConf;
//import com.moxi.mogublog.utils.ResultUtil;
//import com.moxi.mogublog.utils.StringUtils;
//
//import com.moxi.mogublog.xo.service.BlogSearchService;
//import com.moxi.mogublog.xo.service.BlogService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
//@RestController
//@RequestMapping("/search")
//@Api(value = "SolrRestApi", tags = {"SolrRestApi"})
//@Slf4j
//public class SolrRestApi {
//
//    @Autowired
//    private BlogSearchService blogSearchService;
//
//    @Value(value = "${spring.data.solr.core}")
//    private String collection;
//
//    @ApiOperation(value = "通过Solr搜索博客", notes = "通过Solr搜索博客", response = String.class)
//    @GetMapping("/searchBlog")
//    public String searchBlog(HttpServletRequest request,
//                             @RequestParam(required = true) String keywords,
//                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
//                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
//
//        if (StringUtils.isEmpty(keywords)) {
//            return ResultUtil.result(SysConf.ERROR, "关键字不能为空");
//        }
//
//        Map<String, Object> map = blogSearchService.search(collection, keywords, currentPage, pageSize);
//
//
//        return ResultUtil.result(SysConf.SUCCESS, map);
//
//    }
//
//
//}
