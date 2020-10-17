package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Data
@ToString
@TableName("t_user")
public class User extends SuperEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别(1:男2:女)
     */
    private String gender;

    /**
     * 个人头像(UID)
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 出生年月日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 手机
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
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 验证码
     */
    private String validCode;

    /**
     * 资料来源
     */
    private String source;

    /**
     * 平台uuid
     */
    private String uuid;

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
     * 评论状态，0 禁言， 1 正常
     */
    private Integer commentStatus;

    /**
     * 开启邮件通知：  0：关闭， 1：开启
     */
    private Integer startEmailNotification;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 用户标签  0：普通，1：管理员，2：博主
     */
    private Integer userTag;

    // 以下字段不存入数据库

    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String photoUrl;

}
