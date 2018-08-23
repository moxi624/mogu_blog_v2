package com.moxi.mogublog.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mogublog.admin.helper.UserHP;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.UserSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;

/**
 * 用户管理Controller
 * @author xuzhixiang
 * @date 2017年12月29日14:32:24
 *
 */
@Controller
public class UserController extends BaseController{
	
	@Autowired
	UserSO userSO;
	/**
	 * 用户管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/user/list" }, method = RequestMethod.POST)
	@ResponseBody
	public String userList(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = UserHP.getList(model);
		return toJson(model);
	}
	
	/**
	 * 删除用户
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/user/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String userDelete(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			User user = userSO.get(uid);
			if(user != null) {
				user.setStatus(EStatus.DISABLED);
				if (userSO.update(user)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	/**
	 * 冻结用户
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/user/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String userFreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			User user = userSO.get(uid);
			if(user != null) {
				user.setStatus(EStatus.FREEZE);
				if (userSO.update(user)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻用户
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/user/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String userUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			User user = userSO.get(uid);
			if(user != null) {
				user.setStatus(EStatus.ENABLE);
				if (userSO.update(user)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 用户搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/user/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String userSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = UserHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
