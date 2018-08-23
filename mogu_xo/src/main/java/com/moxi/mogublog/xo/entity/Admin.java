package com.moxi.mogublog.xo.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;

import com.moxi.mougblog.base.entity.BaseEntity;

/**
 * 管理员实体类
 * @author xuzhixiang
 * @date 2018年2月8日18:26:48
 *
 */
@Entity
public class Admin extends BaseEntity{
	
	private static final long serialVersionUID = -1246589946364932718L;
	private String username; //用户名
	private String password; //密码
	private int gender; // 性别(1:男2:女)
	private String avatar; //头像
	private String email; //邮箱
	private Date birthday; //生日
	private String mobile; //手机
	private String validcode; //验证码
	private String summary; //简介
	private int logincount; //登录次数
	private Timestamp lastlogintime; //最后登录时间
	private String lastloginip; //最后登录ip
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String usersname) {
		this.username = usersname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getValidcode() {
		return validcode;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
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
