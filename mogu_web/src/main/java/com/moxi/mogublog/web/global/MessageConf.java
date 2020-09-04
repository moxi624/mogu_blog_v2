package com.moxi.mogublog.web.global;

import com.moxi.mougblog.base.global.BaseMessageConf;

/**
 * 消息通知
 *
 * @author 陌溪_
 * @date 2019年12月29日20:20:14
 */
public final class MessageConf extends BaseMessageConf {

    public static final String CAN_NOT_REPORT_YOURSELF_COMMENTS = "不能举报自己的评论";

    public static final String CAN_NOT_REPEAT_REPORT_COMMENT = "不能重复举报该评论";

    public static final String COMMENT_CAN_NOT_MORE_THAN_225 = "评论不能超过225个字符";

    public static final String COMMENT_CAN_NOT_MORE_THAN_1024 = "评论不能超过1024个字符";

    public static final String COMMENT_IS_SPAM = "请输入有意义的评论内容！";

    public static final String YOU_DONT_HAVE_PERMISSION_TO_SPEAK = "您没有发言权限！";

    public static final String YOU_DONT_HAVE_PERMISSION_TO_REPLY = "您没有申请权限！";

    public static final String YOU_DONT_HAVE_PERMISSION_TO_FEEDBACK = "您没有反馈权限！";

    public static final String PLEASE_TRY_AGAIN_IN_AN_HOUR = "由于发送过多无意义评论，您已被禁言一小时，请稍后在试~";

    public static final String BLOG_LINK_IS_EXIST = "您申请的友链，已经在申请列表中！";

    public static final String BLOG_LINK_IS_PUBLISH = "您申请的友链，已经发布!";

    public static final String BLOG_LINK_IS_NO_PUBLISH = "您申请的友链，已经下架！";

    public static final String PASSWORD_IS_ERROR = "输入密码有误！";

    public static final String SYSTEM_PARAMS_NOT_FOUNT = "系统配置不存在！";

}
