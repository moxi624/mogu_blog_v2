package com.moxi.mogublog.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EChannel;


/**
 * 蘑菇博客后台管理首页Controller
 * @author xuzhixiang
 * @date 2017年11月7日15:22:23
 *
 */
@Controller
public class IndexController extends BaseController{
	
	/**
	 * 跳转对应的页面
	 * @param model
	 * @param channel
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/{channel:\\w+}" }, method = RequestMethod.GET)
	public String index(Map<String, Object> model, @PathVariable String channel, HttpServletResponse response) {
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		if(EChannel.blog_collect.equals(channel)) {
			return EChannel.blog_collect;
		} else if(EChannel.blog_blog.equals(channel)) {
			return EChannel.blog_blog;
		} else if(EChannel.blog_comment.equals(channel)) {
			return EChannel.blog_comment;
		} else if(EChannel.blog_feedback.equals(channel)) {
			return EChannel.blog_feedback;
		} else if(EChannel.blog_tag.equals(channel)) {
			return EChannel.blog_tag;
		} else if(EChannel.blog_user.equals(channel)) {
			return EChannel.blog_user;
		} else if(EChannel.blog_visitor.equals(channel)) {
			return EChannel.blog_visitor;
		}
		return "error";
	}
}
