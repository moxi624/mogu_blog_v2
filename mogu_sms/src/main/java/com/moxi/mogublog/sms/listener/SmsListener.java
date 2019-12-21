package com.moxi.mogublog.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.moxi.mogublog.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @RabbitListener(queues = "mogu.sms")
    public void sendSms(Map<String, String> map) {

        try {
            SendSmsResponse response = smsUtil.sendSms(
                    map.get("mobile"),
                    map.get("template_code"),
                    map.get("sign_name"),
                    map.get("param"));
            System.out.println("code:" + response.getCode());
            System.out.println("message:" + response.getMessage());

        } catch (ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
