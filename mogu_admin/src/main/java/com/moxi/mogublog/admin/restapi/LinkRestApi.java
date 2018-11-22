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
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 友链表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
@RestController
@Api(value="友链RestApi",tags={"LinkRestApi"})
@RequestMapping("/link")
public class LinkRestApi {
	@Autowired
	LinkService linkService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取友链列表", notes="获取友链列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Link> queryWrapper = new QueryWrapper<Link>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.CONTENT, keyword);
		}
		
		Page<Link> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);		
		IPage<Link> pageList = linkService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="增加友链", notes="增加友链", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "title", value = "友链标题",required = false) @RequestParam(name = "title", required = false) String title,
			@ApiParam(name = "summary", value = "友链简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "url", value = "友链URL",required = false) @RequestParam(name = "url", required = false) String url,
			@ApiParam(name = "clickCount", value = "友链点击数",required = false) @RequestParam(name = "clickCount", required = false, defaultValue="0") Integer clickCount) {
		
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(url)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		Link link = new Link();
		link.setTitle(title);
		link.setSummary(summary);
		link.setUrl(url);
		link.setClickCount(clickCount);
		link.setStatus(EStatus.ENABLE);
		link.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑友链", notes="编辑友链", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "title", value = "友链标题",required = false) @RequestParam(name = "title", required = false) String title,
			@ApiParam(name = "summary", value = "友链简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "url", value = "友链URL",required = false) @RequestParam(name = "url", required = false) String url,
			@ApiParam(name = "clickCount", value = "友链点击数",required = false) @RequestParam(name = "clickCount", required = false, defaultValue="0") Integer clickCount	) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		
		Link link = linkService.getById(uid);
		link.setTitle(title);
		link.setSummary(summary);
		link.setUrl(url);
		link.setClickCount(clickCount);
		link.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除友链", notes="删除友链", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Link tag = linkService.getById(uid);
		tag.setStatus(EStatus.DISABLED);		
		tag.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
}

