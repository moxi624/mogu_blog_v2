package com.moxi.mogublog.admin.restapi;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.SysConf;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 博客表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@RestController
@RequestMapping("/blog")
@Api(value="博客RestApi", tags={"BlogRestApi"})
public class BlogRestApi {
	
	@Autowired
	BlogService blogService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取博客列表", notes="获取博客列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();
		if(!keyword.isEmpty()) {
			queryWrapper.like(SysConf.title, keyword);
		}
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, list);
	}
	
	@ApiOperation(value="增加博客", notes="增加博客", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "title", value = "博客标题",required = true) @RequestParam(name = "title", required = true) String title,
			@ApiParam(name = "summary", value = "博客简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "content", value = "博客正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "taguid", value = "标签UID",required = false) @RequestParam(name = "taguid", required = false) String taguid,			
			@ApiParam(name = "photo", value = "标题图",required = false) @RequestParam(name = "photo", required = false) String photo		) {
		
		Blog blog = new Blog();
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setContent(content);
		blog.setTaguid(taguid);
		blog.setClickcount(0);
		blog.setStatus(EStatus.ENABLE);
		blog.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	
		
}

