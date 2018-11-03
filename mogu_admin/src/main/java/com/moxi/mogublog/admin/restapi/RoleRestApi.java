package com.moxi.mogublog.admin.restapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.entity.RolePermission;
import com.moxi.mogublog.xo.service.PermissionService;
import com.moxi.mogublog.xo.service.RolePermissionService;
import com.moxi.mogublog.xo.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 管理员表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/role")
@Api(value="角色管理RestApi",tags={"RoleRestApi"})
public class RoleRestApi {

		@Autowired
		private RoleService roleService;
		
		@Autowired
		private PermissionService permissionService;
		
		@Autowired
		private RolePermissionService rolePermissionService;
		
		
		private static Logger log = LogManager.getLogger(RoleRestApi.class);
		
		@ApiOperation(value="获取角色信息列表", notes="获取角色信息列表")
		@GetMapping("/getList")
		public String getList(HttpServletRequest request,
				@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
				@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
			
			QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
			Page<Role> page = new Page<>();
			page.setCurrent(currentPage);
			page.setSize(pageSize);
			IPage<Role> pageList = roleService.page(page, queryWrapper);
			List<Role> list = pageList.getRecords();
			log.info(list.toString());
			return ResultUtil.result(SysConf.SUCCESS, list);
		}
		
		@ApiOperation(value="新增角色信息", notes="新增角色信息")
		@PostMapping("/add")
		public String add(HttpServletRequest request,
				@ApiParam(name = "role",value ="角色信息集合",required = true) @RequestBody(required = true)Role role) {
			String roleName = role.getRoleName();
			QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
			queryWrapper.eq(SQLConf.ROLENAEM, roleName);
			Role getRole = roleService.getOne(queryWrapper);
			if(getRole == null) {
				roleService.save(role);
				return ResultUtil.result(SysConf.SUCCESS, "新增角色成功");
			}
			return ResultUtil.result(SysConf.ERROR, "角色已存在");
		}
		
		@ApiOperation(value="更新角色信息", notes="更新角色信息")
		@PostMapping("/update")
		public String update(HttpServletRequest request,
				@ApiParam(name = "role",value ="角色信息",required = true) @RequestBody(required = true)Role role) {
			
			String uid = role.getUid();
			Role getRole = roleService.getById(uid);
			if (getRole == null) {
				return ResultUtil.result(SysConf.ERROR, "角色不存在");
			}
			String roleName = role.getRoleName();
			QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(SQLConf.ROLENAEM,roleName);
			Role roleInfo = roleService.getOne(queryWrapper);
			if(roleInfo!= null) {
				return ResultUtil.result(SysConf.ERROR, "角色已存在");
			}
			getRole.setRoleName(roleName);
			getRole.setUpdateTime(new Date());
			UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq(SysConf.UID, uid);
			roleService.update(getRole, updateWrapper);
			return ResultUtil.result(SysConf.SUCCESS, "更新角色信息成功");
			
		}
		
		@ApiOperation(value="删除角色信息", notes="删除角色信息")
		@PostMapping("/delete")
		public String delete(HttpServletRequest request,
				@ApiParam(name = "roleUids",value ="角色uid集合",required = true) @RequestParam(name = "roleUids" ,required = true)List<String> roleUids) {
			
			QueryWrapper<Role> queryWrapper  = new QueryWrapper<>();
			if(roleUids.isEmpty()) {
				return ResultUtil.result(SysConf.ERROR, "角色uid不能为空");
			}
			queryWrapper.in(SQLConf.UID, roleUids);
			roleService.remove(queryWrapper);
			return ResultUtil.result(SysConf.SUCCESS, "删除角色信息成功");
		}
		
		@ApiOperation(value="分配角色权限信息列表", notes="分配角色权限信息列表")
		@PostMapping("/assign")
		public String assign(HttpServletRequest request,
				@ApiParam(name = "roleUid",value ="角色uid",required = true) @RequestParam(name = "roleUid" ,required = true )String roleUid){
			
			Map<String, Object> map = new HashMap<>();
		
			Role role = roleService.getById(roleUid);
			map.put("role", role);
			List<Permission> permissions =  pemissionSign(roleUid);
			map.put("permission", permissions);
//			QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
//			List<Permission> permissions = permissionService.list(queryWrapper);
//			
//			List<Permission> assignedPermissions = new ArrayList<>();
//			List<Permission> unAssignPermissions = new ArrayList<>();
//			
//			List<String> permissionUids = rolePermissionService.queryPermissionUidsByRoleUid(roleUid);
//			for (Permission permission : permissions) {
//				if( permissionUids.contains(permission.getUid()) ){
//					assignedPermissions.add(permission);
//				}else {
//					unAssignPermissions.add(permission);
//				}
//			}
//			map.put("assignedRoles", assignedPermissions);
//			map.put("unassignRoles", unAssignPermissions);
			return ResultUtil.result(SysConf.SUCCESS,map);
		}
		
