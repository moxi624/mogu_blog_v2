package com.moxi.mogublog.admin.restapi;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.entity.Permission;
import com.moxi.mogublog.xo.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 管理员表 RestApi
 * </p>
 *
 * @author limbo
 * @since 2018-10-03
 */
@RestController
@RequestMapping("/permission")
@Api(value="权限管理RestApi",tags={"permissionRestApi"})
public class PermissionRestApi {
	
	@Autowired
	private PermissionService permissionService;
	
	private static Logger log = LogManager.getLogger(PermissionRestApi.class);
	
	@ApiOperation(value="获取权限列表", notes="获取权限列表")
	@GetMapping("/getList")
	public String getList(HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
		Page<Permission> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		IPage<Permission> pageList = permissionService.page(page, queryWrapper);
		List<Permission> list = pageList.getRecords();
		log.info(list.toString());
		return ResultUtil.result(SysConf.SUCCESS, list);
	}
	
	@ApiOperation(value="新增权限信息", notes="新增权限信息")
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "permission",value ="权限信息",required = true) @RequestBody(required = true)Permission permission) {
		
		String permissionName = permission.getPermissionName();
		if(permissionName.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "权限名不能为空");
		}
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
		queryWrapper.eq(SQLConf.PERMISSIONNAME, permissionName);
		Permission getPermission = permissionService.getOne(queryWrapper);
		if(getPermission == null) {
			//把上级权限名转换成上级权限id插入表中
			String parentPermissionName = permission.getParentId();
			if(parentPermissionName.isEmpty()) {
				permission.setParentId("0");//无上级权限的名则设置parentId为0
			}else{
				QueryWrapper<Permission> wrapper = new QueryWrapper<>();
				wrapper.eq(SQLConf.PERMISSIONNAME,parentPermissionName);
				Permission parentPermission = permissionService.getOne(wrapper);
				if(parentPermission == null) {
					return ResultUtil.result(SysConf.ERROR, "上级权限不存在");
				}
				String parentPermissionUid = parentPermission.getUid();
				permission.setParentId(parentPermissionUid);//插入上级权限id
			}
			permissionService.save(permission);
			return ResultUtil.result(SysConf.SUCCESS, "新增权限成功");
		}
		return ResultUtil.result(SysConf.ERROR, "权限已存在");
	}
	
	@ApiOperation(value="更新权限信息", notes="更新权限信息")
	@PostMapping("/update")
	public String update(HttpServletRequest request,
			@ApiParam(name = "permission",value ="权限信息",required = true) @RequestBody(required = true)Permission permission) {
		
		String uid = permission.getUid();
		Permission getPermission = permissionService.getById(uid);
		if(permission.getPermissionName().isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "权限名不能为空");
		}
		getPermission.setPermissionName(permission.getPermissionName());
		getPermission.setUrl(permission.getUrl());
		//把上级权限名转换成上级权限id插入表中
		String parentPermissionName = permission.getParentId();
		if(parentPermissionName.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "上级权限不能为空");
		}
		QueryWrapper<Permission> wrapper = new QueryWrapper<>();
		wrapper.eq(SQLConf.PERMISSIONNAME,parentPermissionName);
		Permission parentPermission = permissionService.getOne(wrapper);
		if(parentPermission == null) {
			return ResultUtil.result(SysConf.ERROR, "上级权限不存在");
		}
		String parentPermissionUid = parentPermission.getUid();
		getPermission.setParentId(parentPermissionUid);//插入上级权限id
		getPermission.setIcon(permission.getIcon());
		getPermission.setUpdateTime(new Date());
		
		UpdateWrapper<Permission> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq(SysConf.UID, uid);
		permissionService.update(getPermission, updateWrapper);
		return ResultUtil.result(SysConf.SUCCESS, "更新权限成功");
		
	}
	
	@ApiOperation(value="删除权限信息", notes="删除权限信息")
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid",value ="权限uid",required = true) @RequestBody(required = true)String uid) {
		if(uid.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "权限uid不能为空");
		}
		permissionService.removeById(uid);
		return ResultUtil.result(SysConf.SUCCESS, "删除权限信息成功");
	}
	
}
