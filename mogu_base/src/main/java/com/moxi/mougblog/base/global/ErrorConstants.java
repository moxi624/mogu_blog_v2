package com.moxi.mougblog.base.global;

/**
 * 状态码常量
 *
 * @author: 陌溪
 * @create: 2019-12-04-14:56
 */
public class ErrorConstants {
    public static final String OPERATION_SUCCESS = "00000"; // 操作成功
    public static final String OPERATION_FAIL = "00001"; // 操作失败
    public static final String DATA_NO_EXIST = "00002"; // 该数据不存在

    public static final String LOGIN_NOT_ACTIVE = "00003"; // 该账号未激活
    public static final String LOGIN_DISABLE = "00004"; // 该账号已被封禁
    public static final String LOGIN_ERROR = "00005"; // 登录失败，用户名或密码错误
    public static final String INTERFACE_FREQUENTLY = "00006"; // 接口过于频繁调用
    public static final String PARAM_INCORRECT = "00007"; // 传入参数有误！
    public final static String INVALID_TOKEN = "00008"; // 令牌未被识别

    public static final String DATA_NO_PRIVILEGE = "00997"; // 该数据无权限访问
    public static final String ACCESS_NO_PRIVILEGE = "00998"; // 该资源无权限访问
    public static final String LOGIN_TIMEOUT = "00999"; // 您已退出，请重新登录
}
