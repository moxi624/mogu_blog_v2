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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.moxi.mogublog.utils.CheckUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.AdminRole;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.AdminRoleService;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redis.clients.jedis.Jedis;

/**
 * <p>
 * 管理员表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/admin")
@Api(value="管理员RestApi",tags={"AdminRestApi"})
public class AdminRestApi {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AdminRoleService adminRoleService;
	
	@Autowired
	private Jedis jedis;

	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="注册管理员", notes="注册管理员")
	@PostMapping("/register")
	public String register(HttpServletRequest request,
			@ApiParam(name = "assignbody",value ="管理员注册对象",required = true) @RequestBody(required = true) Admin registered) {
			
			String mobile = registered.getMobile();
			String code = registered.getValidCode();
			String userName = registered.getUserName();
			String email = registered.getEmail();
			String passWord = registered.getPassWord();
			String validCode = null;
			
			if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(passWord)) {
				return ResultUtil.result(SysConf.ERROR, "用户名或密码不能为空");
			}
			if(StringUtils.isEmpty(code)) {
				return ResultUtil.result(SysConf.ERROR, "验证码不能为空");
			}
			if(StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
				return ResultUtil.result(SysConf.ERROR, "邮箱和手机号至少一项不能为空");
			}
			
			//手机号为空时为邮箱注册
			if(StringUtils.isEmpty(mobile)) {
				validCode = stringRedisTemplate.opsForValue().get(email);//从redis中获取验证码
			}else {
				validCode = stringRedisTemplate.opsForValue().get(mobile);//从redis中获取验证码
			}
			if(validCode.isEmpty()) {
				return ResultUtil.result(SysConf.ERROR, "验证码已过期");
			}
			if(!code.equals(validCode)) {
				return ResultUtil.result(SysConf.ERROR, "验证码不正确");
			}
			
			QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
			queryWrapper.eq(SQLConf.USERNAEM, userName);
			Admin admin = adminService.getOne(queryWrapper);
			
			QueryWrapper<Admin> wrapper= new QueryWrapper<Admin>();
			if (admin == null) {
				if(StringUtils.isNotEmpty(email)) {
					wrapper.eq(SQLConf.EMAIL, email);
				}else{
					wrapper.eq(SQLConf.MOBILE, mobile);
				}
				
				if(adminService.getOne(wrapper)!= null ) {
					return ResultUtil.result(SysConf.ERROR, "管理员账户已存在");
				}
				registered.setValidCode(validCode);//将验证码保存到数据库
				registered.setAdministrators(0);//设置为非超级管理员
				registered.setStatus(0);//设置为未审核状态
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				registered.setPassWord(encoder.encode(registered.getPassWord()));
				adminService.save(registered);
				//清楚redis中的缓存
				if(StringUtils.isEmpty(mobile)) {
					jedis.del(email);
				}else {
					jedis.del(mobile);
				}
				return ResultUtil.result(SysConf.SUCCESS, "注册成功");
			}
		return ResultUtil.result(SysConf.ERROR, "管理员账户已存在");
	}
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="获取管理员列表", notes="获取管理员列表")
	@GetMapping("/getList")
	public String getList(HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
		Page<Admin> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		IPage<Admin> pageList = adminService.page(page, queryWrapper);
		List<Admin> list = pageList.getRecords();
		log.info(list.toString());
		return ResultUtil.result(SysConf.SUCCESS, list);
	}
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="更新管理员基本信息", notes="更新管理员基本信息")
	@PostMapping("/update")
	public String update(HttpServletRequest request,
			@ApiParam(name = "updateBody",value ="管理员对象",required = true) @RequestBody(required = true) Admin updateBody){
		String uid = updateBody.getUid();
		Admin admin = adminService.getById(uid);
		if (admin == null) {
			return ResultUtil.result(SysConf.ERROR, "管理员不存在");
		}
		admin.setUserName(updateBody.getUserName());
		admin.setGender(updateBody.getGender());
		admin.setAvatar(updateBody.getAvatar());
		admin.setSummary(updateBody.getSummary());
		admin.setBirthday(updateBody.getBirthday());
		admin.setUpdateTime(new Date());
		
		
		UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq(SQLConf.UID,uid);
		adminService.update(admin, updateWrapper);
		return ResultUtil.result(SysConf.SUCCESS, "更新管理员成功");
	}
	
	@PreAuthorize("hasRole('管理员')")
	@ApiOperation(value="更新管理员密码", notes="更新管理员密码")
	@PostMapping("/updatePassWord")
	public String updatePassWord(HttpServletRequest request,
			@ApiParam(name = "userInfo",value ="管理员账户名",required = true) @RequestParam(name = "userInfo" ,required = true )String userInfo,
			@ApiParam(name = "passWord",value ="管理员旧密码",required = true) @RequestParam(name = "passWord" ,required = true )String passWord,
			@ApiParam(name = "newPassWord",value ="管理员新密码",required = true) @RequestParam(name = "newPassWord" ,required = true )String newPassWord){
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
		if(CheckUtils.checkEmail(userInfo)) {
			queryWrapper.eq(SQLConf.EMAIL, userInfo);
		}else if(CheckUtils.checkMobileNumber(userInfo)) {
			queryWrapper.eq(SQLConf.MOBILE, userInfo);
		}else {
			queryWrapper.eq(SQLConf.USERNAEM, userInfo);
		}
		Admin admin = adminService.getOne(queryWrapper);
		if (admin == null) {
			return ResultUtil.result(SysConf.ERROR, "管理员不存在");
		}
		if(StringUtils.isEmpty(passWord)) {
			return ResultUtil.result(SysConf.ERROR, "旧密码不能为空");
		}
		if(StringUtils.isEmpty(newPassWord)) {
			return ResultUtil.result(SysConf.ERROR, "新密码不能为空");
		}
		String uid = admin.getUid();
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
	    boolean isPassword = encoder.matches(passWord, admin.getPassWord());
	    if (isPassword) {
			admin.setPassWord(encoder.encode(newPassWord));
			UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq(SQLConf.UID,uid);
			admin.setUpdateTime(new Date());
			adminService.update(admin, updateWrapper);
			return ResultUtil.result(SysConf.SUCCESS, "密码更新成功");
		}
		return ResultUtil.result(SysConf.ERROR, "旧密码错误");
	}
	
	@PreAuthorize("hasRole('管理员')")
	@ApiOperation(value="更新管理员邮箱或手机号", notes="更新管理员邮箱或手机号")
	@PostMapping("/updateEmail")
	public String updateEmail(HttpServletRequest request,
			@ApiParam(name = "uid",value ="管理员uid",required = true) @RequestParam(name = "uid" ,required = true )String uid,
			@ApiParam(name = "newInfo",value ="管理员新邮箱或新手机号",required = true) @RequestParam(name = "newInfo" ,required = true )String newInfo,
			@ApiParam(name = "validCode",value ="验验码",required = true) @RequestParam(name = "validCode" ,required = true )String validCode){
		
//		Claims claims = (Claims) request.getAttribute(SysConf.CLAIMS);
//		String uid = claims.get("adminUid", String.class);
		Admin admin = adminService.getById(uid);
		if (admin == null) {
			return ResultUtil.result(SysConf.ERROR, "管理员不存在");
		}
		String checkValidCode = "123456";//TODO 需整合消息中间件获取验证码
		if(checkValidCode.equals(validCode)) {
			if(CheckUtils.checkEmail(newInfo)) {
				admin.setEmail(newInfo);
			}else if(CheckUtils.checkMobileNumber(newInfo)) {
				admin.setMobile(newInfo);
			}else {
				return ResultUtil.result(SysConf.ERROR, "输入的邮箱或手机号格式有误");
			}
			admin.setUpdateTime(new Date());
			adminService.updateById(admin);
			return ResultUtil.result(SysConf.SUCCESS, "更新成功");
		}
		return ResultUtil.result(SysConf.ERROR, "验证码错误");
	}
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="删除部分管理员信息", notes="删除部分管理员信息")
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "adminUids",value ="管理员uid集合",required = true) @RequestParam(name = "adminUids" ,required = true )List<String> adminUids){
		QueryWrapper<Admin> queryWrapper  = new QueryWrapper<>();
		if(adminUids.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "管理员uid不能为空");
		}
		queryWrapper.in(SQLConf.UID, adminUids);
		adminService.remove(queryWrapper);
		return ResultUtil.result(SysConf.SUCCESS, "删除管理员成功");
	}
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="分配用户角色信息列表", notes="分配用户角色信息列表")
	@PostMapping("/assign")
	public String assign(HttpServletRequest request,
			@ApiParam(name = "adminUid",value ="管理员uid",required = true) @RequestParam(name = "adminUid" ,required = true )String adminUid){
		
		Map<String, Object> map = new HashMap<>();
	
		Admin admin = adminService.getById(adminUid);
		map.put("admin", admin);
		QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
		List<Role> roles = roleService.list(queryWrapper);
		
		List<Role> assignedRoles = new ArrayList<>();
		List<Role> unassignRoles = new ArrayList<>();
		
		//根据admin获取账户拥有的角色uid集合
		QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
		wrapper.eq(SQLConf.ADMINUID,admin.getUid());
		List<AdminRole> adminRoleList = adminRoleService.list(wrapper);
		List<String> roleUids = new ArrayList<>();
		for (AdminRole adminRole : adminRoleList) {
			String roleUid = adminRole.getRoleUid();
			roleUids.add(roleUid);
		}
		for (Role role : roles) {
			if( roleUids.contains(role.getUid()) ){
				assignedRoles.add(role);
			}else {
				unassignRoles.add(role);
			}
		}
		map.put("assignedRoles", assignedRoles);
		map.put("unassignRoles", unassignRoles);
		return ResultUtil.result(SysConf.SUCCESS,map);
	}
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="管理员角色分配", notes="管理员角色分配")
	@PostMapping("/doAssign")
	public String doAssign(HttpServletRequest request,
			@ApiParam(name = "adminUid",value ="管理员uid",required = true) @RequestParam(name = "adminUid" ,required = true )String adminUid,
			@ApiParam(name = "unAssignRoleUids",value = "需分配的未分配角色") @RequestParam(name = "unAssignRoleUids" ,required = true )String[] unAssignRoleUids){
		
		
		if(adminUid.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "管理员不存在");
		}
		List<AdminRole> adminRoles = new ArrayList<>();
		for (String roleUid : unAssignRoleUids) {
			QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(SQLConf.ADMINUID,adminUid);
			queryWrapper.eq(SQLConf.ROLEUID,roleUid);
			AdminRole getAdminRole = adminRoleService.getOne(queryWrapper);
			if(getAdminRole!=null) {
				continue;
			}
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminUid(adminUid);
			adminRole.setRoleUid(roleUid);
			adminRoles.add(adminRole);
		}
		adminRoleService.saveBatch(adminRoles);
		return ResultUtil.result(SysConf.SUCCESS, "分配管理员角色成功");
	}
	
	@PreAuthorize("hasRole('超级管理员')")
	@ApiOperation(value="取消管理员角色分配", notes="取消管理员角色分配")
	@PostMapping("/doUnassign")
	public String doUnassign(HttpServletRequest request,
			@ApiParam(name = "adminUid",value ="管理员uid",required = true) @RequestParam(name = "adminUid" ,required = true )String adminUid,
			@ApiParam(name = "assignRoleUids",value = "需取消的已分配角色") @RequestParam(name = "assignRoleUids" ,required = true )String[] assignRoleUids){
		
		if(adminUid.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "管理员不存在");
		}
		
		for (String roleUid : assignRoleUids) {
			QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(SQLConf.ADMINUID, adminUid);
			queryWrapper.eq(SQLConf.ROLEUID,roleUid);
			adminRoleService.remove(queryWrapper);
		}
		return ResultUtil.result(SysConf.SUCCESS, "取消管理员角色成功");
	}
	
}

