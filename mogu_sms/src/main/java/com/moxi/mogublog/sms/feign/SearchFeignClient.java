package com.moxi.mogublog.sms.feign;

import com.moxi.mogublog.sms.config.FeignConfiguration;
import com.moxi.mougblog.base.entity.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mogu-elasticsearch", url = "http://localhost:8605/", configuration = FeignConfiguration.class)
public interface SearchFeignClient {

    /**
     * ElasticSearch添加博客
     * @param Eblog
     * @return
     */
    @PostMapping("/search/addBlogbyUid")
    public String addBlogIndex(@RequestBody Blog Eblog);

    /**
     * 通过博客uid删除ElasticSearch博客索引
     * @param uid
     * @return
     */
    @PostMapping("/search/deleteElasticSearchByUid")
    public String deleteElasticSearchByUid(@RequestParam(required = true) String uid);

    /**
     * 通过uids删除ElasticSearch博客索引
     * @param uids
     * @return
     */
    @PostMapping("/search/deleteElasticSearchByUids")
    public String deleteElasticSearchByUids(@RequestParam(required = true) String uids);

    /**
     * 初始化ElasticSearch索引
     * @return
     */
    @PostMapping("/search/initElasticSearchIndex")
    public String initElasticSearchIndex();

    /**
     * 通过uid来增加ElasticSearch索引
     * @return
     */
    @PostMapping("/search/addElasticSearchIndexByUid")
    public String addElasticSearchIndexByUid(@RequestParam(required = true) String uid);


}
