package com.moxi.mogublog.sms.listener;

import com.moxi.mogublog.sms.feign.SearchFeignClient;
import com.moxi.mogublog.sms.global.SysConf;
import com.moxi.mogublog.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * 博客监听器(用于更新Redis和索引)
 *
 * @author xzx19950624@qq.com
 * @date 2018年11月3日下午12:53:23
 */
@Component
@Slf4j
public class BlogListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SearchFeignClient searchFeignClient;

// TODO 在这里同时需要对Redis和Solr进行操作，同时利用MQ来保证数据一致性

    @RabbitListener(queues = "mogu.blog")
    public void updateRedis(Map<String, String> map) {

        if (map != null) {

            String comment = map.get(SysConf.COMMAND);

            String uid = map.get("blogUid");

            //从Redis清空对应的数据
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.ONE, "");
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.TWO, "");
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.THREE, "");
            stringRedisTemplate.opsForValue().set(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.FOUR, "");
            stringRedisTemplate.opsForValue().set(SysConf.HOT_BLOG, "");
            stringRedisTemplate.opsForValue().set(SysConf.NEW_BLOG, "");
            switch (comment) {
                case SysConf.DELETE_BATCH: {

                    log.info("mogu-sms处理批量删除博客");
                    stringRedisTemplate.opsForValue().set("BLOG_SORT_BY_MONTH:", "");
                    stringRedisTemplate.opsForValue().set("MONTH_SET", "");

                    // 删除ElasticSearch博客索引
//                    searchFeignClient.deleteElasticSearchByUids(uid);

                    // 删除Solr博客索引
//                    searchFeignClient.deleteSolrIndexByUids(uid);

                }
                break;
                case SysConf.EDIT_BATCH: {

                    log.info("mogu-sms处理批量编辑博客");
                    stringRedisTemplate.opsForValue().set("BLOG_SORT_BY_MONTH:", "");
                    stringRedisTemplate.opsForValue().set("MONTH_SET", "");

                }
                break;
                case SysConf.ADD: {
                    log.info("mogu-sms处理增加博客");
                    updateSearch(map);

                    // 增加ES索引
//                    searchFeignClient.addElasticSearchIndexByUid(uid);

                    // 增加solr索引
//                    searchFeignClient.addSolrIndexByUid(uid);
                }
                break;

                case SysConf.EDIT: {
                    log.info("mogu-sms处理编辑博客");
                    updateSearch(map);

                    // 更新ES索引
//                    searchFeignClient.addElasticSearchIndexByUid(uid);

                    // 更新Solr索引
//                    searchFeignClient.updateSolrIndexByUid(uid);
                }
                break;

                case SysConf.DELETE: {
                    log.info("mogu-sms处理删除博客： uid=" + uid);
                    updateSearch(map);

                    // 删除ES索引
//                    searchFeignClient.deleteElasticSearchByUid(uid);

                    // 删除Solr索引
//                    searchFeignClient.deleteSolrIndexByUid(uid);
                }
                break;
            }
        }
    }


    private void updateSearch(Map<String, String> map) {
        try {

            String level = map.get(SysConf.LEVEL);
            String createTime = map.get(SysConf.CREATE_TIME);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            Date myString = DateFormat.getDateTimeInstance().parse(createTime);
//            String sd = sdf.format(myString);

            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(createTime))));
            String[] list = sd.split("-");
            System.out.println(createTime);
            String year = list[0];
            String month = list[1];
            String key = year + "年" + month + "月";
            System.out.println(key);
            stringRedisTemplate.opsForValue().set("BLOG_SORT_BY_MONTH:" + key, "");

            String jsonResult = stringRedisTemplate.opsForValue().get("MONTH_SET");
            ArrayList<String> monthSet = (ArrayList<String>) JsonUtils.jsonArrayToArrayList(jsonResult);
            Boolean haveMonth = false;
            if (monthSet != null) {
                for (String item : monthSet) {
                    if (item.equals(key)) {
                        haveMonth = true;
                        break;
                    }
                }
                if (!haveMonth) {
                    monthSet.add(key);
                    stringRedisTemplate.opsForValue().set("MONTH_SET", JsonUtils.objectToJson(monthSet));
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
