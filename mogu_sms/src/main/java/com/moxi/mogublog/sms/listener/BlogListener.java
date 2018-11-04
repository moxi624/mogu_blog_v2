package com.moxi.mogublog.sms.listener;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.moxi.mogublog.sms.global.SysConf;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.StringUtils;

/**
 * 博客监听器(用于更新Redis和索引)
 * @author xzx19950624@qq.com
 * @date 2018年11月3日下午12:53:23
 */
@Component
public class BlogListener {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	
	@RabbitListener(queues = "mogu.blog")
	public void updateRedis(Map<String,String> map){
		
		if( map != null) {
			String level = map.get(SysConf.LEVEL);
			//从Redis中获取内容
			stringRedisTemplate.opsForValue().set("BOLG_LEVEL:1", "");
			stringRedisTemplate.opsForValue().set("BOLG_LEVEL:2", "");
			stringRedisTemplate.opsForValue().set("BOLG_LEVEL:3", "");
			stringRedisTemplate.opsForValue().set("BOLG_LEVEL:4", "");

//			String result = stringRedisTemplate.opsForValue().get("BOLG_LEVEL:" + level);
//			if(StringUtils.isNotEmpty(result)) {
//				List list = JsonUtils.jsonArrayToArrayList(result);
//			}
			
				
		}
	}
}
