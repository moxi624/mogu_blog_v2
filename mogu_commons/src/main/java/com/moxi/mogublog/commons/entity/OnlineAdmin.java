package com.moxi.mogublog.commons.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 当前在线的管理员
 * @Author 陌溪
 * @Date 2020年6月9日16:02:46
 *
 */
@Data
public class OnlineAdmin
{
    /** 会话编号 */
    private String tokenId;

    /**
     * 管理员的UID
     */
    private String adminUid;

    /** 用户名称 */
    private String userName;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地址 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录时间 */
    private String roleName;

    /** 登录时间 */
    private String loginTime;
}
