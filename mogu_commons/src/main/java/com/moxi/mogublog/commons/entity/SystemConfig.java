package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * 系统配置表
 *
 * @author 陌溪
 * @since 2020年1月21日09:02:29
 */
@Data
@TableName("t_system_config")
public class SystemConfig extends SuperEntity<SystemConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 七牛云公钥
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qiNiuAccessKey;

    /**
     * 七牛云私钥
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qiNiuSecretKey;

    /**
     * 七牛云上传空间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qiNiuBucket;

    /**
     * 七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qiNiuArea;

    /**
     * Minio远程连接地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String minioEndPoint;

    /**
     * Minio公钥
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String minioAccessKey;

    /**
     * Minio私钥
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String minioSecretKey;

    /**
     * Minio桶
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String minioBucket;

    /**
     * 图片是否上传七牛云 (0:否， 1：是)
     */
    private String uploadQiNiu;

    /**
     * 图片是否上传本地存储 (0:否， 1：是)
     */
    private String uploadLocal;

    /**
     * 文件是否上传Minio (0:否， 1：是)
     */
    private String uploadMinio;

    /**
     * 标题图片显示优先级（ 0:本地  1: 七牛云 2: Minio）
     */
    private String picturePriority;

    /**
     * 博客详情图片显示优先级（ 0:本地  1: 七牛云 2: Minio）
     */
    private String contentPicturePriority;

    /**
     * 本地存储图片服务器，域名前缀:   http://localhost:8600
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String localPictureBaseUrl;

    /**
     * 七牛云存储图片服务器，域名前缀: http://images.moguit.cn
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qiNiuPictureBaseUrl;

    /**
     * Minio服务器文件域名前缀： http://minio.moguit.cn
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String minioPictureBaseUrl;

    /**
     * 邮箱账号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    /**
     * 邮箱发件人用户名
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String emailUserName;

    /**
     * 邮箱密码
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String emailPassword;

    /**
     * SMTP地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String smtpAddress;

    /**
     * SMTP端口
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String smtpPort;

    /**
     * 是否开启邮件通知(0:否， 1:是)
     * 当有新的反馈，友链申请时进行通知，首先需要在系统管理处设置接收通知的邮箱
     */
    private String startEmailNotification;

    /**
     * 编辑器模式，(0：富文本编辑器CKEditor，1：markdown编辑器Veditor)
     */
    private String editorModel;

    /**
     * 主题颜色
     */
    private String themeColor;

    /**
     * 仪表盘通知【首次进入时弹出】
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String dashboardNotification;

    /**
     * 是否开启仪表盘通知【0 关闭，1 开启】
     */
    private String openDashboardNotification;

    /**
     * 是否开启用户邮件激活功能【0 关闭，1 开启】
     */
    private String openEmailActivate;

    /**
     * 搜索模式：0:SQL搜索 、1：全文检索
     */
    private String searchModel;
}
