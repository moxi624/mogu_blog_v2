package com.moxi.mogublog.picture.util;

import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.commons.feign.WebFeignClient;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Feign操作工具类
 *
 * @author: 陌溪
 * @create: 2020-02-29-15:39
 */
@Slf4j
@Component
public class FeignUtil {

    @Autowired
    AdminFeignClient adminFeignClient;

    @Autowired
    WebFeignClient webFeignClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 通过Token获取七牛云配置
     * @param token
     * @return
     */
    public Map<String, String> getQiNiuConfig(String token) {

        // 从Redis中获取的SystemConf 或者 通过feign获取的
        Map<String, String> resultMap = new HashMap<>();

        //从Redis中获取内容
        String jsonResult = stringRedisTemplate.opsForValue().get(SysConf.ADMIN_TOKEN + SysConf.REDIS_SEGMENTATION + token);

        // 判断Redis中是否有数据
        if(StringUtils.isNotEmpty(jsonResult)) {

            resultMap = (Map<String, String>) JsonUtils.jsonToMap(jsonResult, String.class);

        } else {

            // 进行七牛云校验
            String resultStr = adminFeignClient.getSystemConfig();

            Map<String, Object> resultTempMap = JsonUtils.jsonToMap(resultStr);

            if(resultTempMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultTempMap.get(SysConf.CODE).toString())) {
                resultMap = (Map<String, String>) resultTempMap.get(SysConf.DATA);
                //将从token存储到redis中，设置30分钟后过期
                stringRedisTemplate.opsForValue().set(SysConf.ADMIN_TOKEN + SysConf.REDIS_SEGMENTATION + token, JsonUtils.objectToJson(resultMap), 30, TimeUnit.MINUTES);
            }
        }
        return resultMap;
    }

    /**
     * 通过Web端的token获取七牛云配置文件
     * @param token
     * @return
     */
    public Map<String, String> getQiNiuConfigByWebToken(String token) {

        // 从Redis中获取的SystemConf 或者 通过feign获取的
        Map<String, String> resultMap = new HashMap<>();

        //从Redis中获取内容
        String jsonResult = stringRedisTemplate.opsForValue().get(SysConf.WEB_TOKEN + SysConf.REDIS_SEGMENTATION + token);

        // 判断Redis中是否有数据
        if(StringUtils.isNotEmpty(jsonResult)) {

            resultMap = (Map<String, String>) JsonUtils.jsonToMap(jsonResult, String.class);

        } else {

            // 进行七牛云校验
            String resultStr = webFeignClient.getSystemConfig(token);

            Map<String, Object> resultTempMap = JsonUtils.jsonToMap(resultStr);

            if(resultTempMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultTempMap.get(SysConf.CODE).toString())) {
                resultMap = (Map<String, String>) resultTempMap.get(SysConf.DATA);
                //将从token存储到redis中，设置30分钟后过期
                stringRedisTemplate.opsForValue().set(SysConf.WEB_TOKEN + SysConf.REDIS_SEGMENTATION + token, JsonUtils.objectToJson(resultMap), 30, TimeUnit.MINUTES);
            }
        }
        return resultMap;
    }

}
