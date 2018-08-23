package com.moxi.mogublog.admin.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mogublog.admin.helper.BlogHP;
import com.moxi.mogublog.admin.helper.TagHP;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.TagSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;

/**
 * 标签管理Controller
 * @author xuzhixiang
 * @date 2017年12月28日10:21:48
 *
 */
@Controller
public class TagController extends BaseController{
	
	@Autowired
	TagSO tagSO;

	/**
	 * 标签管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/tag/list" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagList(HttpServletRequest request,Map<String, Object> model){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = TagHP.getList(model);
		return toJson(model);
	}
	
	/**
	 * 添加标签
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = {"/tag/add" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagAdd(HttpServletRequest request, String content){
		Tag entity = new Tag();
		if(StrUtils.isNotEmpty(content)) {
			entity.setContent(content);  
			entity.setClickcount(0); 
			if (tagSO.insert(entity)) {
				return "success";
			}
		}
		return "failer";
	}
	
	/**
	 * 删除标签
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/tag/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagDelete(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Tag tag = tagSO.get(uid);
			if(tag != null) {
				tag.setStatus(EStatus.DISABLED);
				if (tagSO.update(tag)) {
		        	BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 编辑标签
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = {"/tag/edit" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagEdit(HttpServletRequest request, Map<String, Object> model , String uid, String content ) throws IllegalStateException, IOException{	
		Tag tag = tagSO.get(uid);
		if(tag != null) {
			tag.setContent(content);
			if(tagSO.update(tag)){
				BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
				return "success";
			}
		}
		return "failer";
	}
	
	/**
	 * 冻结标签
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/tag/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagFreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Tag tag = tagSO.get(uid);
			if(tag != null) {
				tag.setStatus(EStatus.FREEZE);
				if (tagSO.update(tag)) {
					BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 置顶标签
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/tag/stick" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagStick(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Tag tag = tagSO.get(uid);
			if(tag != null) {
				tag.setStatus(EStatus.STICK);
				if (tagSO.update(tag)) {
					BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 标签取消置顶
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/tag/unstick" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagUnstick(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Tag tag = tagSO.get(uid);			
			if(tag != null) {
				if(tag.getStatus() == EStatus.STICK) {
					tag.setStatus(EStatus.ENABLE);
					if (tagSO.update(tag)) {
						BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
						return "success";
					}
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻标签
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = {"/tag/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid){
		if(StrUtils.isNotEmpty(uid)) {
			Tag tag = tagSO.get(uid);
			if(tag != null) {
				if(tag.getStatus() == EStatus.FREEZE) {
					tag.setStatus(EStatus.ENABLE);
					if (tagSO.update(tag)) {
						BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
						return "success";
					}
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 标签搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/tag/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String tagSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = TagHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
