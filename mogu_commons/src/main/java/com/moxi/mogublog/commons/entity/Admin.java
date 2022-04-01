package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Data
@TableName("t_admin")
public class Admin extends SuperEntity<Admin> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色Uid
     */
    private String roleUid;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 昵称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String nickName;

    /**
     * 性别(1:男2:女)
     */
    private String gender;

    /**
     * 个人头像
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String avatar;

    /**
     * 邮箱
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    /**
     * 出生年月日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 手机
     * updateStrategy = FieldStrategy.IGNORED ：表示更新时候忽略非空判断
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String mobile;

    /**
     * QQ号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qqNumber;

    /**
     * 微信号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String weChat;

    /**
     * 职业
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String occupation;

    /**
     * 自我简介最多150字
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String summary;

    /**
     * 个人履历（Markdown）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String personResume;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 最后登录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * github地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String github;

    /**
     * gitee地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String gitee;

    // 以下字段不存入数据库

    /**
     * 用户头像
     */
    @TableField(exist = false)
    private List<String> photoList;

    /**
     * 所拥有的角色名
     */
    @TableField(exist = false)
    private List<String> roleNames;

    /**
     * 所拥有的角色名
     */
    @TableField(exist = false)
    private Role role;

    /**
     * 验证码
     */
    @TableField(exist = false)
    private String validCode;

    /**
     * 已用网盘容量
     */
    @TableField(exist = false)
    private Long storageSize;

    /**
     * 最大网盘容量
     */
    @TableField(exist = false)
    private Long maxStorageSize;

    /**
     * 令牌UID【主要用于换取token令牌，防止token直接暴露到在线用户管理中】
     */
    @TableField(exist = false)
    private String tokenUid;

}
