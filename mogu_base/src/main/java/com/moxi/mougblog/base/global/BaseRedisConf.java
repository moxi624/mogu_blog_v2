package com.moxi.mougblog.base.global;

/**
 * Redis常量基类
 *
 * @author: 陌溪
 * @create: 2020-03-06-20:43
 */
public class BaseRedisConf {

    /**
     * 请求限制
     */
    public final static String REQUEST_LIMIT = "REQUEST_LIMIT";

    /**
     * 博客分类
     */
    public final static String BLOG_SORT_BY_MONTH = "BLOG_SORT_BY_MONTH";

    /**
     * Redis分隔符
     */
    public final static String SEGMENTATION = ":";

    /**
     * 等于
     */
    public final static String EQUAL_TO = "=";

    /**
     * 井号
     */
    public final static String WELL_NUMBER = "#";

    /**
     * 月份集合
     */
    public final static String MONTH_SET = "MONTH_SET";

    /**
     * 博客等级
     */
    public final static String BLOG_LEVEL = "BLOG_LEVEL";

    /**
     * 博客点击
     */
    public final static String BLOG_CLICK = "BLOG_CLICK";

    /**
     * IP地址解析
     */
    public final static String IP_SOURCE = "IP_SOURCE";

    /**
     * 最热博客
     */
    public final static String HOT_BLOG = "HOT_BLOG";

    /**
     * 最新博客
     */
    public final static String NEW_BLOG = "NEW_BLOG";

    /**
     * 管理员Token
     */
    public final static String ADMIN_TOKEN = "ADMIN_TOKEN";

    /**
     * 系统配置
     */
    public final static String SYSTEM_CONFIG = "SYSTEM_CONFIG";

    /**
     * 表单重复提交
     */
    public final static String AVOID_REPEATABLE_COMMIT = "AVOID_REPEATABLE_COMMIT";

    /**
     * 登录限制
     */
    public final static String LOGIN_LIMIT = "LOGIN_LIMIT";

    /**
     * 字典类型
     */
    public final static String REDIS_DICT_TYPE = "REDIS_DICT_TYPE";

    /**
     * token令牌
     */
    public final static String USER_TOKEN = "USER_TOKEN";

    /**
     * 激活用户的时间
     */
    public final static String ACTIVATE_USER = "ACTIVATE_USER";

    /**
     * 登录用户的token
     */
    public final static String LOGIN_TOKEN_KEY = "LOGIN_TOKEN_KEY";

    /**
     * 登录的UUID
     */
    public final static String LOGIN_UUID_KEY = "LOGIN_UUID_KEY";

    /**
     * 用户提交无效评论的次数
     */
    public final static String USER_PUBLISH_SPAM_COMMENT_COUNT = "USER_PUBLISH_SPAM_COMMENT_COUNT";

    /**
     * 管理员访问菜单
     */
    public final static String ADMIN_VISIT_MENU = "ADMIN_VISIT_MENU";

    /**
     * 博客点赞
     */
    public final static String BLOG_PRAISE = "BLOG_PRAISE";

    /**
     * ALL
     */
    public final static String ALL = "ALL";

    /**
     * 标签点击
     */
    public final static String TAG_CLICK = "TAG_CLICK";

    /**
     * 系统参数配置
     */
    public final static String SYSTEM_PARAMS = "SYSTEM_PARAMS";

    /**
     * 网站配置
     */
    public final static String WEB_CONFIG = "WEB_CONFIG";

    /**
     * 首页展示
     */
    public final static String DASHBOARD = "DASHBOARD";

    /**
     * 一周访问量
     */
    public final static String WEEK_VISIT = "WEEK_VISIT";

    /**
     * 博客标签下包含的博客数量
     */
    public final static String BLOG_COUNT_BY_TAG = "BLOG_COUNT_BY_TAG";

    /**
     * 博客分类下包含的博客数量
     */
    public final static String BLOG_COUNT_BY_SORT = "BLOG_COUNT_BY_SORT";

    /**
     * 全年博客贡献数
     */
    public final static String BLOG_CONTRIBUTE_COUNT = "BLOG_CONTRIBUTE_COUNT";

    public final static String BLOG_LINK = "BLOG_LINK";

    public final static String BLOG_TAG = "BLOG_TAG";

    /**
     * 用户收到评论数
     */
    public final static String USER_RECEIVE_COMMENT_COUNT = "USER_RECEIVE_COMMENT_COUNT";

    /**
     * 登录方式
     */
    public final static String LOGIN_TYPE = "LOGIN_TYPE";
    public final static String GITEE = "GITEE";
    public final static String GITHUB = "GITHUB";
    public final static String QQ = "QQ";
    public final static String PASSWORD = "PASSWORD";
    public final static String WECHAT = "WECHAT";


}
