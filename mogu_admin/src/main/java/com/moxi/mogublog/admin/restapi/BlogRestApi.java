package com.moxi.mogublog.admin.restapi;


import java.util.ArrayList;
import java.util.Date;
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
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.TagService;
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
	
	@Autowired
	TagService tagService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取博客列表", notes="获取博客列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();
		if(!StringUtils.isEntity(keyword)) {
			queryWrapper.like(SysConf.title, keyword);
		}
		
		//分页插件还没导入，暂时分页没用
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		
		queryWrapper.eq(SysConf.status, EStatus.ENABLE);
		
		queryWrapper.orderByDesc(SysConf.createtime);
		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();
		for(Blog item : list) {
			if(item != null && !StringUtils.isEntity(item.getTaguid())) {
				String uids[] = item.getTaguid().split(",");
				List<Tag> tagList = new ArrayList<Tag>();
				for(String uid : uids) {
					Tag  tag = tagService.getById(uid);
					if(tag != null && tag.getStatus() != EStatus.DISABLED) {
						tagList.add(tag);						
					}
				}
				item.setTagList(tagList);
			}
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="增加博客", notes="增加博客", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "title", value = "博客标题",required = true) @RequestParam(name = "title", required = true) String title,
			@ApiParam(name = "summary", value = "博客简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "content", value = "博客正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "taguid", value = "标签uid",required = false) @RequestParam(name = "taguid", required = false) String taguid,			
			@ApiParam(name = "photo", value = "标题图",required = false) @RequestParam(name = "photo", required = false) String photo	) {
		
		if(StringUtils.isEntity(title) || StringUtils.isEntity(content)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		Blog blog = new Blog();
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setContent(content);
		blog.setTaguid(taguid);
		blog.setClickcount(0);
		blog.setPhoto(photo);
		blog.setStatus(EStatus.ENABLE);
		blog.setCreatetime(new Date());
		blog.setUpdatetime(new Date());
		blogService.save(blog);
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑博客", notes="编辑博客", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "title", value = "博客标题",required = false) @RequestParam(name = "title", required = false) String title,
			@ApiParam(name = "summary", value = "博客简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "content", value = "博客正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "taguid", value = "标签UID",required = false) @RequestParam(name = "taguid", required = false) String taguid,			
			@ApiParam(name = "photo", value = "标题图",required = false) @RequestParam(name = "photo", required = false) String photo ) {
		
		if(StringUtils.isEntity(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Blog blog = blogService.getById(uid);
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setContent(content);
		blog.setTaguid(taguid);
		blog.setPhoto(photo);
		blog.setStatus(EStatus.ENABLE);
		blog.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除博客", notes="删除博客", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEntity(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Blog blog = blogService.getById(uid);
		blog.setStatus(EStatus.DISABLED);		
		blog.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
		
}

