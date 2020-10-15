package com.moxi.mogublog.sms.listener;

import com.moxi.mogublog.sms.util.SendMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * 邮件监听器【用于发送邮件】
 *
 * @author 陌溪
 * @date 2020年10月6日10:09:30
 */
@Slf4j
@Component
public class MailListener {

    @Autowired
    private SendMailUtils sendMailUtils;


    @RabbitListener(queues = "mogu.email")
    public void sendMail(Map<String, String> map) {
        if (map != null) {
            sendMailUtils.sendEmail(
                    map.get("receiver"),
                    map.get("text")
            );
        }
    }
}
