package com.moxi.mogublog.xo.vo;

import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

/**
 * <p>
 * 网站配置VO
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月11日14:54:12
 */
@Data
public class SystemConfigVO extends BaseVO<SystemConfigVO> {

    /**
     * 七牛云公钥
     */
    private String qiNiuAccessKey;

    /**
     * 七牛云私钥
     */
    private String qiNiuSecretKey;

    /**
     * 邮箱账号
     */
    private String email;

    /**
     * 邮箱发件人用户名
     */
    private String emailUserName;

    /**
     * 邮箱密码
     */
    private String emailPassword;

    /**
     * SMTP地址
     */
    private String smtpAddress;

    /**
     * SMTP端口
     */
    private String smtpPort;

}
