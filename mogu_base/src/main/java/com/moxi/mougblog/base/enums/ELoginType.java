package com.moxi.mougblog.base.enums;

/**
 * 网站登录方式枚举类
 *
 * @Author: 陌溪
 * @Date: 2020年10月14日11:10:04
 */
public enum ELoginType {

    /**
     * 账号密码
     */
    PASSWORD("1", "PASSWORD"),

    /**
     * 码云
     */
    GITEE("2", "GITEE"),

    /**
     * GITHUB
     */
    GITHUB("3", "GITHUB"),

    /**
     * QQ
     */
    QQ("4", "QQ"),

    /**
     * Gitee
     */
    WECHAT("5", "WECHAT");


    private final String code;
    private final String name;

    ELoginType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}