		@ApiOperation(value="角色权限分配", notes="角色权限分配")
		@PostMapping("/doAssign")
		public String doAssign(HttpServletRequest request,
				@ApiParam(name = "roleUid",value ="角色uid",required = true) @RequestParam(name = "roleUid" ,required = true )String roleUid,
				@ApiParam(name = "unAssignPermissionUids",value = "需分配的未分配的权限") @RequestParam(name = "unAssignPermissionUids" ,required = true)String[] unAssignPermissionUids){
			
			if(roleUid.isEmpty()) {
				return ResultUtil.result(SysConf.ERROR, "角色不存在");
			}
			List<RolePermission> rolePermissions = new ArrayList<>();
			for (String permissionUid : unAssignPermissionUids) {
				QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq(SQLConf.ROLEUID,roleUid);
				queryWrapper.eq(SQLConf.PERMISSIONUID,permissionUid);
				RolePermission getRolePermission = rolePermissionService.getOne(queryWrapper);
				if(getRolePermission!=null) {
					continue;
				}
				RolePermission rolePermission = new RolePermission();
				rolePermission.setRoleUid(roleUid);
				rolePermission.setPermissionUid(permissionUid);
				rolePermissions.add(rolePermission);
			}
			rolePermissionService.saveBatch(rolePermissions);
			return ResultUtil.result(SysConf.SUCCESS, "分配角色权限成功");
		}
		
		
		@ApiOperation(value="取消角色权限分配", notes="取消角色权限分配")
		@PostMapping("/doUnassign")
		public String doUnassign(HttpServletRequest request,
				@ApiParam(name = "roleUid",value ="角色uid",required = true) @RequestParam(name = "roleUid" ,required = true )String roleUid,
				@ApiParam(name = "assignedPermissionUids",value = "需取消的已分配权限") @RequestParam(name = "assignedPermissionUids" ,required = true)String[] assignedPermissionUids){
			
			if(roleUid.isEmpty()) {
				return ResultUtil.result(SysConf.ERROR, "角色不存在");
			}
			
			for (String permissionUid : assignedPermissionUids) {
				QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq(SQLConf.ROLEUID,roleUid);
				queryWrapper.eq(SQLConf.PERMISSIONUID,permissionUid);
				rolePermissionService.remove(queryWrapper);
			}
			return ResultUtil.result(SysConf.SUCCESS, "取消角色权限成功");
		}
		
		private List<Permission> pemissionSign(String roleUid) {
			List<Permission> permissions = new ArrayList<Permission>();
			QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
			List<Permission> allPermissions = permissionService.list(queryWrapper);
			String zeroUid = "0";//权限无上级id填0
			// 获取当前角色已经分配的许可信息
			
			List<String> permissionUids = rolePermissionService.queryPermissionUidsByRoleUid(roleUid);
			
			Map<String, Permission> permissionMap = new HashMap<String, Permission>();
			for (Permission permission : allPermissions ) {
				if ( permissionUids.contains(permission.getUid()) ) {
					permission.setChecked(true);//设置为选中即该角色已拥有的权限
				} else {
					permission.setChecked(false);//设置为选中即该角色未拥有的权限
				}
				permissionMap.put(permission.getUid(),permission);
			}
			for (Permission childpermission : allPermissions) {
				if (childpermission.getParentId().equals(zeroUid)) {
					permissions.add(childpermission);
				} else {
					Permission parent = permissionMap.get(childpermission.getParentId());
					parent.getChildren().add(childpermission);//子权限集合
				}
			}
			
			return permissions;
		}
		
//		private List<String> queryChildPermissionUids(String parentId){
//			QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
//			queryWrapper.eq(SQLConf.PARENTID,parentId);
//			List<Permission> list = new ArrayList<>();
//			list = permissionService.list(queryWrapper);
//			List<String>  ChildPermissionUids = new ArrayList<>();
//			for (Permission permission : list) {
//				String uid = permission.getUid();
//				ChildPermissionUids.add(uid);
//			}
//			return ChildPermissionUids;
//		}
}
