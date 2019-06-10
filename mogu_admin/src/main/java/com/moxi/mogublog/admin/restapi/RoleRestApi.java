package com.moxi.mogublog.admin.restapi;

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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 角色表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
//@PreAuthorize("hasRole('Administrators')")
@RestController
@RequestMapping("/role")
@Api(value="角色管理RestApi",tags={"RoleRestApi"})
public class RoleRestApi {

		@Autowired
		private RoleService roleService;
		
		private static Logger log = LogManager.getLogger(RoleRestApi.class);
		
		@ApiOperation(value="获取角色信息列表", notes="获取角色信息列表")
		@GetMapping("/getList")
		public String getList(HttpServletRequest request,
				@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
				@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
				@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
			
			QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
			if(StringUtils.isNotEmpty(keyword) && StringUtils.isNotEmpty(keyword.trim())) {
				queryWrapper.like(SQLConf.ROLENAEM, keyword.trim());	
			}
			
			Page<Role> page = new Page<>();
			page.setCurrent(currentPage);
			page.setSize(pageSize);
			IPage<Role> pageList = roleService.page(page, queryWrapper);
			log.info("返回结果");
			return ResultUtil.result(SysConf.SUCCESS, pageList);
		}
		
		@OperationLogger(value="新增角色信息")
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
		
		@OperationLogger(value="更新角色信息")
		@ApiOperation(value="更新角色信息", notes="更新角色信息")
		@PostMapping("/update")
		public String update(HttpServletRequest request,
				@ApiParam(name = "role",value ="角色信息",required = false) @RequestBody(required = false )Role role) {
			
			String uid = role.getUid();
			Role getRole = roleService.getById(uid);
			if (getRole == null) {
				return ResultUtil.result(SysConf.ERROR, "角色不存在");
			}
			role.updateById();
			return ResultUtil.result(SysConf.SUCCESS, "更新角色信息成功");
			
		}
		
		@OperationLogger(value="删除角色信息")
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
		
}
