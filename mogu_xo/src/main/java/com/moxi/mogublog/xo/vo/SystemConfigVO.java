package com.moxi.mogublog.xo.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

/**
 * 网站配置VO
 *
 * @author 陌溪
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
     * 七牛云上传空间
     */
    private String qiNiuBucket;

    /**
     * 七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)
     */
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
     * 标题图片显示优先级（ 1 展示 七牛云,  0 本地）
     */
    private String picturePriority;

    /**
     * 封面图片显示优先级（ 1 展示 七牛云,  0 本地）
     */
    private String contentPicturePriority;

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
     * 文件是否上传Minio (0:否， 1：是)
     */
    private String uploadMinio;

    /**
     * Minio远程连接地址
     */
    private String minioEndPoint;

    /**
     * Minio公钥
     */
    private String minioAccessKey;

    /**
     * Minio私钥
     */
    private String minioSecretKey;

    /**
     * Minio桶
     */
    private String minioBucket;

    /**
     * Minio服务器文件域名前缀： http://minio.moguit.cn
     */
    private String minioPictureBaseUrl;

    /**
     * 仪表盘通知【首次进入时弹出】
     */
    private String dashboardNotification;

    /**
     * 是否开启仪表盘通知【0 关闭，1 开启】
     */
    private String openDashboardNotification;

}
