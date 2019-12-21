package com.moxi.mogublog.utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件Util
 *
 * @author xzx19950624@qq.com
 * @date 2018年10月20日下午3:18:25
 */
public class SendMailUtils {

    public static final String HOST = "smtp.163.com";
    public static final String PROTOCOL = "smtp";
    public static final int PORT = 25;
    public static final String SENDER = "mogublog@163.com";//  
    public static final String SENDERPWD = "a1313375";

    /**
     * 获取用于发送邮件的Session
     *
     * @return
     */
    public static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址  
        props.put("mail.store.protocol", PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口  
        props.put("mail.smtp.auth", true);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER, SENDERPWD);
            }
        };
        Session session = Session.getDefaultInstance(props, authenticator);
        return session;
    }

    /**
     * 发送邮件
     *
     * @param receiver
     * @param content
     */
    public static void send(String receiver, String content) {
        Session session = getSession();
        try {
            System.out.println("-------开始发送-------");
            Message msg = new MimeMessage(session);
            //设置message属性  
            msg.setFrom(new InternetAddress(SENDER));
            InternetAddress[] addrs = {new InternetAddress(receiver)};
            msg.setRecipients(Message.RecipientType.TO, addrs);
            msg.setSubject("蘑菇博客-帐号激活");
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");
            //开始发送  
            Transport.send(msg);
            System.out.println("-------发送完成-------");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}