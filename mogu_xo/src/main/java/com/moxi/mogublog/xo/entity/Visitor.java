package com.moxi.mogublog.xo.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;

import com.moxi.mougblog.base.entity.BaseEntity;

/**
 * 游客实体类
 * @author xuzhixiang
 * @date 2017年11月3日18:53:16
 *
 */
@Entity
public class Visitor extends BaseEntity{

	private static final long serialVersionUID = 1161512248235762111L;
	private String username; //用户名
	private String email; //邮箱
	private int logincount; //登录次数
	private Timestamp lastlogintime; //最后登录时间
	private String lastloginip; //最后登录ip
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getLogincount() {
		return logincount;
	}
	public void setLogincount(int logincount) {
		this.logincount = logincount;
	}
	public Timestamp getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(Timestamp lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	public String getLastloginip() {
		return lastloginip;
	}
	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}
}