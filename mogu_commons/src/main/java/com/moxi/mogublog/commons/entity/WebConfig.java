package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 网站配置表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月11日14:54:12
 */
@Data
@TableName("t_web_config")
public class WebConfig extends SuperEntity<WebConfig> {

    private static final long serialVersionUID = 1L;


    /**
     * 网站Logo
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String logo;

    /**
     * 网站名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;

    /**
     * 标题
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String title;

    /**
     * 描述
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String summary;

    /**
     * 关键字
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String keyword;

    /**
     * 作者
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String author;

    /**
     * 备案号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String recordNum;

    /**
     * 支付宝收款码FileId
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String aliPay;

    /**
     * 微信收款码FileId
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String weixinPay;

    /**
     * 是否开启网页端评论(0:否， 1:是)
     */
    private String openComment;

    /**
     * 是否开启移动端评论(0:否， 1:是)
     */
    private String openMobileComment;

    /**
     * 是否开启赞赏(0:否， 1:是)
     */
    private String openAdmiration;

    /**
     * 是否开启移动端赞赏(0:否， 1:是)
     */
    private String openMobileAdmiration;

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

    /**
     * QQ号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qqNumber;

    /**
     * QQ群
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qqGroup;

    /**
     * 微信号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String weChat;

    /**
     * 邮箱
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    /**
     * 显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）
     */
    private String showList;

    /**
     * 登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）
     */
    private String loginTypeList;


    // 以下字段不存入数据库，封装为了方便使用

    /**
     * 标题图
     */
    @TableField(exist = false)
    private List<String> photoList;

    /**
     * 支付宝付款码
     */
    @TableField(exist = false)
    private String aliPayPhoto;

    /**
     * 微信付款码
     */
    @TableField(exist = false)
    private String weixinPayPhoto;

}
