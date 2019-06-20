package com.moxi.mogublog.admin.restapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.CommentService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 首页RestApi
 * @author xzx19950624@qq.com
 * @date 2018年10月22日下午3:27:24
 */
//@PreAuthorize("hasRole('Administrator')")
@RestController
@RequestMapping("/index")
@Api(value="首页RestApi", tags={"IndexRestApi"})
public class IndexRestApi {
	
	@Autowired
	BlogService blogService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	WebVisitService webVisitService;

	@ApiOperation(value="首页初始化数据", notes="获取博客数量", response = String.class)	
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		Integer blogCount = blogService.getBlogCount(EStatus.ENABLE);
		Integer commentCount = commentService.getCommentCount(EStatus.ENABLE);					
		Integer visitCount = webVisitService.getWebVisitCount();
		
		List<Map<String, Object>> blogSortMap = blogService.getBlogCountByTag();
		
		Map<String,Object> visitByWeek =  webVisitService.getVisitByWeek();
		
		map.put(SysConf.BLOG_COUNT, blogCount);
		map.put(SysConf.COMMENT_COUNT, commentCount);
		map.put(SysConf.VISIT_COUNT, visitCount);
		map.put(SysConf.BLOG_COUNT_BY_TAG, blogSortMap);
		map.put(SysConf.VISIT_BY_WEEK, visitByWeek);
		
		return ResultUtil.result(SysConf.SUCCESS, map);
	}
	
	
}
