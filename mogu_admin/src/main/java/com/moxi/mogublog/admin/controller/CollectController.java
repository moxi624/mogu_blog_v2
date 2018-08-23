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
import com.moxi.mogublog.admin.helper.CollectHP;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Collect;
import com.moxi.mogublog.xo.service.CollectSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;


/**
 * 收藏管理Controller
 * @author xuzhixiang
 * @date 2017年10月2日20:08:02
 *
 */
@Controller
public class CollectController extends BaseController{
	
	@Autowired
	CollectSO collectSO;
	
	/**
	 * 收藏管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/collect/list" }, method = RequestMethod.POST)
	@ResponseBody
	public String collectList(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = CollectHP.getList(model);
		return toJson(model);
	}
	
	/**
	 * 添加收藏
	 * @param request
	 * @param useruid
	 * @param songuid
	 * @param type
	 * @return
	 */
	@RequestMapping(value = {"/collect/add" }, method = RequestMethod.POST)
	@ResponseBody
	public String collectAdd(HttpServletRequest request, String useruid, String bloguid) {
		Collect collect = new Collect();
		if(StrUtils.isNotEmpty(useruid) && StrUtils.isNotEmpty(bloguid) ) {
			collect.setUseruid(useruid);
			collect.setBloguid(bloguid);
			if (collectSO.insert(collect)) {
				return "success";
			}
		}
		return "failer";
	}
	
	/**
	 * 取消收藏
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/collect/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String collectDelete(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Collect collect = collectSO.get(uid);
			if(collect != null) {
				collect.setStatus(EStatus.DISABLED);
				if (collectSO.update(collect)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 冻结收藏
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/collect/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String collectFreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Collect collect = collectSO.get(uid);
			if(collect != null) {
				collect.setStatus(EStatus.FREEZE);
				if (collectSO.update(collect)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻收藏
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/collect/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String collectUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Collect collect = collectSO.get(uid);
			if(collect != null) {
				collect.setStatus(EStatus.ENABLE);
				if (collectSO.update(collect)) {
					return "success";
				}
			} 
		}
		return "failer";
	}

	/**
	 * 收藏搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/collect/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String collectSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = CollectHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
