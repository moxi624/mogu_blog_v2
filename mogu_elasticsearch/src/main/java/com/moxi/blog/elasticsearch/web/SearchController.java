package com.moxi.blog.elasticsearch.web;

import com.moxi.blog.elasticsearch.global.SysConf;
import com.moxi.blog.elasticsearch.service.SearchService;
import com.moxi.mogublog.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/elasticSearchBlog")
    public String searchBlog(HttpServletRequest request,
                             @RequestParam(required = true) String keywords,
                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        return ResultUtil.result(SysConf.SUCCESS, searchService.search(keywords, currentPage, pageSize));
    }
}
