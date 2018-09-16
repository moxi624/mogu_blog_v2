package com.moxi.mogublog.admin.restapi;


import java.util.Date;

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
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.SysConf;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 标签表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@RestController
@RequestMapping("/tag")
public class TagRestApi {
	@Autowired
	TagService tagService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取标签列表", notes="获取标签列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<Tag>();
		if(!StringUtils.isEntity(keyword)) {
			queryWrapper.like(SysConf.content, keyword);
		}
		
		//分页插件还没导入，暂时分页没用
		Page<Tag> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SysConf.status, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SysConf.createtime);		
		IPage<Tag> pageList = tagService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="增加标签", notes="增加标签", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "content", value = "标签正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "clickcount", value = "标签点击数",required = false) @RequestParam(name = "clickcount", required = false, defaultValue="0") Integer clickcount) {
		
		if(StringUtils.isEntity(content)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		Tag tag = new Tag();
		tag.setContent(content);
		tag.setClickcount(0);
		tag.setStatus(EStatus.ENABLE);
		tag.setCreatetime(new Date());
		tag.setUpdatetime(new Date());
		tag.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑标签", notes="编辑标签", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "content", value = "标签正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "clickcount", value = "标签点击数",required = false) @RequestParam(name = "clickcount", required = false, defaultValue="0") Integer clickcount	) {
		
		if(StringUtils.isEntity(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		
		Tag tag = tagService.getById(uid);
		tag.setContent(content);
		tag.setClickcount(clickcount);
		tag.setStatus(EStatus.ENABLE);
		tag.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除标签", notes="删除标签", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEntity(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Tag tag = tagService.getById(uid);
		tag.setStatus(EStatus.DISABLED);		
		tag.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
}

