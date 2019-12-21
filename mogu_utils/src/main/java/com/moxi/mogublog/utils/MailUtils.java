package com.moxi.mogublog.utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {
    static Properties properties;
    static Message msg;
    static Transport transport;

    //初始化Mail信息  
    public MailUtils() {
        properties = new Properties();
        properties.setProperty("mail.debug", "true");//调试模式发送  
        properties.setProperty("mail.smtp.auth", "true");//身份验证设置  
        properties.setProperty("mail.host", "smtp.163.com");//发件人邮箱主机名  
        properties.setProperty("mail.transport.protocol", "smtp");//发件协议            
        Session session = Session.getInstance(properties);
        msg = new MimeMessage(session);

        try {
            msg.setSubject("蘑菇博客网站验证邮件");
            msg.setFrom(new InternetAddress("mogublog@163.com"));//设置发件人  

            transport = session.getTransport();
            transport.connect("mogublog@163.com", "w1313375");//设置发件人在该邮箱主机上的用户名密码  
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到邮箱地址邮箱内容发送。
     *
     * @param 邮箱地址
     * @param 邮箱内容
     * @throws AddressException
     * @throws MessagingException
     */

    public void sendMail(String address, String text) throws AddressException, MessagingException {
        msg.setText(text);
        transport.sendMessage(msg, new Address[]{new InternetAddress(address)});
        transport.close();
    }


}  