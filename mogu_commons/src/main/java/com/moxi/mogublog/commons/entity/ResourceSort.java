package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 资源分类表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年10月19日20:46:51
 */
@Data
@TableName("t_resource_sort")
public class ResourceSort extends SuperEntity<ResourceSort> {

    private static final long serialVersionUID = 1L;


    /**
     * 分类名
     */
    private String sortName;

    /**
     * 分类介绍
     */
    private String content;

    /**
     * 分类图片UID
     */
    private String fileUid;

    /**
     * 分类点击数
     */
    private String clickCount;

    /**
     * 排序字段，数值越大，越靠前
     */
    private int sort;

    /**
     * 分类图
     */
    @TableField(exist = false)
    private List<String> photoList;
}
