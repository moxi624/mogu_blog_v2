package com.moxi.mogublog.xo.entity;

import javax.persistence.Entity;

import com.moxi.mougblog.base.entity.BaseEntity;

/**
 * 标签实体类
 * @author xuzhixiang
 * @date 2017年11月3日18:50:22
 *
 */
@Entity
public class Tag extends BaseEntity{

	private static final long serialVersionUID = -1177787058917755230L;
	private String content; //标签名
	private int clickcount; //点击数
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getClickcount() {
		return clickcount;
	}
	public void setClickcount(int clickcount) {
		this.clickcount = clickcount;
	}
}
