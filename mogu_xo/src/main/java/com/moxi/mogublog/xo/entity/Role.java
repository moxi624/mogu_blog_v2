package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Data
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
    
    /**
     * 该角色所能管辖的区域
     */
    private String categoryMenuUids;
}
