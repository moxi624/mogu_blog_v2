package com.moxi.mogublog.admin.restapi;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 用户访问表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2019年5月17日08:38:09
 */
@Api(value="用户访问RestApi",tags={"用户访问RestApi"})
@RestController
@RequestMapping("/webConfig")
public class WebVisitRestApi {
		
	@Autowired
	WebVisitService webVisitService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	BlogSortService blogSortService;
	
	@Autowired
	BlogService blogService;
	
	@Autowired
	LinkService linkService;

	
	@ApiOperation(value="获取用户访问列表", notes="获取用户访问列表")
	@GetMapping("/getList")
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "startTime", value = "时间段",required = false) @RequestParam(name = "startTime", required = false) String startTime,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<WebVisit> queryWrapper = new QueryWrapper<>();
		
		// 得到所有的枚举对象		
		EBehavior [] arr = EBehavior.values();

		// 设置关键字查询
		if(!StringUtils.isEmpty(keyword.trim())) {
			
			String behavior = "";
			for(int a=0; a<arr.length; a++) {
				// 设置行为名称
				if(arr[a].getContent().equals(keyword.trim())) {
					behavior = arr[a].getBehavior();
				}
			}
			
			queryWrapper.like(SQLConf.IP, keyword.trim()).or().eq(SQLConf.BEHAVIOR, behavior);
		}
		
		// 设置起始时间段
		if(!StringUtils.isEmpty(startTime)) {
			String [] time = startTime.split(",");
			if(time.length < 2) {
				return ResultUtil.result(SysConf.ERROR, "传入时间有误");
			}
			queryWrapper.between(SQLConf.CREATE_TIME, DateUtils.str2Date(time[0]), DateUtils.str2Date(time[1]));			
		}
		
		Page<WebVisit> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);		
		IPage<WebVisit> pageList = webVisitService.page(page, queryWrapper);		
		
		List<WebVisit> list = pageList.getRecords();
		List<String> blogUids = new ArrayList<>();
		List<String> tagUids = new ArrayList<>();
		List<String> sortUids = new ArrayList<>();
		List<String> linkUids = new ArrayList<>();
		
		list.forEach(item -> {
			if(item.getBehavior().equals(EBehavior.BLOG_CONTNET.getBehavior()) ) {
				blogUids.add(item.getModuleUid());
			} else if(item.getBehavior().equals(EBehavior.BLOG_SORT.getBehavior())) {
				sortUids.add(item.getModuleUid());
			} else if(item.getBehavior().equals(EBehavior.BLOG_TAG.getBehavior())) {
				tagUids.add(item.getModuleUid());
			} else if(item.getBehavior().equals(EBehavior.FRIENDSHIP_LINK.getBehavior())) {
				linkUids.add(item.getModuleUid());
			}
		});
		Collection<Blog> blogList = new ArrayList<>();
		Collection<Tag> tagList = new ArrayList<>();
		Collection<BlogSort> sortList = new ArrayList<>();
		Collection<Link> linkList = new ArrayList<>();
		
		if(blogUids.size() > 0) {
			blogList = blogService.listByIds(blogUids);	
		}
		
		if(tagUids.size() > 0) {
			tagList = tagService.listByIds(tagUids);	
		}
		
		if(sortUids.size() > 0) {
			sortList = blogSortService.listByIds(sortUids);	
		}
		
		if(linkUids.size() > 0) {
			linkList = linkService.listByIds(linkUids);	
		}

		Map<String, String> contentMap = new HashMap<>();
		blogList.forEach(item -> {
			contentMap.put(item.getUid(), item.getTitle());
		});
		
		tagList.forEach(item -> {
			contentMap.put(item.getUid(), item.getContent());
		});
		
		sortList.forEach(item -> {
			contentMap.put(item.getUid(), item.getContent());
		});
		
		linkList.forEach(item -> {
			contentMap.put(item.getUid(), item.getTitle());
		});
			
		list.forEach(item -> {
			

			for(int a=0; a<arr.length; a++) {
				// 设置行为名称
				if(arr[a].getBehavior().equals(item.getBehavior())) {
					item.setBehaviorContent(arr[a].getContent());
					break;
				}
			}
			
			if(item.getBehavior().equals(EBehavior.BLOG_CONTNET.getBehavior()) ||
					item.getBehavior().equals(EBehavior.BLOG_SORT.getBehavior()) ||
					item.getBehavior().equals(EBehavior.BLOG_TAG.getBehavior()) ||					
					item.getBehavior().equals(EBehavior.FRIENDSHIP_LINK.getBehavior())) {
														
				//从map中获取到对应的名称
				if(StringUtils.isNotEmpty(item.getModuleUid())) {
					item.setContent(contentMap.get(item.getModuleUid()));	
				}
								
			} else  {
				item.setContent(item.getOtherData());
			}
		});
		
		pageList.setRecords(list);
		
		return ResultUtil.result(SysConf.SUCCESS, pageList);		
	}
	

}

