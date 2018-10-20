package com.moxi.mogublog.admin.restapi;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.Picture;
import com.moxi.mogublog.xo.service.PictureService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 图片表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月17日16:21:43
 */
@RestController
@RequestMapping("/picture")
public class PictureRestApi {
	@Autowired
	PictureService pictureService;
	
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	@Value(value="${data.image.url}")
	private String IMG_HOST;
	
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取图片列表", notes="获取图片列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "pictureSortUid", value = "图片分类UID",required = true) @RequestParam(name = "pictureSortUid", required = true) String pictureSortUid,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		if(StringUtils.isEmpty(pictureSortUid)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		QueryWrapper<Picture> queryWrapper = new QueryWrapper<Picture>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.PIC_NAME, keyword);
		}
		
		Page<Picture> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(SQLConf.PICTURE_SORT_UID, pictureSortUid);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);		
		IPage<Picture> pageList = pictureService.page(page, queryWrapper);
		List<Picture> pictureList = pageList.getRecords();
		
		//TODO 以下代码以后需要优化，应该是将全部的id拼接，然后在调用图片接口
		
		for(Picture picture : pictureList) {
			String result = this.pictureFeignClient.getPicture(picture.getFileUid(), ",");
			List<String> picList = WebUtils.getPicture(result);
			log.info("##### picList: #######" + picList);
			if(picList != null && picList.size() > 0) {
				picture.setPictureUrl(picList.get(0)); //获取一张图片
			}
		}

		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="增加图片", notes="增加图片", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "fileUids", value = "图片UIDs",required = false) @RequestParam(name = "fileUids", required = false) String fileUids,			
			@ApiParam(name = "pictureSortUid", value = "图片分类UID",required = false) @RequestParam(name = "pictureSortUid", required = false) String pictureSortUid) {
		
		if(StringUtils.isEmpty(fileUids) || StringUtils.isEmpty(pictureSortUid)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		List<String> list = StringUtils.changeStringToString(fileUids, ",");
		if(list.size() > 0) {
			for(String fileUid : list) {
				Picture picture = new Picture();
				picture.setFileUid(fileUid);			
				picture.setPictureSortUid(pictureSortUid);
				picture.setStatus(EStatus.ENABLE);
				picture.insert();	
			}				
		}
		
		
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑图片", notes="编辑图片", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "fileUid", value = "图片UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid,
			@ApiParam(name = "picName", value = "图片名称",required = false) @RequestParam(name = "picName", required = false) String picName,
			@ApiParam(name = "pictureSortUid", value = "图片分类UID",required = false) @RequestParam(name = "pictureSortUid", required = false) String pictureSortUid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Picture picture = pictureService.getById(uid);
		picture.setFileUid(fileUid);
		picture.setPicName(picName);
		picture.setPictureSortUid(pictureSortUid);		
		picture.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除图片", notes="删除图片", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		List<String> uids = StringUtils.changeStringToString(uid, ",");
		for(String item : uids) {
			Picture picture = pictureService.getById(item);
			picture.setStatus(EStatus.DISABLED);		
			picture.updateById();
		}				
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
}

