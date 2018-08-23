package com.moxi.mogublog.xo.entity;

import javax.persistence.Entity;

import com.moxi.mougblog.base.entity.BaseEntity;

/**
 * 收藏实体类
 * @author xuzhixiang
 * @date 2017年11月3日18:41:53
 *
 */
@Entity
public class Collect extends BaseEntity{

	private static final long serialVersionUID = -4862467942942784295L;
	private String useruid; //用户uid
	private String bloguid; //博客uid
	
	//以下字段不存入数据库，封装为了更好使用
	private String username;
	private String collectname;
	
	public String getUseruid() {
		return useruid;
	}
	public void setUseruid(String useruid) {
		this.useruid = useruid;
	}
	public String getBloguid() {
		return bloguid;
	}
	public void setBloguid(String bloguid) {
		this.bloguid = bloguid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCollectname() {
		return collectname;
	}
	public void setCollectname(String collectname) {
		this.collectname = collectname;
	}
}
