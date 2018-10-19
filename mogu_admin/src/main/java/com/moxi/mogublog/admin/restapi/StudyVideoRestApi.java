package com.moxi.mogublog.admin.restapi;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.moxi.mogublog.xo.entity.StudyVideo;
import com.moxi.mogublog.xo.service.StudyVideoService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 视频表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年10月19日21:35:55
 */
@RestController
@RequestMapping("/studyVideo")
public class StudyVideoRestApi {
	
	@Autowired
	StudyVideoService studyVideoService;
	
	@Autowired
	PictureFeignClient pictureFeignClient;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取学习视频列表", notes="获取学习视频列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<StudyVideo> queryWrapper = new QueryWrapper<StudyVideo>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.NAME, keyword);
		}
		
		Page<StudyVideo> page = new Page<StudyVideo>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);		
		IPage<StudyVideo> pageList = studyVideoService.page(page, queryWrapper);
		List<StudyVideo> list = pageList.getRecords();
		
		for(StudyVideo item : list) {	
			//获取分类资源
			if(item != null && !StringUtils.isEmpty(item.getFileUid())) {				
				String result = this.pictureFeignClient.getPicture(item.getFileUid(), ",");
				List<String> picList = WebUtils.getPicture(result);
				log.info("##### picList: #######" + picList);
				if(picList != null && picList.size() > 0) {
					item.setPhotoList(picList); 
				}
			}
			
		}
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="增加学习视频", notes="增加学习视频", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request, @RequestBody StudyVideo studyVideo) {
		
		if(StringUtils.isEmpty(studyVideo.getName()) || StringUtils.isEmpty(studyVideo.getResourceSortUid())) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		studyVideo.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑学习视频", notes="编辑学习视频", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request, @RequestBody StudyVideo studyVideo) {
		
		if(StringUtils.isEmpty(studyVideo.getName()) || StringUtils.isEmpty(studyVideo.getResourceSortUid())) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		studyVideo.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除学习视频", notes="删除学习视频", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		StudyVideo studyVideo = studyVideoService.getById(uid);
		studyVideo.setStatus(EStatus.DISABLED);		
		studyVideo.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
}

