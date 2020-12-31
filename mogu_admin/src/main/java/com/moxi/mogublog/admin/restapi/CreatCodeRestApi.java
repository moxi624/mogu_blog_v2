package com.moxi.mogublog.admin.restapi;

import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.CheckUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mougblog.base.global.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码服务RestApi
 *
 * @author limbo
 * @since 2018-10-22
 */
@RestController
@RefreshScope
@RequestMapping("/creatCode")
@Api(value = "生成验证码相关接口", tags = {"生成验证码相关接口"})
@Slf4j
public class CreatCodeRestApi {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value(value = "${templateCode}")
    private String templateCode;
    @Value(value = "${signName}")
    private String signName;
    @Value(value = "${moguBlog.email}")
    private String moguBlogEmail;

    @OperationLogger(value = "生成验证码")
    @ApiOperation(value = "生成验证码", notes = "生成验证码")
    @PostMapping("/validCode")
    public String validCode(HttpServletRequest request,
                            @ApiParam(name = "info", value = "注册手机号码或邮箱", required = true) @RequestBody(required = true) String info) {

        if (!CheckUtils.checkMobileNumber(info)) {
            if (!CheckUtils.checkEmail(info)) {
                return ResultUtil.result(SysConf.ERROR, "输入的邮箱或手机号格式有误");
            }
        }
        //生成6位随机数
        final String code = (long) (Math.random() * 1000000) + "";
        log.info("验证码:" + code);

        String text =
                "<html>\r\n" +
                        " <head>\r\n" +
                        "  <title> mogublog </title>\r\n" +
                        " </head>\r\n" +
                        " <body>\r\n" +
                        "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'spam', 'ZC1222-PrLAp4T0Z7Z7UUMYzqLkb8a');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">    \r\n" +
                        "  <div id=\"qm_con_body\"><div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"\">\r\n" +
                        "<style>\r\n" +
                        "  .qmbox .email-body{color:#40485B;font-size:14px;font-family:-apple-system, \"Helvetica Neue\", Helvetica, \"Nimbus Sans L\", \"Segoe UI\", Arial, \"Liberation Sans\", \"PingFang SC\", \"Microsoft YaHei\", \"Hiragino Sans GB\", \"Wenquanyi Micro Hei\", \"WenQuanYi Zen Hei\", \"ST Heiti\", SimHei, \"WenQuanYi Zen Hei Sharp\", sans-serif;background:#f8f8f8;}.qmbox .pull-right{float:right;}.qmbox a{color:#FE7300;text-decoration:underline;}.qmbox a:hover{color:#fe9d4c;}.qmbox a:active{color:#b15000;}.qmbox .logo{text-align:center;margin-bottom:20px;}.qmbox .panel{background:#fff;border:1px solid #E3E9ED;margin-bottom:10px;}.qmbox .panel-header{font-size:18px;line-height:30px;padding:10px 20px;background:#fcfcfc;border-bottom:1px solid #E3E9ED;}.qmbox .panel-body{padding:20px;}.qmbox .container{width:100%;max-width:600px;padding:20px;margin:0 auto;}.qmbox .text-center{text-align:center;}.qmbox .thumbnail{padding:4px;max-width:100%;border:1px solid #E3E9ED;}.qmbox .btn-primary{color:#fff;font-size:16px;padding:8px 14px;line-height:20px;border-radius:2px;display:inline-block;background:#FE7300;text-decoration:none;}.qmbox .btn-primary:hover,.qmbox .btn-primary:active{color:#fff;}.qmbox .footer{color:#9B9B9B;font-size:12px;margin-top:40px;}.qmbox .footer a{color:#9B9B9B;}.qmbox .footer a:hover{color:#fe9d4c;}.qmbox .footer a:active{color:#b15000;}.qmbox .email-body#mail_to_teacher{line-height:26px;color:#40485B;font-size:16px;padding:0px;}.qmbox .email-body#mail_to_teacher .container,.qmbox .email-body#mail_to_teacher .panel-body{padding:0px;}.qmbox .email-body#mail_to_teacher .container{padding-top:20px;}.qmbox .email-body#mail_to_teacher .textarea{padding:32px;}.qmbox .email-body#mail_to_teacher .say-hi{font-weight:500;}.qmbox .email-body#mail_to_teacher .paragraph{margin-top:24px;}.qmbox .email-body#mail_to_teacher .paragraph .pro-name{color:#000000;}.qmbox .email-body#mail_to_teacher .paragraph.link{margin-top:32px;text-align:center;}.qmbox .email-body#mail_to_teacher .paragraph.link .button{background:#4A90E2;border-radius:2px;color:#FFFFFF;text-decoration:none;padding:11px 17px;line-height:14px;display:inline-block;}.qmbox .email-body#mail_to_teacher ul.pro-desc{list-style-type:none;margin:0px;padding:0px;padding-left:16px;}.qmbox .email-body#mail_to_teacher ul.pro-desc li{position:relative;}.qmbox .email-body#mail_to_teacher ul.pro-desc li::before{content:'';width:3px;height:3px;border-radius:50%;background:red;position:absolute;left:-15px;top:11px;background:#40485B;}.qmbox .email-body#mail_to_teacher .blackboard-area{height:600px;padding:40px;background-image:url();color:#FFFFFF;}.qmbox .email-body#mail_to_teacher .blackboard-area .big-title{font-size:32px;line-height:45px;text-align:center;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc{margin-top:8px;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc p{margin:0px;text-align:center;line-height:28px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(odd){float:left;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(even){float:right;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card .title{font-size:18px;text-align:center;margin-bottom:10px;}\r\n" +
                        "</style>\r\n" +
                        "<meta>\r\n" +
                        "<div class=\"email-body\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"container\">\r\n" +
                        "<div class=\"logo\">\r\n" +
                        "<img src=\"http://picture.moguit.cn/blog/admin/jpg/2018/10/21/logo.jpg\",height=\"100\" width=\"100\">\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"panel-header\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "注册验证\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel-body\">\r\n" +
                        "<p>您好 <a href=\"mailto:" + moguBlogEmail + "\" rel=\"noopener\" target=\"_blank\">" + info + "<wbr></a>！</p>\r\n" +
                        "<p>欢迎注册蘑菇博客，请将验证码填写到注册页面。</p>\r\n" +
                        "<p>验证码：" + code + "</p>\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<div class=\"footer\">\r\n" +
                        "@muguit.cn\r\n" +
                        "<div class=\"pull-right\"></div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div></div><!-- --><style>#mailContentContainer .txt {height:auto;}</style>  </div>\r\n" +
                        " </body>\r\n" +
                        "</html>";


        Map<String, String> map = new HashMap<>(Constants.NUM_FOUR);
        if (CheckUtils.checkEmail(info)) {
            map.put("receiver", info);
            map.put("text", text);
            //发送到RabbitMq
            rabbitTemplate.convertAndSend("exchange.direct", "mogu.email", map);
        }
        if (CheckUtils.checkMobileNumber(info)) {
            //code是我们申请模板时写的参数
            map.put("param", code);
            //手机号
            map.put("mobile", info);
            //短信模板编号
            map.put("template_code", templateCode);
            //短信签名
            map.put("sign_name", signName);
            //发送到RabbitMq
            rabbitTemplate.convertAndSend("exchange.direct", "mogu.sms", map);
        }

        //存入缓存
        stringRedisTemplate.opsForValue().set("SMS:" + info, code);

        return ResultUtil.result(SysConf.SUCCESS, "生成验证码成功");
    }
}
