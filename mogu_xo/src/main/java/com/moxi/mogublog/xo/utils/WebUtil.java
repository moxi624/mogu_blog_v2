package com.moxi.mogublog.xo.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.service.SystemConfigService;
import com.moxi.mougblog.base.enums.EFilePriority;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
import com.moxi.mougblog.base.global.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * web有关的工具类
 *
 * @author 陌溪
 * @date 2020年4月6日23:42:41
 */
@Slf4j
@Component
public class WebUtil {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 格式化数据获取图片列表
     *
     * @param result
     * @return
     */
    public List<String> getPicture(String result) {
        String picturePriority = "";
        String localPictureBaseUrl = "";
        String qiNiuPictureBaseUrl = "";
        String minioPictureBaseUrl = "";
        // 从Redis中获取系统配置
        String systemConfigJson = redisUtil.get(RedisConf.SYSTEM_CONFIG);
        if (StringUtils.isEmpty(systemConfigJson)) {
            QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            SystemConfig systemConfig = systemConfigService.getOne(queryWrapper);
            if (systemConfig == null) {
                throw new QueryException(MessageConf.SYSTEM_CONFIG_IS_NOT_EXIST);
            } else {
                // 将系统配置存入Redis中【设置过期时间24小时】
                redisUtil.setEx(RedisConf.SYSTEM_CONFIG, JsonUtils.objectToJson(systemConfig), 24, TimeUnit.HOURS);
            }
            picturePriority = systemConfig.getPicturePriority();
            localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
            qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
            minioPictureBaseUrl = systemConfig.getMinioPictureBaseUrl();
        } else {
            SystemConfig systemConfig = JsonUtils.jsonToPojo(systemConfigJson, SystemConfig.class);
            if(systemConfig == null) {
                throw new QueryException(ErrorCode.QUERY_DEFAULT_ERROR, "系统配置转换错误，请检查系统配置，或者清空Redis后重试！");
            }
            picturePriority = systemConfig.getPicturePriority();
            localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
            qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
            minioPictureBaseUrl = systemConfig.getMinioPictureBaseUrl();
        }

        List<String> picUrls = new ArrayList<>();
        try {
            Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
            if (SysConf.SUCCESS.equals(picMap.get(SysConf.CODE))) {
                List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get(SysConf.DATA);
                if (picData.size() > 0) {
                    for (int i = 0; i < picData.size(); i++) {
                        // 判断文件显示优先级【需要显示存储在哪里的图片】
                        if (EFilePriority.QI_NIU.equals(picturePriority)) {
                            picUrls.add(qiNiuPictureBaseUrl + picData.get(i).get(SysConf.QI_NIU_URL));
                        } else if (EFilePriority.MINIO.equals(picturePriority)){
                            picUrls.add(minioPictureBaseUrl + picData.get(i).get(SysConf.MINIO_URL));
                        } else {
                            picUrls.add(localPictureBaseUrl + picData.get(i).get(SysConf.URL));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("从Json中获取图片列表失败");
            log.error(e.getMessage());
            return picUrls;
        }
        return picUrls;
    }

    /**
     * 获取图片，返回Map
     *
     * @param result
     * @return
     */
    public List<Map<String, Object>> getPictureMap(String result) {

        String picturePriority = "";
        String localPictureBaseUrl = "";
        String qiNiuPictureBaseUrl = "";
        String minioPictureBaseUrl = "";
        // 从Redis中获取系统配置
        String systemConfigJson = redisUtil.get(RedisConf.SYSTEM_CONFIG);
        if (StringUtils.isEmpty(systemConfigJson)) {
            QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            SystemConfig systemConfig = systemConfigService.getOne(queryWrapper);
            if (systemConfig == null) {
                throw new QueryException(MessageConf.SYSTEM_CONFIG_IS_NOT_EXIST);
            } else {
                // 将系统配置存入Redis中【设置过期时间24小时】
                redisUtil.setEx(RedisConf.SYSTEM_CONFIG, JsonUtils.objectToJson(systemConfig), 24, TimeUnit.HOURS);
            }
            picturePriority = systemConfig.getPicturePriority();
            localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
            qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
            minioPictureBaseUrl = systemConfig.getMinioPictureBaseUrl();
        } else {
            SystemConfig systemConfig = JsonUtils.jsonToPojo(systemConfigJson, SystemConfig.class);
            picturePriority = systemConfig.getPicturePriority();
            localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
            qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
            minioPictureBaseUrl = systemConfig.getMinioPictureBaseUrl();
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(picMap.get(SysConf.CODE))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get(SysConf.DATA);
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    if (StringUtils.isEmpty(picData.get(i).get(SysConf.UID))) {
                        continue;
                    }
                    // 判断文件显示优先级【需要显示存储在哪里的图片】
                    if (EFilePriority.QI_NIU.equals(picturePriority)) {
                        map.put(SysConf.URL, qiNiuPictureBaseUrl + picData.get(i).get(SysConf.QI_NIU_URL));
                    } else if (EFilePriority.MINIO.equals(picturePriority)){
                        map.put(SysConf.URL, minioPictureBaseUrl + picData.get(i).get(SysConf.MINIO_URL));
                    } else {
                        map.put(SysConf.URL, localPictureBaseUrl + picData.get(i).get(SysConf.URL));
                    }

                    map.put(SysConf.UID, picData.get(i).get(SysConf.UID));
                    resultList.add(map);
                }
            }
        } else if (SysConf.ERROR.equals(picMap.get(SysConf.CODE))) {
            log.error("获取图片失败，图片服务出现异常：{}", picMap.get(SysConf.MESSAGE));
        } else {
            log.error("获取图片失败");
        }
        return resultList;
    }

    /**
     * 获取结果集的数据
     *
     * @param result
     * @return
     */
    public <T> T getData(String result, Class<T> beanType) {
        if (com.moxi.mogublog.utils.StringUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(dataMap.get(SysConf.CODE))) {
            Map<String, Object> data = (Map<String, Object>) dataMap.get(SysConf.DATA);
            T t = JsonUtils.mapToPojo(data, beanType);
            return t;
        }
        return null;
    }

    /**
     * 获取结果集的消息
     *
     * @param result
     * @return
     */
    public Map<String, String> getMessage(String result) {
        Map<String, String> ret = new HashMap<>();
        if (StringUtils.isEmpty(result)) {
            ret.put(SysConf.CODE, SysConf.ERROR);
            ret.put(SysConf.MESSAGE, MessageConf.PARAM_INCORRECT);
            return ret;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(dataMap.get(SysConf.CODE)) && dataMap.get(SysConf.MESSAGE) != null) {
            ret.put(SysConf.CODE, SysConf.SUCCESS);
            ret.put(SysConf.MESSAGE, dataMap.get(SysConf.MESSAGE).toString());
            return ret;
        } else {
            ret.put(SysConf.CODE, SysConf.ERROR);
            ret.put(SysConf.MESSAGE, dataMap.get(SysConf.MESSAGE).toString());
            return ret;
        }
    }


    /**
     * 获取结果集的内容 【带有分页信息】
     *
     * @param result
     * @return
     */
    public <T> List<T> getListByPage(String result, Class<T> beanType) {
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        List<T> resultList = new ArrayList<>();
        if (SysConf.SUCCESS.equals(dataMap.get(SysConf.CODE))) {
            Map<String, Object> data = (Map<String, Object>) dataMap.get(SysConf.DATA);
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(SysConf.RECORDS);
            list.forEach(item -> {
                resultList.add(JsonUtils.mapToPojo(item, beanType));
            });
            return resultList;
        } else {
            log.error((String) dataMap.get(SysConf.MESSAGE));
            return resultList;
        }
    }

    /**
     * 获取结果集的内容
     *
     * @param result
     * @return
     */
    public <T> List<T> getList(String result, Class<T> beanType) {
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        List<T> resultList = new ArrayList<>();
        if (SysConf.SUCCESS.equals(dataMap.get(SysConf.CODE))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get(SysConf.DATA);
            data.forEach(item -> {
                resultList.add(JsonUtils.mapToPojo(item, beanType));
            });
            return resultList;
        } else {
            log.error((String) dataMap.get(SysConf.MESSAGE));
            return resultList;
        }
    }
}