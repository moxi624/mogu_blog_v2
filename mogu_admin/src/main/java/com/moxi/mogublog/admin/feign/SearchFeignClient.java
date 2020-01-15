package com.moxi.mogublog.admin.feign;

import com.moxi.mogublog.admin.config.FeignConfiguration;
import com.moxi.mogublog.xo.entity.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "mogu-elasticsearch", url = "http://localhost:8605/", configuration = FeignConfiguration.class)
public interface SearchFeignClient {

    @PostMapping("/search/addBlogbyUid")
    public String AddBlog(@RequestParam(required = true)Blog Eblog);

    @PostMapping("/search/delBlogbyUid")
    public String DelBlog(@RequestParam(required = true) Long uid);

}
