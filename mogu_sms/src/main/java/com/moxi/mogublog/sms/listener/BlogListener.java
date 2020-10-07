package com.moxi.mogublog.sms.listener;

import com.moxi.mogublog.commons.feign.SearchFeignClient;
import com.moxi.mogublog.sms.global.RedisConf;
import com.moxi.mogublog.sms.global.SysConf;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mougblog.base.global.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * 博客监听器【用于更新Redis和索引】
 *
 * @author 陌溪
 * @date 2018年11月3日下午12:53:23
 */
@Component
@Slf4j
public class BlogListener {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SearchFeignClient searchFeignClient;

    // TODO 在这里同时需要对Redis和Solr进行操作，同时利用MQ来保证数据一致性
    @RabbitListener(queues = "mogu.blog")
    public void updateRedis(Map<String, String> map) {

        if (map != null) {
            String comment = map.get(SysConf.COMMAND);
            String uid = map.get(SysConf.BLOG_UID);

            //从Redis清空对应的数据
            redisUtil.delete(RedisConf.BLOG_LEVEL + Constants.SYMBOL_COLON + Constants.NUM_ONE);
            redisUtil.delete(RedisConf.BLOG_LEVEL + Constants.SYMBOL_COLON + Constants.NUM_TWO);
            redisUtil.delete(RedisConf.BLOG_LEVEL + Constants.SYMBOL_COLON + Constants.NUM_THREE);
            redisUtil.delete(RedisConf.BLOG_LEVEL + Constants.SYMBOL_COLON + Constants.NUM_FOUR);
            redisUtil.delete(RedisConf.HOT_BLOG);
            redisUtil.delete(RedisConf.NEW_BLOG);
            redisUtil.delete(RedisConf.DASHBOARD + Constants.SYMBOL_COLON + RedisConf.BLOG_CONTRIBUTE_COUNT);
            redisUtil.delete(RedisConf.DASHBOARD + Constants.SYMBOL_COLON + RedisConf.BLOG_COUNT_BY_SORT);
            redisUtil.delete(RedisConf.DASHBOARD + Constants.SYMBOL_COLON + RedisConf.BLOG_COUNT_BY_TAG);

            switch (comment) {
                case SysConf.DELETE_BATCH: {

                    log.info("mogu-sms处理批量删除博客");
                    redisUtil.set(RedisConf.BLOG_SORT_BY_MONTH + Constants.SYMBOL_COLON, "");
                    redisUtil.set(RedisConf.MONTH_SET, "");

                    // 删除ElasticSearch博客索引
//                    searchFeignClient.deleteElasticSearchByUids(uid);

                    // 删除Solr博客索引
//                    searchFeignClient.deleteSolrIndexByUids(uid);
                }
                break;
                case SysConf.EDIT_BATCH: {

                    log.info("mogu-sms处理批量编辑博客");
                    redisUtil.set(RedisConf.BLOG_SORT_BY_MONTH + Constants.SYMBOL_COLON, "");
                    redisUtil.set(RedisConf.MONTH_SET, "");

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
                    log.info("mogu-sms处理删除博客: uid:" + uid);
                    updateSearch(map);

                    // 删除ES索引
//                    searchFeignClient.deleteElasticSearchByUid(uid);

                    // 删除Solr索引
//                    searchFeignClient.deleteSolrIndexByUid(uid);
                }
                break;
                default: {
                    log.info("mogu-sms处理博客");
                }
            }
        }
    }


    private void updateSearch(Map<String, String> map) {
        try {
            String level = map.get(SysConf.LEVEL);
            String createTime = map.get(SysConf.CREATE_TIME);
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM);
            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(createTime))));
            String[] list = sd.split(Constants.SYMBOL_HYPHEN);
            String year = list[0];
            String month = list[1];
            String key = year + "年" + month + "月";
            redisUtil.delete(RedisConf.BLOG_SORT_BY_MONTH + Constants.SYMBOL_COLON + key);
            String jsonResult = redisUtil.get(RedisConf.MONTH_SET);
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
                    redisUtil.set(RedisConf.MONTH_SET, JsonUtils.objectToJson(monthSet));
                }
            }

        } catch (Exception e) {
            log.error("更新Redis失败");
        }
    }
}
