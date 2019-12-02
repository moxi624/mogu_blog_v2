package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 管理员和角色关系表
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Data
@TableName("t_admin_role")
public class AdminRole extends SuperEntity<AdminRole>{

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    private String adminUid;

    /**
     * 角色id
     */
    private String roleUid;
    
    
    //以下字段不存入数据库，封装为了更好使用
    
    /**
     * 管理员
     */
    @TableField(exist = false)
    private Admin admin;

    /**
     * 角色
     */
    @TableField(exist = false)
    private Role role;

}
