package com.moxi.mogublog.xo.entity;

import javax.persistence.Entity;

import com.moxi.mougblog.base.entity.BaseEntity;

/**
 * 博客实体类
 * @author xuzhixiang
 * @date 2017年11月3日18:39:11
 *
 */
@Entity
public class Blog extends BaseEntity{

	private static final long serialVersionUID = 1L;
	private String title; //博客标题
	private String summary; //简介
	private String content; //博客内容
	private String taguid; //标签uid
	private int clickcount; //博客点击数
	private int collectcount; //博客收藏数
	private String photo; //标题图
	
	//以下字段不存入数据库，封装为了方便使用
	private String tagname;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTaguid() {
		return taguid;
	}
	public void setTaguid(String taguid) {
		this.taguid = taguid;
	}
	public int getClickcount() {
		return clickcount;
	}
	public void setClickcount(int clickcount) {
		this.clickcount = clickcount;
	}
	public int getCollectcount() {
		return collectcount;
	}
	public void setCollectcount(int collectcount) {
		this.collectcount = collectcount;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	
}
