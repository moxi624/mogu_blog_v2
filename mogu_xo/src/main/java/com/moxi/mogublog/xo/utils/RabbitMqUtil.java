package com.moxi.mogublog.xo.utils;

import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.xo.global.SysConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于RabbitMQ发送消息
 *
 * @author 陌溪
 * @date 2020年4月29日11:56:10
 */
@Slf4j
@RefreshScope
@Component
public class RabbitMqUtil {

    public static final String EXCHANGE_DIRECT = "exchange.direct";
    public static final String ROUTING_KEY_EMAIL = "mogu.email";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value(value = "${data.web.url}")
    private String dataWebUrl;
    @Value(value = "${data.website.url}")
    private String dataWebsiteUrl;

    /**
     * 网站英文名
     */
    @Value(value = "${data.web.project_name_en}")
    private String PROJECT_NAME_EN;
    /**
     * 网站中文名
     */
    @Value(value = "${data.web.project_name}")
    private String PROJECT_NAME;
    /**
     * 网站LOGO
     */
    @Value(value = "${data.web.logo}")
    private String LOGO;

    /**
     * 发送邮件
     *
     * @param email
     * @param text
     */
    private void sendEmail(String email, String text) {
        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.SUBJECT, PROJECT_NAME);
        result.put(SysConf.RECEIVER, email);
        result.put(SysConf.TEXT, text);
        //发送到RabbitMq
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT, ROUTING_KEY_EMAIL, result);
    }

    /**
     * 发送评论邮件
     */
    public void sendCommentEmail(Map<String, String> map) {
        String email = map.get(SysConf.EMAIL);
        String text = map.get(SysConf.TEXT);
        String toText = map.get(SysConf.TO_TEXT);
        String nickName = map.get(SysConf.NICKNAME);
        String toUserNickName = map.get(SysConf.TO_NICKNAME);
        String userUid = map.get(SysConf.USER_UID);
        String url = map.get(SysConf.URL);
        String content =
                "<html>\r\n" +
                        " <head>\r\n" +
                        "  <title> " + PROJECT_NAME + "</title>\r\n" +
                        " </head>\r\n" +
                        " <body>\r\n" +
                        "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'spam', 'ZC1222-PrLAp4T0Z7Z7UUMYzqLkb8a');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">    \r\n" +
                        "  <div id=\"qm_con_body\"><div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"\">\r\n" +
                        "<style>\r\n" +
                        "  .qmbox .email-body{color:#40485B;font-size:14px;font-family:-apple-system, \"Helvetica Neue\", Helvetica, \"Nimbus Sans L\", \"Segoe UI\", Arial, \"Liberation Sans\", \"PingFang SC\", \"Microsoft YaHei\", \"Hiragino Sans GB\", \"Wenquanyi Micro Hei\", \"WenQuanYi Zen Hei\", \"ST Heiti\", SimHei, \"WenQuanYi Zen Hei Sharp\", sans-serif;background:#f8f8f8;}.qmbox .pull-right{float:right;}.qmbox a{color:#FE7300;text-decoration:underline;}.qmbox a:hover{color:#fe9d4c;}.qmbox a:active{color:#b15000;}.qmbox .logo{text-align:center;margin-bottom:20px;}.qmbox .panel{background:#fff;border:1px solid #E3E9ED;margin-bottom:10px;}.qmbox .panel-header{font-size:18px;line-height:30px;padding:10px 20px;background:#fcfcfc;border-bottom:1px solid #E3E9ED;}.qmbox .panel-body{padding:20px;}.qmbox .container{width:50%;min-width:300px;padding:20px;margin:0 auto;}.qmbox .text-center{text-align:center;}.qmbox .thumbnail{padding:4px;max-width:100%;border:1px solid #E3E9ED;}.qmbox .btn-primary{color:#fff;font-size:16px;padding:8px 14px;line-height:20px;border-radius:2px;display:inline-block;background:#FE7300;text-decoration:none;}.qmbox .btn-primary:hover,.qmbox .btn-primary:active{color:#fff;}.qmbox .footer{color:#9B9B9B;font-size:12px;margin-top:40px;}.qmbox .footer a{color:#9B9B9B;}.qmbox .footer a:hover{color:#fe9d4c;}.qmbox .footer a:active{color:#b15000;}.qmbox .email-body#mail_to_teacher{line-height:26px;color:#40485B;font-size:16px;padding:0px;}.qmbox .email-body#mail_to_teacher .container,.qmbox .email-body#mail_to_teacher .panel-body{padding:0px;}.qmbox .email-body#mail_to_teacher .container{padding-top:20px;}.qmbox .email-body#mail_to_teacher .textarea{padding:32px;}.qmbox .email-body#mail_to_teacher .say-hi{font-weight:500;}.qmbox .email-body#mail_to_teacher .paragraph{margin-top:24px;}.qmbox .email-body#mail_to_teacher .paragraph .pro-name{color:#000000;}.qmbox .email-body#mail_to_teacher .paragraph.link{margin-top:32px;text-align:center;}.qmbox .email-body#mail_to_teacher .paragraph.link .button{background:#4A90E2;border-radius:2px;color:#FFFFFF;text-decoration:none;padding:11px 17px;line-height:14px;display:inline-block;}.qmbox .email-body#mail_to_teacher ul.pro-desc{list-style-type:none;margin:0px;padding:0px;padding-left:16px;}.qmbox .email-body#mail_to_teacher ul.pro-desc li{position:relative;}.qmbox .email-body#mail_to_teacher ul.pro-desc li::before{content:'';width:3px;height:3px;border-radius:50%;background:red;position:absolute;left:-15px;top:11px;background:#40485B;}.qmbox .email-body#mail_to_teacher .blackboard-area{height:600px;padding:40px;background-image:url();color:#FFFFFF;}.qmbox .email-body#mail_to_teacher .blackboard-area .big-title{font-size:32px;line-height:45px;text-align:center;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc{margin-top:8px;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc p{margin:0px;text-align:center;line-height:28px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(odd){float:left;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(even){float:right;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card .title{font-size:18px;text-align:center;margin-bottom:10px;}\r\n" +
                        "</style>\r\n" +
                        "<meta>\r\n" +
                        "<div class=\"email-body\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"container\">\r\n" +
                        "<div class=\"logo\">\r\n" +
                        "<img src=\"" + LOGO + "\",height=\"100\" width=\"100\">\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"panel-header\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "评论提醒\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel-body\">\r\n" +
                        "<p>您好 <a href=\"mailto:" + email + "\" rel=\"noopener\" target=\"_blank\">" + toUserNickName + "<wbr></a>！</p>\r\n" +
                        "<p>" + nickName + " 对您的评论：" + "<a href=\"" + url + "\">" + toText + "</a>" + "   进行了回复</p>\r\n" +
                        "\r\n" +
                        "<p>回复内容为：" + "<a href=\"" + url + "\">" + text + "</a>" + "</p>\r\n" +
                        "\r\n" +
                        "<p>如果邮件通知干扰了您，可以点击右侧链接关闭通知：" + "<a href=\"" + dataWebUrl + "/web/comment/closeEmailNotification/" + userUid + "\">点击这里</a>" + "</p>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<div class=\"footer\">\r\n" +
                        "<a href=\" " + dataWebsiteUrl + "\">@" + PROJECT_NAME_EN + "</a>\n" +
                        "<div class=\"pull-right\"></div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div></div><!-- --><style>#mailContentContainer .txt {height:auto;}</style>  </div>\r\n" +
                        " </body>\r\n" +
                        "</html>";
        sendEmail(email, content);
    }

    /**
     * 发送简单邮件
     *
     * @param email
     * @param text
     */
    public void sendSimpleEmail(String email, String text) {
        String content =
                "<html>\r\n" +
                        " <head>\r\n" +
                        "  <title> " + PROJECT_NAME + "</title>\r\n" +
                        " </head>\r\n" +
                        " <body>\r\n" +
                        "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'spam', 'ZC1222-PrLAp4T0Z7Z7UUMYzqLkb8a');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">    \r\n" +
                        "  <div id=\"qm_con_body\"><div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"\">\r\n" +
                        "<style>\r\n" +
                        "  .qmbox .email-body{color:#40485B;font-size:14px;font-family:-apple-system, \"Helvetica Neue\", Helvetica, \"Nimbus Sans L\", \"Segoe UI\", Arial, \"Liberation Sans\", \"PingFang SC\", \"Microsoft YaHei\", \"Hiragino Sans GB\", \"Wenquanyi Micro Hei\", \"WenQuanYi Zen Hei\", \"ST Heiti\", SimHei, \"WenQuanYi Zen Hei Sharp\", sans-serif;background:#f8f8f8;}.qmbox .pull-right{float:right;}.qmbox a{color:#FE7300;text-decoration:underline;}.qmbox a:hover{color:#fe9d4c;}.qmbox a:active{color:#b15000;}.qmbox .logo{text-align:center;margin-bottom:20px;}.qmbox .panel{background:#fff;border:1px solid #E3E9ED;margin-bottom:10px;}.qmbox .panel-header{font-size:18px;line-height:30px;padding:10px 20px;background:#fcfcfc;border-bottom:1px solid #E3E9ED;}.qmbox .panel-body{padding:20px;}.qmbox .container{width:50%;min-width:300px;padding:20px;margin:0 auto;}.qmbox .text-center{text-align:center;}.qmbox .thumbnail{padding:4px;max-width:100%;border:1px solid #E3E9ED;}.qmbox .btn-primary{color:#fff;font-size:16px;padding:8px 14px;line-height:20px;border-radius:2px;display:inline-block;background:#FE7300;text-decoration:none;}.qmbox .btn-primary:hover,.qmbox .btn-primary:active{color:#fff;}.qmbox .footer{color:#9B9B9B;font-size:12px;margin-top:40px;}.qmbox .footer a{color:#9B9B9B;}.qmbox .footer a:hover{color:#fe9d4c;}.qmbox .footer a:active{color:#b15000;}.qmbox .email-body#mail_to_teacher{line-height:26px;color:#40485B;font-size:16px;padding:0px;}.qmbox .email-body#mail_to_teacher .container,.qmbox .email-body#mail_to_teacher .panel-body{padding:0px;}.qmbox .email-body#mail_to_teacher .container{padding-top:20px;}.qmbox .email-body#mail_to_teacher .textarea{padding:32px;}.qmbox .email-body#mail_to_teacher .say-hi{font-weight:500;}.qmbox .email-body#mail_to_teacher .paragraph{margin-top:24px;}.qmbox .email-body#mail_to_teacher .paragraph .pro-name{color:#000000;}.qmbox .email-body#mail_to_teacher .paragraph.link{margin-top:32px;text-align:center;}.qmbox .email-body#mail_to_teacher .paragraph.link .button{background:#4A90E2;border-radius:2px;color:#FFFFFF;text-decoration:none;padding:11px 17px;line-height:14px;display:inline-block;}.qmbox .email-body#mail_to_teacher ul.pro-desc{list-style-type:none;margin:0px;padding:0px;padding-left:16px;}.qmbox .email-body#mail_to_teacher ul.pro-desc li{position:relative;}.qmbox .email-body#mail_to_teacher ul.pro-desc li::before{content:'';width:3px;height:3px;border-radius:50%;background:red;position:absolute;left:-15px;top:11px;background:#40485B;}.qmbox .email-body#mail_to_teacher .blackboard-area{height:600px;padding:40px;background-image:url();color:#FFFFFF;}.qmbox .email-body#mail_to_teacher .blackboard-area .big-title{font-size:32px;line-height:45px;text-align:center;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc{margin-top:8px;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc p{margin:0px;text-align:center;line-height:28px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(odd){float:left;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(even){float:right;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card .title{font-size:18px;text-align:center;margin-bottom:10px;}\r\n" +
                        "</style>\r\n" +
                        "<meta>\r\n" +
                        "<div class=\"email-body\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"container\">\r\n" +
                        "<div class=\"logo\">\r\n" +
                        "<img src=\"" + LOGO + "\",height=\"100\" width=\"100\">\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"panel-header\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        PROJECT_NAME + "邮件提醒\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel-body\">\r\n" +
                        "<p>" + text + "</p>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<div class=\"footer\">\r\n" +
                        "<a href=\" " + dataWebsiteUrl + "\">@" + PROJECT_NAME_EN + "</a>\n" +
                        "<div class=\"pull-right\"></div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div></div><!-- --><style>#mailContentContainer .txt {height:auto;}</style>  </div>\r\n" +
                        " </body>\r\n" +
                        "</html>";

        sendEmail(email, content);
    }

    /**
     * 发送激活邮件
     *
     * @param user
     * @param token
     */
    public void sendActivateEmail(User user, String token) {
        String text =
                "<html>\r\n" +
                        " <head>\r\n" +
                        "  <title>" + PROJECT_NAME + "</title>\r\n" +
                        " </head>\r\n" +
                        " <body>\r\n" +
                        "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'spam', 'ZC1222-PrLAp4T0Z7Z7UUMYzqLkb8a');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">    \r\n" +
                        "  <div id=\"qm_con_body\"><div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"\">\r\n" +
                        "<style>\r\n" +
                        "  .qmbox .email-body{color:#40485B;font-size:14px;font-family:-apple-system, \"Helvetica Neue\", Helvetica, \"Nimbus Sans L\", \"Segoe UI\", Arial, \"Liberation Sans\", \"PingFang SC\", \"Microsoft YaHei\", \"Hiragino Sans GB\", \"Wenquanyi Micro Hei\", \"WenQuanYi Zen Hei\", \"ST Heiti\", SimHei, \"WenQuanYi Zen Hei Sharp\", sans-serif;background:#f8f8f8;}.qmbox .pull-right{float:right;}.qmbox a{color:#FE7300;text-decoration:underline;}.qmbox a:hover{color:#fe9d4c;}.qmbox a:active{color:#b15000;}.qmbox .logo{text-align:center;margin-bottom:20px;}.qmbox .panel{background:#fff;border:1px solid #E3E9ED;margin-bottom:10px;}.qmbox .panel-header{font-size:18px;line-height:30px;padding:10px 20px;background:#fcfcfc;border-bottom:1px solid #E3E9ED;}.qmbox .panel-body{padding:20px;}.qmbox .container{width:50%;min-width:300px;padding:20px;margin:0 auto;}.qmbox .text-center{text-align:center;}.qmbox .thumbnail{padding:4px;max-width:100%;border:1px solid #E3E9ED;}.qmbox .btn-primary{color:#fff;font-size:16px;padding:8px 14px;line-height:20px;border-radius:2px;display:inline-block;background:#FE7300;text-decoration:none;}.qmbox .btn-primary:hover,.qmbox .btn-primary:active{color:#fff;}.qmbox .footer{color:#9B9B9B;font-size:12px;margin-top:40px;}.qmbox .footer a{color:#9B9B9B;}.qmbox .footer a:hover{color:#fe9d4c;}.qmbox .footer a:active{color:#b15000;}.qmbox .email-body#mail_to_teacher{line-height:26px;color:#40485B;font-size:16px;padding:0px;}.qmbox .email-body#mail_to_teacher .container,.qmbox .email-body#mail_to_teacher .panel-body{padding:0px;}.qmbox .email-body#mail_to_teacher .container{padding-top:20px;}.qmbox .email-body#mail_to_teacher .textarea{padding:32px;}.qmbox .email-body#mail_to_teacher .say-hi{font-weight:500;}.qmbox .email-body#mail_to_teacher .paragraph{margin-top:24px;}.qmbox .email-body#mail_to_teacher .paragraph .pro-name{color:#000000;}.qmbox .email-body#mail_to_teacher .paragraph.link{margin-top:32px;text-align:center;}.qmbox .email-body#mail_to_teacher .paragraph.link .button{background:#4A90E2;border-radius:2px;color:#FFFFFF;text-decoration:none;padding:11px 17px;line-height:14px;display:inline-block;}.qmbox .email-body#mail_to_teacher ul.pro-desc{list-style-type:none;margin:0px;padding:0px;padding-left:16px;}.qmbox .email-body#mail_to_teacher ul.pro-desc li{position:relative;}.qmbox .email-body#mail_to_teacher ul.pro-desc li::before{content:'';width:3px;height:3px;border-radius:50%;background:red;position:absolute;left:-15px;top:11px;background:#40485B;}.qmbox .email-body#mail_to_teacher .blackboard-area{height:600px;padding:40px;background-image:url();color:#FFFFFF;}.qmbox .email-body#mail_to_teacher .blackboard-area .big-title{font-size:32px;line-height:45px;text-align:center;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc{margin-top:8px;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc p{margin:0px;text-align:center;line-height:28px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(odd){float:left;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(even){float:right;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card .title{font-size:18px;text-align:center;margin-bottom:10px;}\r\n" +
                        "</style>\r\n" +
                        "<meta>\r\n" +
                        "<div class=\"email-body\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"container\">\r\n" +
                        "<div class=\"logo\">\r\n" +
                        "<img src=\"" + LOGO + "\",height=\"100\" width=\"100\">\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"panel-header\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        PROJECT_NAME + "账号激活\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel-body\">\r\n" +
                        "<p>您好 <a href=\"mailto:" + user.getEmail() + "\" rel=\"noopener\" target=\"_blank\">" + user.getNickName() + "<wbr></a>！</p>\r\n" +
                        "<p>欢迎您注册" + PROJECT_NAME + "账号，请点击下方链接进行账号激活</p>\r\n" +
                        "<p>地址：" + "<a href=\"" + dataWebUrl + "/login/activeUser/" + token + "\">点击这里</a>" + "</p>\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<div class=\"footer\">\r\n" +
                        "<a href=\" " + dataWebsiteUrl + "\">@" + PROJECT_NAME_EN + "</a>\n" +
                        "<div class=\"pull-right\"></div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div></div><!-- --><style>#mailContentContainer .txt {height:auto;}</style>  </div>\r\n" +
                        " </body>\r\n" +
                        "</html>";
        sendEmail(user.getEmail(), text);
    }

    /**
     * 发送注册邮件
     *
     * @param user
     * @param token
     */
    public void sendRegisterEmail(User user, String token) {
        String text =
                "<html>\r\n" +
                        " <head>\r\n" +
                        "  <title>" + PROJECT_NAME + "</title>\r\n" +
                        " </head>\r\n" +
                        " <body>\r\n" +
                        "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'spam', 'ZC1222-PrLAp4T0Z7Z7UUMYzqLkb8a');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">    \r\n" +
                        "  <div id=\"qm_con_body\"><div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"\">\r\n" +
                        "<style>\r\n" +
                        "  .qmbox .email-body{color:#40485B;font-size:14px;font-family:-apple-system, \"Helvetica Neue\", Helvetica, \"Nimbus Sans L\", \"Segoe UI\", Arial, \"Liberation Sans\", \"PingFang SC\", \"Microsoft YaHei\", \"Hiragino Sans GB\", \"Wenquanyi Micro Hei\", \"WenQuanYi Zen Hei\", \"ST Heiti\", SimHei, \"WenQuanYi Zen Hei Sharp\", sans-serif;background:#f8f8f8;}.qmbox .pull-right{float:right;}.qmbox a{color:#FE7300;text-decoration:underline;}.qmbox a:hover{color:#fe9d4c;}.qmbox a:active{color:#b15000;}.qmbox .logo{text-align:center;margin-bottom:20px;}.qmbox .panel{background:#fff;border:1px solid #E3E9ED;margin-bottom:10px;}.qmbox .panel-header{font-size:18px;line-height:30px;padding:10px 20px;background:#fcfcfc;border-bottom:1px solid #E3E9ED;}.qmbox .panel-body{padding:20px;}.qmbox .container{width:50%;min-width:600px;padding:20px;margin:0 auto;}.qmbox .text-center{text-align:center;}.qmbox .thumbnail{padding:4px;max-width:100%;border:1px solid #E3E9ED;}.qmbox .btn-primary{color:#fff;font-size:16px;padding:8px 14px;line-height:20px;border-radius:2px;display:inline-block;background:#FE7300;text-decoration:none;}.qmbox .btn-primary:hover,.qmbox .btn-primary:active{color:#fff;}.qmbox .footer{color:#9B9B9B;font-size:12px;margin-top:40px;}.qmbox .footer a{color:#9B9B9B;}.qmbox .footer a:hover{color:#fe9d4c;}.qmbox .footer a:active{color:#b15000;}.qmbox .email-body#mail_to_teacher{line-height:26px;color:#40485B;font-size:16px;padding:0px;}.qmbox .email-body#mail_to_teacher .container,.qmbox .email-body#mail_to_teacher .panel-body{padding:0px;}.qmbox .email-body#mail_to_teacher .container{padding-top:20px;}.qmbox .email-body#mail_to_teacher .textarea{padding:32px;}.qmbox .email-body#mail_to_teacher .say-hi{font-weight:500;}.qmbox .email-body#mail_to_teacher .paragraph{margin-top:24px;}.qmbox .email-body#mail_to_teacher .paragraph .pro-name{color:#000000;}.qmbox .email-body#mail_to_teacher .paragraph.link{margin-top:32px;text-align:center;}.qmbox .email-body#mail_to_teacher .paragraph.link .button{background:#4A90E2;border-radius:2px;color:#FFFFFF;text-decoration:none;padding:11px 17px;line-height:14px;display:inline-block;}.qmbox .email-body#mail_to_teacher ul.pro-desc{list-style-type:none;margin:0px;padding:0px;padding-left:16px;}.qmbox .email-body#mail_to_teacher ul.pro-desc li{position:relative;}.qmbox .email-body#mail_to_teacher ul.pro-desc li::before{content:'';width:3px;height:3px;border-radius:50%;background:red;position:absolute;left:-15px;top:11px;background:#40485B;}.qmbox .email-body#mail_to_teacher .blackboard-area{height:600px;padding:40px;background-image:url();color:#FFFFFF;}.qmbox .email-body#mail_to_teacher .blackboard-area .big-title{font-size:32px;line-height:45px;text-align:center;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc{margin-top:8px;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc p{margin:0px;text-align:center;line-height:28px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(odd){float:left;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(even){float:right;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card .title{font-size:18px;text-align:center;margin-bottom:10px;}\r\n" +
                        "</style>\r\n" +
                        "<meta>\r\n" +
                        "<div class=\"email-body\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"container\">\r\n" +
                        "<div class=\"logo\">\r\n" +
                        "<img src=\"" + LOGO + "\",height=\"100\" width=\"100\">\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"panel-header\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        PROJECT_NAME + "邮箱绑定\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel-body\">\r\n" +
                        "<p>您好 <a href=\"mailto:" + user.getEmail() + "\" rel=\"noopener\" target=\"_blank\">" + user.getNickName() + "<wbr></a>！</p>\r\n" +
                        "<p>欢迎您给" + PROJECT_NAME + "账号绑定邮箱，请点击下方链接进行绑定</p>\r\n" +
                        "<p>地址：" + "<a href=\"" + dataWebUrl + "/oauth/bindUserEmail/" + token + "/" + user.getValidCode() + "\">点击这里</a>" + "</p>\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<div class=\"footer\">\r\n" +
                        "<a href=\" " + dataWebsiteUrl + "\">@" + PROJECT_NAME_EN + "</a>\n" +
                        "<div class=\"pull-right\"></div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div></div><!-- --><style>#mailContentContainer .txt {height:auto;}</style>  </div>\r\n" +
                        " </body>\r\n" +
                        "</html>";
        sendEmail(user.getEmail(), text);
    }


}
