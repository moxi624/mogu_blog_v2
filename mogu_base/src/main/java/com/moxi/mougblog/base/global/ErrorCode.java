package com.moxi.mougblog.base.global;

/**
 * 操作状态码常量
 *
 * @author: 陌溪
 * @create: 2020年9月9日17:17:52
 */
public class ErrorCode {

    //=========================================
    // 查询相关异常码
    //=========================================

    /**
     * 查询操作默认异常码
     */
    public static final String QUERY_DEFAULT_ERROR = "00100";
    public static final String SYSTEM_CONFIG_IS_NOT_EXIST = "00101";
    public static final String PLEASE_CONFIGURE_PASSWORD = "00102";
    public static final String PLEASE_CONFIGURE_BLOG_COUNT = "00103";
    public static final String PLEASE_CONFIGURE_TAG_COUNT = "00104";
    public static final String PLEASE_CONFIGURE_LINK_COUNT = "00105";
    public static final String PLEASE_CONFIGURE_SYSTEM_PARAMS = "00106";
    public static final String PLEASE_SET_QI_NIU = "00107";
    public static final String PLEASE_SET_LOCAL = "00108";
    public static final String PLEASE_SET_MINIO = "00109";
    public static final String SYSTEM_CONFIG_NOT_EXIST = "00110";


    //=========================================
    // 新增操作相关异常码
    //=========================================

    /**
     * 插入操作默认异常码
     */
    public static final String INSERT_DEFAULT_ERROR = "00200";

    //=========================================
    // 更新操作相关异常码
    //=========================================

    /**
     * 更新操作默认异常码
     */
    public static final String UPDATE_DEFAULT_ERROR = "00300";


    //=========================================
    // 删除操作相关异常码
    //=========================================

    /**
     * 删除操作默认异常码
     */
    public static final String DELETE_DEFAULT_ERROR = "00400";
    public static final String DELETE_FAILED_PLEASE_CHECK_UID = "00401";

    /**
     * 操作成功
     */
    public static final String OPERATION_SUCCESS = "00000";

    /**
     * 操作失败
     */
    public static final String OPERATION_FAIL = "00001";

    /**
     * 该数据不存在
     */
    public static final String DATA_NO_EXIST = "00002";

    /**
     * 该账号未激活
     */
    public static final String LOGIN_NOT_ACTIVE = "00003";

    /**
     * 该账号已被封禁
     */
    public static final String LOGIN_DISABLE = "00004";

    /**
     * 登录失败，用户名或密码错误
     */
    public static final String LOGIN_ERROR = "00005";

    /**
     * 接口过于频繁调用
     */
    public static final String INTERFACE_FREQUENTLY = "00006";

    /**
     * 传入参数有误！
     */
    public static final String PARAM_INCORRECT = "00007";

    /**
     * 令牌未被识别
     */
    public final static String INVALID_TOKEN = "00008";

    /**
     * 该数据无权限访问
     */
    public static final String DATA_NO_PRIVILEGE = "00997";

    /**
     * 该资源无权限访问
     */
    public static final String ACCESS_NO_PRIVILEGE = "00998";

    /**
     * 您已退出，请重新登录
     */
    public static final String LOGIN_TIMEOUT = "00999";


    public static final String ERROR = "error";
}
