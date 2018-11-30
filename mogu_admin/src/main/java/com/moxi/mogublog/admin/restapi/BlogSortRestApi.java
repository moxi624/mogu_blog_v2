package com.moxi.mogublog.admin.restapi;


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
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 博客分类表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月24日15:45:18
 */
//@PreAuthorize("hasRole('Administrator')")
@RestController
@RequestMapping("/blogSort")
public class BlogSortRestApi {
	
	@Autowired
	BlogSortService blogSortService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取博客分类列表", notes="获取博客分类列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<BlogSort>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.CONTENT, keyword);
		}
		
		Page<BlogSort> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.SORT);		
		IPage<BlogSort> pageList = blogSortService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@OperationLogger(value="增加博客分类")
	@ApiOperation(value="增加博客分类", notes="增加博客分类", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "sortName", value = "分类名",required = false) @RequestParam(name = "sortName", required = false) String sortName,
			@ApiParam(name = "content", value = "分类介绍",required = false) @RequestParam(name = "content", required = false) String content) {
		
		if(StringUtils.isEmpty(sortName)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		BlogSort blogSort = new BlogSort();
		blogSort.setContent(content);
		blogSort.setSortName(sortName);
		blogSort.setStatus(EStatus.ENABLE);
		blogSort.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@OperationLogger(value="编辑博客分类")
	@ApiOperation(value="编辑博客分类", notes="编辑博客分类", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "sortName", value = "分类名",required = false) @RequestParam(name = "sortName", required = false) String sortName,
			@ApiParam(name = "content", value = "分类介绍",required = false) @RequestParam(name = "content", required = false) String content	) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		
		BlogSort blogSort = blogSortService.getById(uid);
		if(blogSort == null ) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		blogSort.setContent(content);
		blogSort.setSortName(sortName);
		blogSort.setStatus(EStatus.ENABLE);
		blogSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@OperationLogger(value="删除博客分类")
	@ApiOperation(value="删除博客分类", notes="删除博客分类", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		BlogSort blogSort = blogSortService.getById(uid);
		blogSort.setStatus(EStatus.DISABLED);		
		blogSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
	@ApiOperation(value="置顶分类", notes="置顶分类", response = String.class)
	@PostMapping("/stick")
	public String stick(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		BlogSort blogSort = blogSortService.getById(uid);
		
		//查找出最大的那一个
		QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc(SQLConf.SORT);		
		BlogSort  maxSort = blogSortService.getOne(queryWrapper);
		
		if(StringUtils.isEmpty(maxSort.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "数据错误"); 
		}
		if(maxSort.getUid().equals(blogSort.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "该分类已经在顶端");
		}
		
		Integer sortCount = maxSort.getSort() + 1;
		
		blogSort.setSort(sortCount);
			
		blogSort.updateById();
		
		return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
	}
}

