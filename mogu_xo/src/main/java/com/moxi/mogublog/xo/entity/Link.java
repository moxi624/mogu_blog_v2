package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 友情链接表
 * </p>
 *
 * @author xuzhixiang
 * @since 22018年9月26日09:54:43
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
     * 点击数
     */
    private Integer clickCount;

    /**
     * 排序字段
     */
    private Integer sort;
}
