package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * 友情链接表
 *
 * @author 陌溪
 * @since 2018年9月26日09:54:43
 */
@Data
@TableName("t_link")
public class Link extends SuperEntity<Link> {

    private static final long serialVersionUID = 1L;

    /**
     * 友链标题
     */
    private String title;

    /**
     * 友链介绍
     */
    private String summary;

    /**
     * 友链地址
     */
    private String url;

    /**
     * 友链状态： 0 申请中， 1：上线  2: 已下架
     */
    private Integer linkStatus;

    /**
     * 点击数
     */
    private Integer clickCount;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 管理员UID
     */
    private String adminUid;

    /**
     * 申请用户Uid
     */
    private String userUid;

    /**
     * 站长邮箱
     */
    private String email;

    /**
     * 网站图标uid
     */
    private String fileUid;

    /**
     * 网站图标URL 【该字段不存入数据库】
     */
    @TableField(exist = false)
    private List<String> photoList;

}
