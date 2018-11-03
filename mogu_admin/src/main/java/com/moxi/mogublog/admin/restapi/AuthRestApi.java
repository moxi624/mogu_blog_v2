package com.moxi.mogublog.admin.restapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.config.jwt.Audience;
import com.moxi.mogublog.config.jwt.JwtHelper;
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
 * 需放行接口RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/auth")
@Api(value="需放行接口RestApi",tags={"AuthRestApi"})
public class AuthRestApi {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AdminRoleService adminRoleService;
	
	@Autowired
	private Audience audience;
	
	@Autowired
	private Jedis jedis;
	
	@Value(value="${tokenHead}")
	private String tokenHead;
	
	@Value(value="${base64Security}")
	private String base64Security;
	
	@Value(value="${expiresSecond}")
	private String expiresSecond;
	
	
	@ApiOperation(value="用户登录", notes="用户登录")
	@PostMapping("/login")
	public String login(HttpServletRequest request, 
			@ApiParam(name = "usernameOrEmailOrMobile", value = "用户名或邮箱或手机号", required = true) @RequestParam(name = "usernameOrEmailOrMobile", required = true) String usernameOrEmailOrMobile,
			@ApiParam(name = "password", value = "密码", required = true) @RequestParam(name = "password", required = true) String password) {
		
		if(StringUtils.isEmpty(usernameOrEmailOrMobile) || StringUtils.isEmpty(password)) {
			return ResultUtil.result(SysConf.ERROR, "账号或密码不能为空");
		}		
		  Boolean isEmail = CheckUtils.checkEmail(usernameOrEmailOrMobile);
		  Boolean isMobile = CheckUtils.checkMobileNumber(usernameOrEmailOrMobile);
	      QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
	      if (isEmail) {
	    	  queryWrapper.eq(SQLConf.EMAIL,usernameOrEmailOrMobile);
	      } else if(isMobile){
	    	  queryWrapper.eq(SQLConf.MOBILE,usernameOrEmailOrMobile);
	      }else {
	    	  queryWrapper.eq(SQLConf.USERNAEM,usernameOrEmailOrMobile);
	      }
	      Admin admin = adminService.getOne(queryWrapper);
	      if (admin == null) {
	          return ResultUtil.result(SysConf.ERROR, "管理员账号不存在");
	      }
	      //验证密码
	      PasswordEncoder encoder = new BCryptPasswordEncoder();
	      boolean isPassword = encoder.matches(password, admin.getPassWord());
	      if (!isPassword) {
	          //密码错误，返回提示
	          return ResultUtil.result(SysConf.ERROR, "密码错误");
	      }
	      //根据admin获取账户拥有的角色uid集合
	      QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
	      wrapper.eq(SQLConf.ADMINUID,admin.getUid());
	      List<AdminRole> adminRoleList = adminRoleService.list(wrapper);
	      List<String> roleUids = new ArrayList<>();
	      for (AdminRole adminRole : adminRoleList) {
			String roleUid = adminRole.getRoleUid();
			roleUids.add(roleUid);
	      }
	      
	      List<Role> roles = (List<Role>) roleService.listByIds(roleUids);
	      String roleNames = null;
	      for (Role role : roles) {
			roleNames+=(role.getRoleName()+",");
		  }
	      String roleName = roleNames.substring(0, roleNames.length()-2);
	      String jwtToken = JwtHelper.createJWT(admin.getUserName(),
	    		 							   admin.getUid(),
	    		 							   roleName.toString(),
	    		 							   audience.getClientId(),
	    		 							   audience.getName(),
	    		 							   audience.getExpiresSecond()*1000,
	    		 							   audience.getBase64Secret());
	      String token = tokenHead + jwtToken;
		return ResultUtil.result(SysConf.SUCCESS, token);
	}
	
	@ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
	@PostMapping(value = "/logout")
	public String logout(@ApiParam(name = "token", value = "token令牌",required = false) @RequestParam(name = "token", required = false) String token) {	
		String destroyToken = null;
		return ResultUtil.result(SysConf.SUCCESS, destroyToken);
	}
	
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
	
	@ApiOperation(value="更新token", notes="更新token")
	@PostMapping("/refreshToken")
    public String refreshToken(String oldToken) {
        
		final String token = oldToken.substring(tokenHead.length());
        if (JwtHelper.canTokenBeRefreshed(token,base64Security)){
            return JwtHelper.refreshToken(token, base64Security,Long.parseLong(expiresSecond));
        }
        return null;
    }
}
