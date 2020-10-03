package com.moxi.mogublog.xo.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.service.SystemConfigService;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
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
    } else {
      SystemConfig systemConfig = JsonUtils.jsonToPojo(systemConfigJson, SystemConfig.class);
      picturePriority = systemConfig.getPicturePriority();
      localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
      qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
    }

    List<String> picUrls = new ArrayList<>();
    try {
      Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
      if (SysConf.SUCCESS.equals(picMap.get(SysConf.CODE))) {
        List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get(SysConf.DATA);
        if (picData.size() > 0) {
          for (int i = 0; i < picData.size(); i++) {
            if ("1".equals(picturePriority)) {
              picUrls.add(qiNiuPictureBaseUrl + picData.get(i).get(SysConf.QI_NIU_URL));
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
    } else {
      SystemConfig systemConfig = JsonUtils.jsonToPojo(systemConfigJson, SystemConfig.class);
      picturePriority = systemConfig.getPicturePriority();
      localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
      qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
    }

    List<Map<String, Object>> resultList = new ArrayList<>();
    Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
    if (SysConf.SUCCESS.equals(picMap.get(SysConf.CODE))) {
      List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get(SysConf.DATA);
      if (picData.size() > 0) {
        for (int i = 0; i < picData.size(); i++) {
          Map<String, Object> map = new HashMap<>();
          if (StringUtils.isEmpty(picData.get(i).get(SysConf.URL)) || StringUtils.isEmpty(picData.get(i).get(SysConf.UID))) {
            continue;
          }
          // 图片优先显示 七牛云 or 本地
          if (EOpenStatus.OPEN.equals(picturePriority)) {
            map.put(SysConf.URL, qiNiuPictureBaseUrl + picData.get(i).get(SysConf.QI_NIU_URL));
          } else {
            map.put(SysConf.URL, localPictureBaseUrl + picData.get(i).get(SysConf.URL));
          }
          map.put(SysConf.UID, picData.get(i).get(SysConf.UID));
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
    if (SysConf.SUCCESS.equals(dataMap.get(SysConf.CODE))) {

      Map<String, Object> data = (Map<String, Object>) dataMap.get(SysConf.CODE);
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
    if (SysConf.SUCCESS.equals(dataMap.get(SysConf.CODE))) {
      Map<String, Object> data = (Map<String, Object>) dataMap.get(SysConf.DATA);
      List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(SysConf.RECORDS);
      List<T> resultList = new ArrayList<>();
      list.forEach(item -> {
        resultList.add(JsonUtils.mapToPojo(item, beanType));
      });
      return resultList;
    }
    return null;
  }
}