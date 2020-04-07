package com.moxi.mogublog.commons.feign;

import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mogu-search", configuration = FeignConfiguration.class)
public interface SearchFeignClient {

    /**
     * ElasticSearch添加博客
     *
     * @param Eblog
     * @return
     */
    @PostMapping("/search/addBlogbyUid")
    public String addBlogIndex(@RequestBody Blog Eblog);

    /**
     * 通过ElasticSearch删除博客
     *
     * @param uid
     * @return
     */
    @PostMapping("/search/delBlogbyUid")
    public String delBlog(@RequestParam(required = true) String uid);

    /**
     * 初始化ElasticSearch索引
     *
     * @return
     */
    @PostMapping("/search/initElasticSearchIndex")
    public String initElasticSearchIndex();

    /**
     * 初始化Solr索引
     *
     * @return
     */
    @PostMapping("/search/initSolrIndex")
    public String initSolrIndex();


}
