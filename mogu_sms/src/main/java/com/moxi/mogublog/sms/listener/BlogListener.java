package com.moxi.mogublog.sms.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mougblog.base.global.BaseSQLConf;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.moxi.mogublog.sms.global.SysConf;

/**
 * 博客监听器(用于更新Redis和索引)
 *
 * @author xzx19950624@qq.com
 * @date 2018年11月3日下午12:53:23
 */
@Component
public class BlogListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RabbitListener(queues = "mogu.blog")
    public void updateRedis(Map<String, String> map) {

        if (map != null) {
            String level = map.get(SysConf.LEVEL);
            //从Redis中获取内容
            stringRedisTemplate.opsForValue().set("BOLG_LEVEL:1", "");
            stringRedisTemplate.opsForValue().set("BOLG_LEVEL:2", "");
            stringRedisTemplate.opsForValue().set("BOLG_LEVEL:3", "");
            stringRedisTemplate.opsForValue().set("BOLG_LEVEL:4", "");

            String createTime = map.get(SysConf.CREATE_TIME);
            System.out.println(createTime);
            String year = createTime.substring(0, 4);
            String month = createTime.substring(5, 7);
            String key = year + "年" + month + "月";
            System.out.println(key);
            stringRedisTemplate.opsForValue().set("BLOG_SORT_BY_MONTH" + key, "");
            stringRedisTemplate.opsForValue().set("MONTH_SET", "");

//			String result = stringRedisTemplate.opsForValue().get("BOLG_LEVEL:" + level);
//			if(StringUtils.isNotEmpty(result)) {
//				List list = JsonUtils.jsonArrayToArrayList(result);
//			}


        }
    }
}
