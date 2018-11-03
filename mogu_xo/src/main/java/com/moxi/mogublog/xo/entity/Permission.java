package com.moxi.mogublog.xo.entity;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;


/**
 * <p>
 * 权限信息表
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@TableName("t_permission")
public class Permission extends SuperEntity<Permission>{
	
	private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String permissionName;
    
    /**
     * 权限路径
     */
    private String url;
    
    /**
     * 上一级权限uid
     */
    private String parentId;
    
    /**
     * 图标类型
     */
    private String icon;
    
    /**
     * 是否选中
     */
    @TableField(exist = false)
    private boolean checked = false;
    
    /**
     * 子权限
     */
    @TableField(exist = false)
    private List<Permission> children = new ArrayList<Permission>();

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Permission> getChildren() {
		return children;
	}

	public void setChildren(List<Permission> children) {
		this.children = children;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
