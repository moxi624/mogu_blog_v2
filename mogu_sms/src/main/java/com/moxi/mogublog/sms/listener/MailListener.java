package com.moxi.mogublog.sms.listener;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moxi.mogublog.sms.util.SendMailUtils;


@Component
public class MailListener {
	
	@Autowired
	private SendMailUtils sendMailUtils;
	
	
	@RabbitListener(queues = "mogu.email")
	public void sendMail(Map<String,String> map){

			try {
				sendMailUtils.sendEmail(
						map.get("receiver"),
						map.get("text")
						);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		
	}
}
