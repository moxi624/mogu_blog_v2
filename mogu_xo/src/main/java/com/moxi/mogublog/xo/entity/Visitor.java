package com.moxi.mogublog.xo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 博主表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@TableName("t_visitor")
public class Visitor extends SuperEntity<Visitor> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 登录次数
     */
    private Integer login_count;

    /**
     * 最后登录时间
     */
    private Date last_login_time;

    /**
     * 最后登录IP
     */
    private String last_login_ip;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLogin_count() {
		return login_count;
	}

	public void setLogin_count(Integer login_count) {
		this.login_count = login_count;
	}

	public Date getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}

	public String getLast_login_ip() {
		return last_login_ip;
	}

	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
