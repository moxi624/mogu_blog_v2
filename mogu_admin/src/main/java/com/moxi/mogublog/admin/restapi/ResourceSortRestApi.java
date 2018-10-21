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
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.ResourceSort;
import com.moxi.mogublog.xo.service.ResourceSortService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 资源分类表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年10月19日21:36:02
 */
@RestController
@RequestMapping("/resourceSort")
public class ResourceSortRestApi {
	
	@Autowired
	ResourceSortService resourceSortService;
	
	@Autowired
	PictureFeignClient pictureFeignClient;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取资源分类列表", notes="获取资源分类列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<ResourceSort> queryWrapper = new QueryWrapper<ResourceSort>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.SORT_NAME, keyword);
		}
		
		Page<ResourceSort> page = new Page<ResourceSort>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);		
		IPage<ResourceSort> pageList = resourceSortService.page(page, queryWrapper);
		List<ResourceSort> list = pageList.getRecords();
		
		for(ResourceSort item : list) {	
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
	
	@ApiOperation(value="增加资源分类", notes="增加资源分类", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "sortName", value = "资源分类名",required = false) @RequestParam(name = "sortName", required = false) String sortName,
			@ApiParam(name = "content", value = "分类介绍",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "fileUid", value = "分类资源UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {
		
		if(StringUtils.isEmpty(sortName)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		ResourceSort resourceSort = new ResourceSort();
		resourceSort.setSortName(sortName);
		resourceSort.setContent(content);
		resourceSort.setFileUid(fileUid);
		resourceSort.setStatus(EStatus.ENABLE);
		resourceSort.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑资源分类", notes="编辑资源分类", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "sortName", value = "资源分类名",required = false) @RequestParam(name = "sortName", required = false) String sortName,
			@ApiParam(name = "content", value = "分类介绍",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "fileUid", value = "分类资源UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		
		ResourceSort resourceSort = resourceSortService.getById(uid);
		resourceSort.setSortName(sortName);
		resourceSort.setContent(content);
		resourceSort.setFileUid(fileUid);
		resourceSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除资源分类", notes="删除资源分类", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		ResourceSort resourceSort = resourceSortService.getById(uid);
		resourceSort.setStatus(EStatus.DISABLED);		
		resourceSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
}

