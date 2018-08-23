package com.moxi.mogublog.admin.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.BlogSO;
import com.moxi.mogublog.xo.service.CommentSO;
import com.moxi.mogublog.xo.service.UserSO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.helper.BaseHP;

/**
 * 评论管理HP
 * @author xuzhixiang
 * @date 2017年12月29日16:00:27
 *
 */
public class CommentHP extends BaseHP{
	
	static CommentSO commentSO;
	
	static UserSO userSO;
	
	static BlogSO blogSO;
	
	private static CommentSO getCommentSO () {
		if (commentSO == null) {
			commentSO = getApplicationContext().getBean(CommentSO.class);
		}
		return commentSO;
	}
	
	private static UserSO getUserSO() {
		if (userSO == null) {
			userSO = getApplicationContext().getBean(UserSO.class);
		}
		return userSO;
	}
	
	private static BlogSO getBlogSO() {
		if (blogSO == null) {
			blogSO = getApplicationContext().getBean(BlogSO.class);
		}
		return blogSO;
	}
	
	/**
	 * 获取用户list
	 * @return
	 */
	public static Map<String, Object> getList(Map<String, Object> model) {
		Map<String, Object> map = getMap();
		map.put(SysConf.status, EStatus.ENABLE);
		List<Comment> list= getCommentSO().getList(map);
		model.put(SysConf.rows, list);
		model.put(SysConf.total, list.size());
		return model;
	}
	
	/**
	 * 搜索用户list
	 * @author xuzhixiang
	 * @date 2017年12月29日14:32:13
	 * @return
	 */
	public static Map<String, Object> getSearchList(Map<String, Object> model, String keyword) {
		Map<String, Object> map = getMap();
		if(StrUtils.isNotEmpty(keyword)) {
			map.put(SysConf.keyword, keyword);
		}	
		List<Comment> list= getCommentSO().getList(map);
		List<Comment> newList = new ArrayList<Comment>();
		for(Comment comment : list) {
			if(comment.getStatus() != EStatus.DISABLED) {
				Blog blog = getBlogSO().get(comment.getBloguid());
				if(blog != null) {
					comment.setBlogtitle(blog.getTitle());
				}
				//获取发送的用户名
				User touser = getUserSO().get(comment.getTouseruid());
				if(touser != null) {
					comment.setTousername(touser.getUsername());
				}
				//获取发送消息的用户名
				User user = getUserSO().get(comment.getUseruid());
				if(user != null) {
					comment.setUsername(user.getUsername());
				}
				newList.add(comment);
			}
		}
		model.put(SysConf.rows, newList);
		model.put(SysConf.total, newList.size());
		return model;
	}
}
