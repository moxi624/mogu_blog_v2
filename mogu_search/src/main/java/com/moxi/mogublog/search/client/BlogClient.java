package com.moxi.mogublog.search.client;

import com.moxi.mogublog.search.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "mogu-web", url = "http://localhost:8603/", configuration = FeignConfiguration.class)
public interface BlogClient {

    @RequestMapping("/content/getBlogByUid")
    public String getBlogByUid(@RequestParam(name = "uid", required = false) String uid);

    @RequestMapping("/content/getSameBlogByTagUid")
    public String getSameBlogByTagUid(@RequestParam(name = "tagUid", required = true) String tagUid,
                                      @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize);

    @RequestMapping("/content/getSameBlogByBlogUid")
    public String getSameBlogByBlogUid(@RequestParam(name = "blogUid", required = true) String blogUid, Long currentPage, @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize);

    @RequestMapping(value = "/index/getNewBlog")
    public String getNewBlog(@RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize);
}
