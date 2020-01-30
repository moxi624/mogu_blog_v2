package com.moxi.mogublog.admin.restapi;

import com.moxi.mogublog.admin.feign.SearchFeignClient;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 索引维护ReastApi
 *
 * @author 陌溪
 * @create 2020年1月15日16:44:27
 */
@RestController
@RequestMapping("/search")
@Api(value = "索引维护RestApi", tags = {"solrIndexRestApi"})
@Slf4j
public class SearchIndexRestApi {

    @Autowired
    private SearchFeignClient searchFeignClient;

    @OperationLogger(value = "初始化ElasticSearch索引")
    @ApiOperation(value = "初始化ElasticSearch索引", notes = "初始化solr索引")
    @PostMapping("/initElasticIndex")
    public String initElasticIndex(HttpServletRequest request) {

        String result = searchFeignClient.initElasticSearchIndex();
        Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(blogMap.get(SysConf.CODE))) {
            return ResultUtil.result(SysConf.SUCCESS, "初始化ElasticSearch索引成功");
        } else {
            return ResultUtil.result(SysConf.ERROR, "初始化ElasticSearch索引失败");
        }
    }

    @OperationLogger(value = "初始化Solr索引")
    @ApiOperation(value = "初始化Solr索引", notes = "初始化solr索引")
    @PostMapping("/initSolrIndex")
    public String initSolrIndex(HttpServletRequest request) {

        String result = searchFeignClient.initSolrIndex();
        Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(blogMap.get(SysConf.CODE))) {
            return ResultUtil.result(SysConf.SUCCESS, "初始化Solr索引成功");
        } else {
            return ResultUtil.result(SysConf.ERROR, "初始化Solr索引失败");
        }
    }
}