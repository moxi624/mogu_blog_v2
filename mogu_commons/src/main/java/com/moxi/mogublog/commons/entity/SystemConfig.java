package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author xuzhixiang
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
     * 图片是否上传七牛云 (0:否， 1：是)
     */
    private String uploadQiNiu;

    /**
     * 图片是否上传本地存储 (0:否， 1：是)
     */
    private String uploadLocal;

    /**
     * 图片显示优先级（ 1 展示 七牛云,  0 本地）
     */
    private String picturePriority;

    /**
     * 本地存储图片服务器，域名前缀：   http://localhost:8600
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String localPictureBaseUrl;

    /**
     * 七牛云存储图片服务器，域名前缀： http://images.moguit.cn
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String qiNiuPictureBaseUrl;

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

}
