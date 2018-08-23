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
import com.moxi.mogublog.admin.helper.VisitorHP;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Visitor;
import com.moxi.mogublog.xo.service.VisitorSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;

/**
 * 游客管理Controller
 * @author xuzhixiang
 * @date 2017年12月29日14:32:24
 *
 */
@Controller
public class VisitorController extends BaseController{
	
	@Autowired
	VisitorSO visitorSO;
	
	/**
	 * 游客管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/visitor/list" }, method = RequestMethod.POST)
	@ResponseBody
	public String visitorList(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = VisitorHP.getList(model);
		return toJson(model);
	}
	
	/**
	 * 删除游客
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/visitor/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String visitorDelete(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Visitor visitor = visitorSO.get(uid);
			if(visitor != null) {
				visitor.setStatus(EStatus.DISABLED);
				if (visitorSO.update(visitor)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	/**
	 * 冻结游客
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/visitor/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String visitorFreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Visitor visitor = visitorSO.get(uid);
			if(visitor != null) {
				visitor.setStatus(EStatus.FREEZE);
				if (visitorSO.update(visitor)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻游客
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/visitor/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String visitorUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Visitor visitor = visitorSO.get(uid);
			if(visitor != null) {
				visitor.setStatus(EStatus.ENABLE);
				if (visitorSO.update(visitor)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 游客搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/visitor/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String visitorSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = VisitorHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
