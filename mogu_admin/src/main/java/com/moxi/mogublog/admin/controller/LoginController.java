package com.moxi.mogublog.admin.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mogublog.utils.MD5Utils;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.service.AdminSO;

/**
 * 登录注册Controller
 * @author xuzhixiang
 * @date 2017年12月25日09:30:12
 *
 */
@RestController
public class LoginController {
	
	@Autowired
	AdminSO adminSO;
	
	
	/**
	 * 跳转到管理员登录界面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/", "/adminlogin" }, method = RequestMethod.GET)
	public String adminlogin(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		return "login";
	}
	
	/**
	 * 验证登录
	 * @param request
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@RequestMapping(value = {"/index" }, method = RequestMethod.POST)
	public String login(HttpServletRequest request,Map<String, Object> model, String username, String password) throws NoSuchAlgorithmException{
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		if (StrUtils.isNotEmpty(username) && StrUtils.isNotEmpty(password)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SysConf.username, username);
			Admin admin = adminSO.getOne(map);
			if(admin != null) {
				if ( admin.getPassword().equals(MD5Utils.string2MD5(password))) {
					HttpSession session = request.getSession();
					session.setAttribute(SysConf.adminuid, admin.getUid());
					model.put(SysConf.adminname, username);
					return "index";
				}
			}
		}		
		return "login";
	}
	
	/**
	 * 用户注册
	 * @param request
	 * @param model
	 * @param username
	 * @param password
	 * @param gender
	 * @param email
	 * @return
	 */
	@RequestMapping(value = {"/register" }, method = RequestMethod.POST)
	public String register(HttpServletRequest request,Map<String, Object> model, String username, String password, int gender, String email ) {
		Admin user = new Admin();
		user.setUsername(username);
		user.setPassword(password);
		user.setGender(gender);
		user.setEmail(email);
		if (adminSO.insert(user)) {
			return "success";
		}
		return "failear";
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param model
	 * @param oldpwd
	 * @param newpwd
	 * @return
	 * @date 2017年10月23日20:07:07
	 * @author xuzhixiang
	 * @throws NoSuchAlgorithmException 
	 */
	@RequestMapping(value = {"/changepwd" }, method = RequestMethod.POST)
	@ResponseBody
	public String changepwd(HttpServletRequest request,Map<String, Object> model, String oldpwd, String newpwd) throws NoSuchAlgorithmException {
		HttpSession session = request.getSession();
		String adminuid = (String) session.getAttribute(SysConf.adminuid);
		Admin admin = adminSO.get(adminuid);
		if (admin != null) {
			if(admin.getPassword().equals(MD5Utils.string2MD5(oldpwd)) && StrUtils.isNotEmpty(MD5Utils.string2MD5(newpwd))) {
				admin.setPassword(MD5Utils.string2MD5(newpwd));
			}
			if (adminSO.update(admin)) {
				return "success";
			}
		}
		return "failear";
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param model
	 * @param oldpwd
	 * @param newpwd
	 * @return
	 */
	@RequestMapping(value = {"/logout" }, method = RequestMethod.GET)
	public String logout(HttpServletRequest request,Map<String, Object> model) {
		HttpSession session = request.getSession();
		session.setAttribute(SysConf.adminuid, "");
		return "login";
	}
}
