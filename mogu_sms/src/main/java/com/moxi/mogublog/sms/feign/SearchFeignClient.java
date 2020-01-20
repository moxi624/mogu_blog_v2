package com.moxi.mogublog.sms.feign;

import com.moxi.mogublog.sms.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mogu-elasticsearch", url = "http://localhost:8605/", configuration = FeignConfiguration.class)
public interface SearchFeignClient {


    /**
     * 通过博客uid删除ElasticSearch博客索引
     *
     * @param uid
     * @return
     */
    @PostMapping("/search/deleteElasticSearchByUid")
    public String deleteElasticSearchByUid(@RequestParam(required = true) String uid);

    /**
     * 通过uids删除ElasticSearch博客索引
     *
     * @param uids
     * @return
     */
    @PostMapping("/search/deleteElasticSearchByUids")
    public String deleteElasticSearchByUids(@RequestParam(required = true) String uids);

    /**
     * 初始化ElasticSearch索引
     *
     * @return
     */
    @PostMapping("/search/initElasticSearchIndex")
    public String initElasticSearchIndex();

    /**
     * 通过uid来增加ElasticSearch索引
     *
     * @return
     */
    @PostMapping("/search/addElasticSearchIndexByUid")
    public String addElasticSearchIndexByUid(@RequestParam(required = true) String uid);


    /**
     * 通过博客uid删除Solr博客索引
     *
     * @param uid
     * @return
     */
    @PostMapping("/search/deleteSolrIndexByUid")
    public String deleteSolrIndexByUid(@RequestParam(required = true) String uid);

    /**
     * 通过uids删除Solr博客索引
     *
     * @param uids
     * @return
     */
    @PostMapping("/search/deleteSolrIndexByUids")
    public String deleteSolrIndexByUids(@RequestParam(required = true) String uids);

    /**
     * 初始化Solr索引
     *
     * @return
     */
    @PostMapping("/search/initSolrIndex")
    public String initSolrIndex();

    /**
     * 通过uid来增加Solr索引
     *
     * @return
     */
    @PostMapping("/search/addSolrIndexByUid")
    public String addSolrIndexByUid(@RequestParam(required = true) String uid);

    /**
     * 通过uid来更新Solr索引
     *
     * @return
     */
    @PostMapping("/search/updateSolrIndexByUid")
    public String updateSolrIndexByUid(@RequestParam(required = true) String uid);


}
