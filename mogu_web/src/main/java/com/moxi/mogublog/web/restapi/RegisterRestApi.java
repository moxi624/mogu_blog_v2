package com.moxi.mogublog.web.restapi;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.CheckUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户注册RestApi
 * @author xzx19950624@qq.com
 * @date 2018年11月12日14:51:54
 */
@RestController
@RequestMapping("/register")
@Api(value="用户注册RestApi",tags={"RegisterRestApi"})
public class RegisterRestApi {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value="邮箱注册", notes="邮箱注册")
	@PostMapping("/emailRegister")
	public String emailRegister(HttpServletRequest request,
		@ApiParam(name = "userName",value ="用户名",required = true) @RequestParam(name = "userName" ,required = true )String userName,
		@ApiParam(name = "email",value ="用户邮箱",required = true) @RequestParam(name = "email" ,required = true )String email,
		@ApiParam(name = "passWord",value ="用户密码",required = true) @RequestParam(name = "passWord" ,required = true )String passWord,
		@ApiParam(name = "code",value ="验证码",required = true) @RequestParam(name = "验证码" ,required = true )String code){
		
		String validCode = null;
		if(StringUtils.isEmpty(userName)) {
			return ResultUtil.result(SysConf.ERROR, "用户名不能为空"); 
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(SQLConf.USER_NAME, userName);
		User user = userService.getOne(queryWrapper);
		
		if(user != null) {
			return ResultUtil.result(SysConf.ERROR, "用户名已存在"); 
		}
		
		if(StringUtils.isEmpty(email) && CheckUtils.checkEmail(email)) {
			return ResultUtil.result(SysConf.ERROR, "请输入正确的邮箱格式"); 
		}
		
		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		queryWrapper.eq(SQLConf.email, email);
		if(userService.getOne(wrapper) != null) {
			return ResultUtil.result(SysConf.ERROR, "该邮箱已被注册"); 
		};
		
		if(StringUtils.isEmpty(passWord)) {
			return ResultUtil.result(SysConf.ERROR, "密码不能为空"); 
		}
		
		if(passWord.matches("^.*[a-zA-Z]+.*$") && passWord.matches("^.*[0-9]+.*$") && !passWord.matches("^.*[\\s]+.*$")) {
			return ResultUtil.result(SysConf.ERROR, "密码须包含字母和数字且不能有空白字符"); 
		}
		
		if( (passWord.length() < 6) && (passWord.length() > 18) ) {
			return ResultUtil.result(SysConf.ERROR, "密码长度需为6-18位之间"); 
		}
		
		validCode  = stringRedisTemplate.opsForValue().get(email);//从redis中获取验证码
		
		if(validCode.isEmpty()) {
			return ResultUtil.result(SysConf.ERROR, "验证码已过期");
		}

		if(code.equals(validCode)) {
			return ResultUtil.result(SysConf.ERROR, "验证码不正确");
		}
		
		User registerUser = new User();
		registerUser.setUserName(userName);
		//密码加密
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		registerUser.setPassWord(encoder.encode(passWord));
		//registerUser.setPassWord(passWord);
		registerUser.setEmail(email);
		registerUser.setValidCode(code);
		//registerUser.setStatus(1);
		userService.save(registerUser);
		
		stringRedisTemplate.delete(email);//清楚redis中换出的验证码
		return ResultUtil.result(SysConf.SUCCESS, "注册成功");
	}
}
