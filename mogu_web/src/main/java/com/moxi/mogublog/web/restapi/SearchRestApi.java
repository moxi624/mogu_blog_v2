package com.moxi.mogublog.web.restapi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctc.wstx.util.StringUtil;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.SolrIndex;
import com.moxi.mogublog.xo.service.BlogSearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/search")
@Api(value="搜索RestApi",tags={"SearchRestApi"})
public class SearchRestApi {
	
    @Autowired
    private BlogSearchService blogSearchService;
    
    private static Logger log = LogManager.getLogger(SearchRestApi.class);

    @SuppressWarnings("unchecked")
	@ApiOperation(value="搜索Blog", notes="搜索Blog")
    @GetMapping("/searchBlog")
    public String searchBlog(HttpServletRequest request,
                             @ApiParam(name = "keywords", value = "关键字",required = true)@RequestParam(required=true)String keywords) {

        Map<String,Object> map = blogSearchService.search(keywords);
//        List<SolrIndex> list = (List<SolrIndex>) JsonUtils.jsonArrayToArrayList(map.get("rows").toString());
//        for(SolrIndex solrIndex : list) {
//        	if(StringUtils.isNotEmpty(solrIndex.getPhotoList())) {
//        		StringUtils.changeStringToString(solrIndex.getPhotoList(), ",");
//        	}
//        }
        
        return ResultUtil.result(SysConf.SUCCESS, map);

    }

}
