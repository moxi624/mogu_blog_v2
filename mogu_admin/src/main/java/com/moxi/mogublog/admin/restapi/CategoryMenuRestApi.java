package com.moxi.mogublog.admin.restapi;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.CategoryMenu;
import com.moxi.mogublog.xo.service.CategoryMenuService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 菜单表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月24日15:45:18
 */
//@PreAuthorize("hasRole('Administrator')")
@RestController
@RequestMapping("/categoryMenu")
public class CategoryMenuRestApi {
	
	@Autowired
	CategoryMenuService categoryMenuService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取菜单列表", notes="获取菜单列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "menuLevel", value = "菜单级别",required = false) @RequestParam(name = "menuLevel", required = false, defaultValue = "0") Integer menuLevel,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<CategoryMenu>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.NAME, keyword);
		}
		
		if(menuLevel != 0) {
			queryWrapper.eq(SQLConf.MENU_LEVEL, menuLevel);
		}
		
		Page<CategoryMenu> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.SORT);		
		IPage<CategoryMenu> pageList = categoryMenuService.page(page, queryWrapper);
		List<CategoryMenu> list = pageList.getRecords();
		
		List<String> ids = new ArrayList<String>();
		list.forEach( item -> {
			if(StringUtils.isNotEmpty(item.getParentUid())) {
				ids.add(item.getParentUid());	
			}			
		});
		
		if(ids.size() > 0) {
			Collection<CategoryMenu> parentList = categoryMenuService.listByIds(ids);
			
			Map<String, CategoryMenu> map = new HashMap<String, CategoryMenu>();
			parentList.forEach(item -> {
				map.put(item.getUid(), item);
			});
			
			list.forEach(item -> {
				if(StringUtils.isNotEmpty(item.getParentUid())) {
					item.setParentCategoryMenu(map.get(item.getParentUid()));				
				}
			});	
			
			resultMap.put(SysConf.OTHER_DATA, parentList);
		}
		
		pageList.setRecords(list);
		log.info("返回结果");

		resultMap.put(SysConf.DATA, pageList);
		return ResultUtil.result(SysConf.SUCCESS, resultMap);
	}
	
	@ApiOperation(value="获取所有菜单列表", notes="获取所有列表", response = String.class)	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public String getAll(HttpServletRequest request) {
		
		QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.MENU_LEVEL, "1");
		List<CategoryMenu> list = categoryMenuService.list(queryWrapper);
		
		//获取所有的ID，去寻找他的子目录
		List<String> ids = new ArrayList<String>();
		list.forEach( item -> {
			if(StringUtils.isNotEmpty(item.getUid())) {
				ids.add(item.getUid());	
			}			
		});
				
		QueryWrapper<CategoryMenu> childWrapper = new QueryWrapper<>();
		childWrapper.in(SQLConf.PARENT_UID, ids);
		Collection<CategoryMenu> childList = categoryMenuService.list(childWrapper);
		for(CategoryMenu parentItem : list) {
			
			List<CategoryMenu> tempList = new ArrayList<>();
			
			for(CategoryMenu item : childList) {
				
				if(item.getParentUid().equals(parentItem.getUid())) {
					tempList.add(item);
				}							
			}
			parentItem.setChildCategoryMenu(tempList);
		}
		
		return ResultUtil.result(SysConf.SUCCESS, list);
	}
	
	
	@ApiOperation(value="增加菜单", notes="增加菜单", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "categoryMenu",value ="菜单",required = false) @RequestBody(required = false ) CategoryMenu categoryMenu) {
		
		if(StringUtils.isEmpty(categoryMenu.getName()) || StringUtils.isEmpty(categoryMenu.getUrl())) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		//如果是一级菜单，将父ID清空
		if(categoryMenu.getMenuLevel() == 1) {
			categoryMenu.setParentUid("");
		}
		categoryMenu.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑菜单", notes="编辑菜单", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "categoryMenu",value ="菜单",required = false) @RequestBody(required = false ) CategoryMenu categoryMenu) {
		
		if(StringUtils.isEmpty(categoryMenu.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		categoryMenu.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除菜单", notes="删除菜单", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		CategoryMenu blogSort = categoryMenuService.getById(uid);
		blogSort.setStatus(EStatus.DISABLED);		
		blogSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
	@ApiOperation(value="置顶菜单", notes="置顶菜单", response = String.class)
	@PostMapping("/stick")
	public String stick(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		CategoryMenu categoryMenu = categoryMenuService.getById(uid);
		
		//查找出最大的那一个
		QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc(SQLConf.SORT);		
		CategoryMenu  maxSort = categoryMenuService.getOne(queryWrapper);
		
		if(StringUtils.isEmpty(maxSort.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "数据错误"); 
		}
		if(maxSort.getUid().equals(categoryMenu.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "该分类已经在顶端");
		}
		
		Integer sortCount = maxSort.getSort() + 1;
		
		categoryMenu.setSort(sortCount);
			
		categoryMenu.updateById();

		return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
	}
	
}

