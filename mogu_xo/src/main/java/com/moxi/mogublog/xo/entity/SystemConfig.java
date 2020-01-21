package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

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
