package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@TableName("t_role")
public class Role extends SuperEntity<Role>{
	
	private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 介绍
     */
    private String summary;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
