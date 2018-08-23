package com.moxi.mogublog.xo.entity;

import java.util.List;

import javax.persistence.Entity;

import com.moxi.mougblog.base.entity.BaseEntity;

/**
 * 评论实体类
 * @author xuzhixiang
 * @date 2017年11月3日18:48:33
 *
 */
@Entity
public class Comment extends BaseEntity{

	private static final long serialVersionUID = -4862467942942784295L;
	private String useruid; //用户uid
	private String bloguid; //博客uid
	private String touid; //回复某条评论的uid
	private String touseruid; //回复某个人的uid
	private String username; //用户名
	private String content; //内容
	
	//以下字段不存入数据库，封装为了更好使用
	private String blogtitle;  //博客标题
	private String tousername; //回复的用户名
	private String useravatar; //用户头像
	private List<Comment> reply; //该评论下的二级评论

	public String getUseruid() {
		return useruid;
	}
	public void setUseruid(String useruid) {
		this.useruid = useruid;
	}
	public String getTouid() {
		return touid;
	}
	public void setTouid(String touid) {
		this.touid = touid;
	}
	public String getTouseruid() {
		return touseruid;
	}
	public void setTouseruid(String touseruid) {
		this.touseruid = touseruid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBloguid() {
		return bloguid;
	}
	public void setBloguid(String bloguid) {
		this.bloguid = bloguid;
	}
	public String getBlogtitle() {
		return blogtitle;
	}
	public void setBlogtitle(String blogtitle) {
		this.blogtitle = blogtitle;
	}
	public String getTousername() {
		return tousername;
	}
	public void setTousername(String tousername) {
		this.tousername = tousername;
	}
	public String getUseravatar() {
		return useravatar;
	}
	public void setUseravatar(String useravatar) {
		this.useravatar = useravatar;
	}
	public List<Comment> getReply() {
		return reply;
	}
	public void setReply(List<Comment> reply) {
		this.reply = reply;
	}	
}
