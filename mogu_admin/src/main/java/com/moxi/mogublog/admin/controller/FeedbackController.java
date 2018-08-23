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
import com.moxi.mogublog.admin.helper.FeedbackHP;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Feedback;
import com.moxi.mogublog.xo.service.FeedbackSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;



/**
 * 反馈管理Controller
 * @author xuzhixiang
 * @date 2017年10月2日20:08:02
 *
 */
@Controller
public class FeedbackController extends BaseController{
	
	@Autowired
	FeedbackSO feedbackSO;
	
	/**
	 * 反馈管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/feedback/list" }, method = RequestMethod.POST)
	@ResponseBody
	public String feedbackList(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = FeedbackHP.getList(model);
		return toJson(model);
	}
	
	/**
	 * 添加反馈
	 * @param request
	 * @param useruid
	 * @param songuid
	 * @param type
	 * @return
	 */
	@RequestMapping(value = {"/feedback/add" }, method = RequestMethod.POST)
	@ResponseBody
	public String feedbackAdd(HttpServletRequest request, String useruid, String bloguid) {
		Feedback feedback = new Feedback();
		if(StrUtils.isNotEmpty(useruid) && StrUtils.isNotEmpty(bloguid) ) {
			feedback.setUseruid(useruid);
			if (feedbackSO.insert(feedback)) {
				return "success";
			}
		}
		return "failer";
	}
	
	/**
	 * 取消反馈
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/feedback/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String feedbackDelete(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Feedback feedback = feedbackSO.get(uid);
			if(feedback != null) {
				feedback.setStatus(EStatus.DISABLED);
				if (feedbackSO.update(feedback)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 冻结反馈
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/feedback/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String feedbackFreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Feedback feedback = feedbackSO.get(uid);
			if(feedback != null) {
				feedback.setStatus(EStatus.FREEZE);
				if (feedbackSO.update(feedback)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻反馈
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/feedback/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String feedbackUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Feedback feedback = feedbackSO.get(uid);
			if(feedback != null) {
				feedback.setStatus(EStatus.ENABLE);
				if (feedbackSO.update(feedback)) {
					return "success";
				}
			} 
		}
		return "failer";
	}

	/**
	 * 反馈搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/feedback/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String feedbackSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = FeedbackHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
