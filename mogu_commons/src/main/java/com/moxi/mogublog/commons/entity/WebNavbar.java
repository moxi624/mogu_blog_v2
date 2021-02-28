package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * 门户页导航栏
 *
 * @author 陌溪
 * @since 2021年2月22日16:32:59
 */
@Data
@TableName("t_web_navbar")
public class WebNavbar extends SuperEntity<WebNavbar> implements Comparable<WebNavbar> {

    private static final long serialVersionUID = 1L;

    /**
     * 导航栏名称
     */
    private String name;

    /**
     * 导航栏级别 （一级分类，二级分类）
     */
    private Integer navbarLevel;

    /**
     * 导航栏介绍
     */
    private String summary;

    /**
     * Icon图标
     */
    private String icon;

    /**
     * 父UID
     */
    private String parentUid;

    /**
     * URL地址
     */
    private String url;

    /**
     * 排序字段(越大越靠前)
     */
    private Integer sort;

    /**
     * 是否显示  1:是  0:否
     */
    private Integer isShow;

    /**
     * 是否跳转外部URL
     */
    private Integer isJumpExternalUrl;

    /**
     * 父菜单
     */
    @TableField(exist = false)
    private WebNavbar parentWebNavbar;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<WebNavbar> childWebNavbar;

    @Override
    public int compareTo(WebNavbar o) {

        if (this.sort >= o.getSort()) {
            return -1;
        }
        return 1;
    }
}
