package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * Web访问记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年12月8日09:27:17
 */
@Data
@TableName("t_web_visit")
public class WebVisit extends SuperEntity<WebVisit> {

    private static final long serialVersionUID = 1L;


    /**
     * 用户UID
     */
    private String userUid;

    /**
     * 用户IP
     */
    private String ip;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 用户访问行为   (点击了文章，点击了标签，点击了分类，进行了搜索)
     */
    private String behavior;

    /**
     * 文章uid，标签uid，分类uid
     */
    private String moduleUid;

    /**
     * 附加数据(比如搜索内容)
     */
    private String otherData;


    // 以下字段不存入数据库

    /**
     * 内容(点击的博客名，点击的标签名，搜索的内容，点击的作者)
     */
    @TableField(exist = false)
    private String content;

    /**
     * 行为名称
     */
    @TableField(exist = false)
    private String behaviorContent;
}
