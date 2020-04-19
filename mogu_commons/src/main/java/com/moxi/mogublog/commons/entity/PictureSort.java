package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * 相册分类实体类
 *
 * @author xuzhixiang
 * @date 2018年9月17日16:10:38
 */
@Data
@TableName("t_picture_sort")
public class PictureSort extends SuperEntity<PictureSort> {

    /**
     *
     */
    private static final long serialVersionUID = 3454006152368184626L;

    /**
     * 父UID
     */
    private String parentUid;

    /**
     * 分类名
     */
    private String name;

    /**
     * 分类图片Uid
     */
    private String fileUid;

    /**
     * 排序字段，数值越大，越靠前
     */
    private int sort;

    /**
     * 是否显示  1: 是  0: 否
     */
    private Integer isShow;

    //以下字段不存入数据库

    /**
     * 分类图
     */
    @TableField(exist = false)
    private List<String> photoList;

}
