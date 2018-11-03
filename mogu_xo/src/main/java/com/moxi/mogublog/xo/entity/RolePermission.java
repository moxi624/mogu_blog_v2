package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 角色和权限关系表
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@TableName("t_role_permission")
public class RolePermission extends SuperEntity<RolePermission>{
	
    private static final long serialVersionUID = 1L;

    /**
     * 角色uid
     */
    private String roleUid;
    
    /**
     * 权限uid
     */
    private String permissionUid;
    
	public String getPermissionUid() {
		return permissionUid;
	}

	public void setPermissionUid(String permissionUid) {
		this.permissionUid = permissionUid;
	}

	public String getRoleUid() {
		return roleUid;
	}

	public void setRoleUid(String roleUid) {
		this.roleUid = roleUid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
