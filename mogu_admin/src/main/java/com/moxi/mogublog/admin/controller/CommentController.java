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
import com.moxi.mogublog.admin.helper.CommentHP;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.service.CommentSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;

/**
 * 评论管理Controller
 * @author xuzhixiang
 * @date 2017年12月29日16:01:53
 *
 */
@Controller
public class CommentController extends BaseController{
	
	@Autowired
	CommentSO commentSO;
	
	/**
	 * 评论管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/comment/list" }, method = RequestMethod.POST)
	@ResponseBody
	public String commentList(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = CommentHP.getList(model);
		return toJson(model);
	}
	
	/**
	 * 删除评论
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/comment/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String commentDelete(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Comment comment = commentSO.get(uid);
			if(comment != null) {
				comment.setStatus(EStatus.DISABLED);
				if (commentSO.update(comment)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	/**
	 * 冻结评论
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/comment/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String commentFreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Comment comment = commentSO.get(uid);
			if(comment != null) {
				comment.setStatus(EStatus.FREEZE);
				if (commentSO.update(comment)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻评论
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/comment/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String commentUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Comment comment = commentSO.get(uid);
			if(comment != null) {
				comment.setStatus(EStatus.ENABLE);
				if (commentSO.update(comment)) {
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 评论搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/comment/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String commentSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = CommentHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
