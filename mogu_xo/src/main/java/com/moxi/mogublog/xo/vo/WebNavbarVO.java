package com.moxi.mogublog.xo.vo;

import com.moxi.mougblog.base.validator.annotion.IntegerNotNull;
import com.moxi.mougblog.base.validator.annotion.NotBlank;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

/**
 * 门户页导航栏VO
 *
 * @author 陌溪
 * @since 2021年2月22日16:51:14
 */
@Data
public class WebNavbarVO extends BaseVO<WebNavbarVO> {

    /**
     * 菜单名称
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String name;

    /**
     * 导航栏级别 （一级分类，二级分类）
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer navbarLevel;

    /**
     * 介绍
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
    @NotBlank(groups = {Insert.class, Update.class})
    private String url;

    /**
     * 排序字段(越大越靠前)
     */
    private Integer sort;

    /**
     * 是否显示  1: 是  0: 否
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer isShow;

    /**
     * 是否跳转外部URL，如果是，那么路由为外部的链接
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer isJumpExternalUrl;
}
