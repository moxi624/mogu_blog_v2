package com.moxi.mougblog.base.global;

/**
 * 消息通知
 *
 * @author: 陌溪
 * @create: 2019-12-29-20:16
 */
public class BaseMessageConf {
    public static final String OPERATION_SUCCESS = "操作成功";
    public static final String OPERATION_FAIL = "操作失败";
    public static final String INSERT_SUCCESS = "插入成功";
    public static final String INSERT_FAIL = "插入失败";
    public static final String UPDATE_SUCCESS = "更新成功";
    public static final String UPDATE_FAIL = "更新失败";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String DELETE_FAIL = "删除失败";

    public static final String ENTITY_EXIST = "该实体已存在";
    public static final String ENTITY_NOT_EXIST = "该实体不存在";

    public static final String KEYWORD_IS_NOT_EMPTY = "关键字不能为空";

    public static final String COMMENT_IS_NOT_EXIST = "该评论不存在";

    public static final String NO_COMMENTS_OPEN = "网站未开启评论功能";

    public static final String BLOG_NO_OPEN_COMMENTS = "本博客未开启评论功能";

    public static final String LOGIN_DISABLE = "该账号已被封禁";
    public static final String LOGIN_ERROR = "登录失败，用户名或密码错误";
    public static final String NO_ROLE = "没有分配角色权限";
    public static final String INTERFACE_FREQUENTLY = "接口过于频繁调用";
    public static final String PARAM_INCORRECT = "传入参数有误！";
    public static final String BLOG_UNDER_THIS_SORT = "该分类下还有博客！";
    public static final String RESOURCE_UNDER_THIS_SORT = "该分类下还有资源！";
    public static final String PICTURE_UNDER_THIS_SORT = "该分类下还有图片！";
    public static final String DICT_DATA_UNDER_THIS_SORT = "该分类下还有字典数据！";
    public static final String BLOG_UNDER_THIS_TAG = "该标签下还有博客！";
    public static final String ADMIN_UNDER_THIS_ROLE = "该角色下还有管理员！";
    public static final String CHILDREN_MENU_UNDER_THIS_MENU = "该菜单下还有子菜单！";
    public static final String THIS_SORT_IS_TOP = "该分类已经在顶端！";
    public static final String THIS_TAG_IS_TOP = "该标签已经在顶端！";
    public final static String INVALID_TOKEN = "token令牌未被识别";

    public static final String DATA_NO_PRIVILEGE = "该数据无权限访问";

    public static final String RESTAPI_NO_PRIVILEGE = "您无权进行该操作";

    public static final String ACCESS_NO_PRIVILEGE = "该资源无权限访问";
    public static final String LOGIN_TIMEOUT = "您已退出，请重新登录";
}
