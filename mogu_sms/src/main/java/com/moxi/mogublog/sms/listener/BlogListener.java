package com.moxi.mogublog.sms.listener;

import com.moxi.mogublog.sms.global.SysConf;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.JsonUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

// TODO 在这里同时需要对Redis和Solr进行操作，同时利用MQ来保证数据一致性

    @RabbitListener(queues = "mogu.blog")
    public void updateRedis(Map<String, String> map) {

        if (map != null) {
            String level = map.get(SysConf.LEVEL);
            //从Redis清空对应的数据
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.ONE, "");
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.TWO, "");
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.THREE, "");
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.FOUR, "");
            stringRedisTemplate.opsForValue().set(SysConf.HOT_BLOG, "");
            stringRedisTemplate.opsForValue().set(SysConf.NEW_BLOG, "");

            String createTime = map.get(SysConf.CREATE_TIME);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(createTime))));
            String [] list = sd.split("-");

            System.out.println(createTime);
            String year = list[0];
            String month = list[1];
            String key = year + "年" + month + "月";
            System.out.println(key);
            stringRedisTemplate.opsForValue().set("BLOG_SORT_BY_MONTH:" + key, "");


            String jsonResult = stringRedisTemplate.opsForValue().get("MONTH_SET");
            ArrayList<String> monthSet = (ArrayList<String>) JsonUtils.jsonArrayToArrayList(jsonResult);
            Boolean haveMonth = false;
            if(monthSet != null) {
                for (String item : monthSet) {
                    if (item.equals(key)) {
                        haveMonth = true;
                        break;
                    }
                }
                if(!haveMonth) {
                    monthSet.add(key);
                    stringRedisTemplate.opsForValue().set("MONTH_SET", JsonUtils.objectToJson(monthSet));
                }
            }
        }
    }
}
