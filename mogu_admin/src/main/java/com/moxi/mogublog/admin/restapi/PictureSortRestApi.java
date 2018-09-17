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
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.PictureSort;
import com.moxi.mogublog.xo.service.PictureSortService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 图片分类表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 22018年9月17日16:37:13
 */
@RestController
@RequestMapping("/pictureSort")
public class PictureSortRestApi {
	@Autowired
	PictureSortService pictureSortService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取图片分类列表", notes="获取图片分类列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<PictureSort> queryWrapper = new QueryWrapper<PictureSort>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.NAME, keyword);
		}
		
		//分页插件还没导入，暂时分页没用
		Page<PictureSort> page = new Page<PictureSort>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);		
		IPage<PictureSort> pageList = pictureSortService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="增加图片分类", notes="增加图片分类", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "name", value = "图片分类名",required = false) @RequestParam(name = "name", required = false) String name,
			@ApiParam(name = "parentUid", value = "图片分类父UID",required = false) @RequestParam(name = "parentUid", required = false) String parentUid,
			@ApiParam(name = "fileUid", value = "分类图片UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {
		
		if(StringUtils.isEmpty(name)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		PictureSort pictureSort = new PictureSort();
		pictureSort.setName(name);
		pictureSort.setParentUid(parentUid);
		pictureSort.setFileUid(fileUid);
		pictureSort.setStatus(EStatus.ENABLE);
		pictureSort.setCreateTime(new Date());
		pictureSort.setUpdateTime(new Date());
		pictureSort.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑图片分类", notes="编辑图片分类", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "name", value = "图片分类名",required = false) @RequestParam(name = "name", required = false) String name,
			@ApiParam(name = "parentUid", value = "图片分类父UID",required = false) @RequestParam(name = "parentUid", required = false) String parentUid,
			@ApiParam(name = "fileUid", value = "分类图片UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		
		PictureSort pictureSort = pictureSortService.getById(uid);
		pictureSort.setName(name);
		pictureSort.setParentUid(parentUid);
		pictureSort.setFileUid(fileUid);
		pictureSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除图片分类", notes="删除图片分类", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		PictureSort pictureSort = pictureSortService.getById(uid);
		pictureSort.setStatus(EStatus.DISABLED);		
		pictureSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
}

