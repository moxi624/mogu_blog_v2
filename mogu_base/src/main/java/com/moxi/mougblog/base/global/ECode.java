package com.moxi.mougblog.base.global;

/**
 * 状态码常量
 *
 * @author: 陌溪
 * @create: 2019-12-04-14:56
 */
public class ECode {

    /**
     * 操作成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 操作失败，请求参数有误
     */
    public static final Integer ERROR = 400;

    /**
     * 未通过token验证
     */
    public static final Integer UNAUTHORIZED = 401;

    /**
     * 无操作权限
     */
    public static final Integer NO_OPERATION_AUTHORITY = 402;

    /**
     * 服务器出现异常
     */
    public static final Integer SERVER_ERROR = 500;

    /**
     * 请求次数过于频繁
     */
    public static final Integer REQUEST_OVER_LIMIT = 502;
}
