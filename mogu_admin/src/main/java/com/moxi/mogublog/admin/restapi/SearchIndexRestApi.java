package com.moxi.mogublog.admin.restapi;

import com.moxi.mogublog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.commons.feign.SearchFeignClient;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 索引维护 ReastApi
 *
 * @author 陌溪
 * @date 2020年1月15日16:44:27
 */
@RestController
@RequestMapping("/search")
@Api(value = "索引维护相关接口", tags = {"索引维护相关接口"})
@Slf4j
public class SearchIndexRestApi {

    @Resource
    private SearchFeignClient searchFeignClient;

    @AuthorityVerify
    @OperationLogger(value = "初始化ElasticSearch索引")
    @ApiOperation(value = "初始化ElasticSearch索引", notes = "初始化solr索引")
    @PostMapping("/initElasticIndex")
    public String initElasticIndex() {

        String result = searchFeignClient.initElasticSearchIndex();
        Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(blogMap.get(SysConf.CODE))) {
            return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
        } else {
            return ResultUtil.errorWithMessage(blogMap.get(SysConf.MESSAGE).toString());
        }
    }

    @AuthorityVerify
    @OperationLogger(value = "初始化Solr索引")
    @ApiOperation(value = "初始化Solr索引", notes = "初始化solr索引")
    @PostMapping("/initSolrIndex")
    public String initSolrIndex() {

        String result = searchFeignClient.initSolrIndex();
        Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(blogMap.get(SysConf.CODE))) {
            return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
        } else {
            return ResultUtil.errorWithMessage(blogMap.get(SysConf.MESSAGE).toString());
        }
    }
}