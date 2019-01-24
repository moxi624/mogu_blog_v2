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
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 标签表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
//@PreAuthorize("hasRole('Administrator')")
@Api(value="标签RestApi",tags={"TagRestApi"})
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
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.CONTENT, keyword);
		}

		Page<Tag> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.SORT);		
		IPage<Tag> pageList = tagService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@OperationLogger(value="增加标签")
	@ApiOperation(value="增加标签", notes="增加标签", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "content", value = "标签正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "clickCount", value = "标签点击数",required = false) @RequestParam(name = "clickCount", required = false, defaultValue="0") Integer clickCount) {
		
		if(StringUtils.isEmpty(content)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.CONTENT, content);
		Tag tempTag = tagService.getOne(queryWrapper);
		if(tempTag != null) {
			return ResultUtil.result(SysConf.ERROR, "该标签已经存在");
		}
		Tag tag = new Tag();
		tag.setContent(content);
		tag.setClickCount(clickCount);
		tag.setStatus(EStatus.ENABLE);
		tag.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@OperationLogger(value="编辑标签")
	@ApiOperation(value="编辑标签", notes="编辑标签", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "content", value = "标签正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "clickCount", value = "标签点击数",required = false) @RequestParam(name = "clickCount", required = false, defaultValue="0") Integer clickCount	) {
		
		if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(content)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		
		Tag tag = tagService.getById(uid);
		
		if(!tag.getContent().equals(content)) {
			QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(SQLConf.CONTENT, content);
			Tag tempTag = tagService.getOne(queryWrapper);
			if(tempTag != null) {
				return ResultUtil.result(SysConf.ERROR, "该标签已经存在");
			}
		}
		
		tag.setContent(content);
		tag.setClickCount(clickCount);
		tag.setStatus(EStatus.ENABLE);
		tag.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@OperationLogger(value="删除标签")
	@ApiOperation(value="删除标签", notes="删除标签", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Tag tag = tagService.getById(uid);
		tag.setStatus(EStatus.DISABLED);		
		tag.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
	@OperationLogger(value="置顶标签")
	@ApiOperation(value="置顶标签", notes="置顶标签", response = String.class)
	@PostMapping("/stick")
	public String stick(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Tag tag = tagService.getById(uid);
		
		//查找出最大的那一个
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc(SQLConf.SORT);		
		Tag  maxTag = tagService.getOne(queryWrapper);
		
		if(StringUtils.isEmpty(maxTag.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "数据错误"); 
		}
		if(maxTag.getUid().equals(tag.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "该标签已经在顶端");
		}
		
		Integer sortCount = maxTag.getSort() + 1;
		
		tag.setSort(sortCount);
			
		tag.updateById();
		
		return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
	}
}

