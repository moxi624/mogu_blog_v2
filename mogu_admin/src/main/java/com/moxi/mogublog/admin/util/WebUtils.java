package com.moxi.mogublog.admin.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.xo.entity.SystemConfig;
import com.moxi.mogublog.xo.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web有关的工具类
 *
 * @author xzx19950624@qq.com
 * @date 2017年9月24日23:27:03
 */
@Slf4j
@Component
public class WebUtils {

    @Autowired
    SystemConfigService systemConfigService;

    /**
     * 格式化数据获取图片列表
     *
     * @param result
     * @return
     */
    public List<String> getPicture(String result) {

        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        SystemConfig systemConfig = systemConfigService.getOne(queryWrapper);
        String picturePriority = systemConfig.getPicturePriority();
        String localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
        String qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();

        List<String> picUrls = new ArrayList<>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(picMap.get("code"))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get("data");
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    if ("1".equals(picturePriority)) {
                        picUrls.add(qiNiuPictureBaseUrl + (String) picData.get(i).get("qiNiuUrl"));
                    } else {
                        picUrls.add(localPictureBaseUrl + (String) picData.get(i).get("url"));
                    }
                }
            }
        }
        return picUrls;
    }

    /**
     * 获取图片，返回Map
     *
     * @author xzx19950624@qq.com
     * @date 2018年10月21日下午12:55:18
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPictureMap(String result) {

        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        SystemConfig systemConfig = systemConfigService.getOne(queryWrapper);
        String picturePriority = systemConfig.getPicturePriority();
        String localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
        String qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(picMap.get("code"))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get("data");
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    if (StringUtils.isEmpty(picData.get(i).get("url")) || StringUtils.isEmpty(picData.get(i).get("uid"))) {
                        continue;
                    }
                    // 图片优先显示 七牛云 or 本地
                    if ("1".equals(picturePriority)) {
                        map.put("url", qiNiuPictureBaseUrl + (String) picData.get(i).get("qiNiuUrl"));
                    } else {
                        map.put("url", localPictureBaseUrl + (String) picData.get(i).get("url"));
                    }

                    map.put("uid", (String) picData.get(i).get("uid"));
                    resultList.add(map);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取结果集的内容
     *
     * @param result
     * @return
     */
    public <T> T getData(String result, Class<T> beanType) {
        if (com.moxi.mogublog.utils.StringUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(dataMap.get("code"))) {

            Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
            T t = JsonUtils.mapToPojo(data, beanType);
            return t;
        }
        return null;
    }

    /**
     * 获取结果集的内容，返回的是 List<POJO>
     *
     * @param result
     * @return
     */
    public <T> List<T> getList(String result, Class<T> beanType) {
        if (com.moxi.mogublog.utils.StringUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(dataMap.get("code"))) {

            Map<String, Object> data = (Map<String, Object>) dataMap.get("data");

            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("records");
            List<T> resultList = new ArrayList<>();
            list.forEach(item -> {
                resultList.add(JsonUtils.mapToPojo(item, beanType));
            });
            return resultList;
        }
        return null;
    }
}
