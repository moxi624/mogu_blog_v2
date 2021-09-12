package com.moxi.mogublog.picture.util;

import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.commons.feign.WebFeignClient;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.global.RedisConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.global.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
     * 通过Token获取系统配置【返回Map类型】
     *
     * @return
     */
    public Map<String, String> getSystemConfigMap(String token) {
        // 判断该token的有效性
        String adminJsonResult = stringRedisTemplate.opsForValue().get(RedisConf.LOGIN_TOKEN_KEY + Constants.SYMBOL_COLON + token);
        if (StringUtils.isEmpty(adminJsonResult)) {
            throw new QueryException(ErrorCode.INVALID_TOKEN, MessageConf.INVALID_TOKEN);
        }
        // 从Redis中获取的SystemConf 或者 通过feign获取的
        Map<String, String> resultMap = new HashMap<>();
        //从Redis中获取内容
        String jsonResult = stringRedisTemplate.opsForValue().get(RedisConf.SYSTEM_CONFIG);
        // 判断Redis中是否有数据
        if (StringUtils.isNotEmpty(jsonResult)) {
            resultMap = (Map<String, String>) JsonUtils.jsonToMap(jsonResult, String.class);
        } else {
            // 通过feign获取系统配置
            String resultStr = adminFeignClient.getSystemConfig();
            Map<String, Object> resultTempMap = JsonUtils.jsonToMap(resultStr);
            if (resultTempMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultTempMap.get(SysConf.CODE).toString())) {
                resultMap = (Map<String, String>) resultTempMap.get(SysConf.DATA);
                //将从token存储到redis中，设置30分钟后过期
                stringRedisTemplate.opsForValue().set(RedisConf.SYSTEM_CONFIG, JsonUtils.objectToJson(resultMap), 30, TimeUnit.MINUTES);
            }
        }
        return resultMap;
    }

    /**
     * 获取系统配置，不论是Admin端还是Web端
     *
     * @return
     */
    public SystemConfig getSystemConfig() {
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        // 后台携带的token
        Object token = request.getAttribute(SysConf.TOKEN);
        // 参数中携带的token
        String paramsToken = request.getParameter(SysConf.TOKEN);
        // 获取平台【web：门户，admin：管理端】
        String platform = request.getParameter(SysConf.PLATFORM);
        Map<String, String> systemConfigMap = new HashMap<>();
        // 判断是否是web端发送过来的请求【后端发送过来的token长度为32】
        if (SysConf.WEB.equals(platform) || (paramsToken != null && paramsToken.length() == Constants.THIRTY_TWO)) {
            // 如果是调用web端获取配置的接口
            systemConfigMap = this.getSystemConfigByWebToken(paramsToken);
        } else {
            // 调用admin端获取配置接口
            if (token != null) {
                // 判断是否是后台过来的请求
                systemConfigMap = this.getSystemConfigMap(token.toString());
            } else {
                // 判断是否是通过params参数传递过来的
                systemConfigMap = this.getSystemConfigMap(paramsToken);
            }
        }

        if (systemConfigMap == null) {
            log.error(MessageConf.PLEASE_SET_QI_NIU);
            throw new QueryException(ErrorCode.PLEASE_SET_QI_NIU, MessageConf.PLEASE_SET_QI_NIU);
        }

        SystemConfig systemConfig = new SystemConfig();
        if (systemConfigMap == null) {
            throw new QueryException(ErrorCode.SYSTEM_CONFIG_NOT_EXIST, MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        } else {
            String uploadQiNiu = systemConfigMap.get(SysConf.UPLOAD_QI_NIU);
            String uploadLocal = systemConfigMap.get(SysConf.UPLOAD_LOCAL);
            String localPictureBaseUrl = systemConfigMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
            String qiNiuPictureBaseUrl = systemConfigMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
            String qiNiuAccessKey = systemConfigMap.get(SysConf.QI_NIU_ACCESS_KEY);
            String qiNiuSecretKey = systemConfigMap.get(SysConf.QI_NIU_SECRET_KEY);
            String qiNiuBucket = systemConfigMap.get(SysConf.QI_NIU_BUCKET);
            String qiNiuArea = systemConfigMap.get(SysConf.QI_NIU_AREA);
            String minioEndPoint = systemConfigMap.get(SysConf.MINIO_END_POINT);
            String minioAccessKey = systemConfigMap.get(SysConf.MINIO_ACCESS_KEY);
            String minioSecretKey = systemConfigMap.get(SysConf.MINIO_SECRET_KEY);
            String minioBucket = systemConfigMap.get(SysConf.MINIO_BUCKET);
            String uploadMinio = systemConfigMap.get(SysConf.UPLOAD_MINIO);
            String minioPictureBaseUrl = systemConfigMap.get(SysConf.MINIO_PICTURE_BASE_URL);

            // 判断七牛云参数是否存在异常
            if (EOpenStatus.OPEN.equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                throw new QueryException(ErrorCode.PLEASE_SET_QI_NIU, MessageConf.PLEASE_SET_QI_NIU);
            }
            // 判断本地服务参数是否存在异常
            if (EOpenStatus.OPEN.equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                throw new QueryException(ErrorCode.PLEASE_SET_LOCAL, MessageConf.PLEASE_SET_QI_NIU);
            }
            // 判断Minio服务是否存在异常
            if (EOpenStatus.OPEN.equals(uploadMinio) && (StringUtils.isEmpty(minioEndPoint) || StringUtils.isEmpty(minioPictureBaseUrl) || StringUtils.isEmpty(minioAccessKey)
                    || StringUtils.isEmpty(minioSecretKey) || StringUtils.isEmpty(minioBucket))) {
                throw new QueryException(ErrorCode.PLEASE_SET_MINIO, MessageConf.PLEASE_SET_MINIO);
            }

            systemConfig.setQiNiuAccessKey(qiNiuAccessKey);
            systemConfig.setQiNiuSecretKey(qiNiuSecretKey);
            systemConfig.setQiNiuBucket(qiNiuBucket);
            systemConfig.setQiNiuArea(qiNiuArea);
            systemConfig.setUploadQiNiu(uploadQiNiu);
            systemConfig.setUploadLocal(uploadLocal);
            systemConfig.setPicturePriority(systemConfigMap.get(SysConf.PICTURE_PRIORITY));
            systemConfig.setLocalPictureBaseUrl(systemConfigMap.get(SysConf.LOCAL_PICTURE_BASE_URL));
            systemConfig.setQiNiuPictureBaseUrl(systemConfigMap.get(SysConf.QI_NIU_PICTURE_BASE_URL));

            systemConfig.setMinioEndPoint(minioEndPoint);
            systemConfig.setMinioAccessKey(minioAccessKey);
            systemConfig.setMinioSecretKey(minioSecretKey);
            systemConfig.setMinioBucket(minioBucket);
            systemConfig.setMinioPictureBaseUrl(minioPictureBaseUrl);
            systemConfig.setUploadMinio(uploadMinio);

        }
        return systemConfig;
    }

    /**
     * 通过Token获取系统配置 【传入AdminToken】
     *
     * @param token
     * @return
     */
    public SystemConfig getSystemConfig(String token) {
        Map<String, String> systemConfigMap = this.getSystemConfigMap(token);
        SystemConfig systemConfig = new SystemConfig();
        if (systemConfigMap == null) {
            throw new QueryException(ErrorCode.SYSTEM_CONFIG_NOT_EXIST, MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        } else {
            String uploadQiNiu = systemConfigMap.get(SysConf.UPLOAD_QI_NIU);
            String uploadLocal = systemConfigMap.get(SysConf.UPLOAD_LOCAL);
            String localPictureBaseUrl = systemConfigMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
            String qiNiuPictureBaseUrl = systemConfigMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
            String qiNiuAccessKey = systemConfigMap.get(SysConf.QI_NIU_ACCESS_KEY);
            String qiNiuSecretKey = systemConfigMap.get(SysConf.QI_NIU_SECRET_KEY);
            String qiNiuBucket = systemConfigMap.get(SysConf.QI_NIU_BUCKET);
            String qiNiuArea = systemConfigMap.get(SysConf.QI_NIU_AREA);

            String minioEndPoint = systemConfigMap.get(SysConf.MINIO_END_POINT);
            String minioAccessKey = systemConfigMap.get(SysConf.MINIO_ACCESS_KEY);
            String minioSecretKey = systemConfigMap.get(SysConf.MINIO_SECRET_KEY);
            String minioBucket = systemConfigMap.get(SysConf.MINIO_BUCKET);
            String uploadMinio = systemConfigMap.get(SysConf.UPLOAD_MINIO);
            String minioPictureBaseUrl = systemConfigMap.get(SysConf.MINIO_PICTURE_BASE_URL);

            if (EOpenStatus.OPEN.equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                throw new QueryException(ErrorCode.PLEASE_SET_QI_NIU, MessageConf.PLEASE_SET_QI_NIU);
            }

            if (EOpenStatus.OPEN.equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                throw new QueryException(ErrorCode.PLEASE_SET_LOCAL, MessageConf.PLEASE_SET_QI_NIU);
            }

            // 判断Minio服务是否存在异常
            if (EOpenStatus.OPEN.equals(uploadMinio) && (StringUtils.isEmpty(minioEndPoint) || StringUtils.isEmpty(minioPictureBaseUrl) || StringUtils.isEmpty(minioAccessKey)
                    || StringUtils.isEmpty(minioSecretKey) || StringUtils.isEmpty(minioBucket))) {
                throw new QueryException(ErrorCode.PLEASE_SET_MINIO, MessageConf.PLEASE_SET_MINIO);
            }

            systemConfig.setQiNiuAccessKey(qiNiuAccessKey);
            systemConfig.setQiNiuSecretKey(qiNiuSecretKey);
            systemConfig.setQiNiuBucket(qiNiuBucket);
            systemConfig.setQiNiuArea(qiNiuArea);
            systemConfig.setUploadQiNiu(uploadQiNiu);
            systemConfig.setUploadLocal(uploadLocal);
            systemConfig.setPicturePriority(systemConfigMap.get(SysConf.PICTURE_PRIORITY));
            systemConfig.setLocalPictureBaseUrl(systemConfigMap.get(SysConf.LOCAL_PICTURE_BASE_URL));
            systemConfig.setQiNiuPictureBaseUrl(systemConfigMap.get(SysConf.QI_NIU_PICTURE_BASE_URL));

            systemConfig.setMinioEndPoint(minioEndPoint);
            systemConfig.setMinioAccessKey(minioAccessKey);
            systemConfig.setMinioSecretKey(minioSecretKey);
            systemConfig.setMinioBucket(minioBucket);
            systemConfig.setMinioPictureBaseUrl(minioPictureBaseUrl);
            systemConfig.setUploadMinio(uploadMinio);


        }
        return systemConfig;
    }

    /**
     * 从Map中获取系统配置
     *
     * @param systemConfigMap
     * @return
     */
    public SystemConfig getSystemConfigByMap(Map<String, String> systemConfigMap) {
        SystemConfig systemConfig = new SystemConfig();
        if (systemConfigMap == null) {
            throw new QueryException(ErrorCode.SYSTEM_CONFIG_NOT_EXIST, MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        } else {
            String uploadQiNiu = systemConfigMap.get(SysConf.UPLOAD_QI_NIU);
            String uploadLocal = systemConfigMap.get(SysConf.UPLOAD_LOCAL);
            String localPictureBaseUrl = systemConfigMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
            String qiNiuPictureBaseUrl = systemConfigMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
            String qiNiuAccessKey = systemConfigMap.get(SysConf.QI_NIU_ACCESS_KEY);
            String qiNiuSecretKey = systemConfigMap.get(SysConf.QI_NIU_SECRET_KEY);
            String qiNiuBucket = systemConfigMap.get(SysConf.QI_NIU_BUCKET);
            String qiNiuArea = systemConfigMap.get(SysConf.QI_NIU_AREA);
            String picturePriority = systemConfigMap.get(SysConf.PICTURE_PRIORITY);
            String contentPicturePriority = systemConfigMap.get(SysConf.CONTENT_PICTURE_PRIORITY);

            String minioEndPoint = systemConfigMap.get(SysConf.MINIO_END_POINT);
            String minioAccessKey = systemConfigMap.get(SysConf.MINIO_ACCESS_KEY);
            String minioSecretKey = systemConfigMap.get(SysConf.MINIO_SECRET_KEY);
            String minioBucket = systemConfigMap.get(SysConf.MINIO_BUCKET);
            String uploadMinio = systemConfigMap.get(SysConf.UPLOAD_MINIO);
            String minioPictureBaseUrl = systemConfigMap.get(SysConf.MINIO_PICTURE_BASE_URL);

            if (EOpenStatus.OPEN.equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                throw new QueryException(ErrorCode.PLEASE_SET_QI_NIU, MessageConf.PLEASE_SET_QI_NIU);
            }

            if (EOpenStatus.OPEN.equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                throw new QueryException(ErrorCode.PLEASE_SET_LOCAL, MessageConf.PLEASE_SET_QI_NIU);
            }

            // 判断Minio服务是否存在异常
            if (EOpenStatus.OPEN.equals(uploadMinio) && (StringUtils.isEmpty(minioEndPoint) || StringUtils.isEmpty(minioPictureBaseUrl) || StringUtils.isEmpty(minioAccessKey)
                    || StringUtils.isEmpty(minioSecretKey) || StringUtils.isEmpty(minioBucket))) {
                throw new QueryException(ErrorCode.PLEASE_SET_MINIO, MessageConf.PLEASE_SET_MINIO);
            }

            systemConfig.setQiNiuAccessKey(qiNiuAccessKey);
            systemConfig.setQiNiuSecretKey(qiNiuSecretKey);
            systemConfig.setQiNiuBucket(qiNiuBucket);
            systemConfig.setQiNiuArea(qiNiuArea);
            systemConfig.setUploadQiNiu(uploadQiNiu);
            systemConfig.setUploadLocal(uploadLocal);
            systemConfig.setPicturePriority(picturePriority);
            systemConfig.setContentPicturePriority(contentPicturePriority);
            systemConfig.setLocalPictureBaseUrl(localPictureBaseUrl);
            systemConfig.setQiNiuPictureBaseUrl(qiNiuPictureBaseUrl);

            systemConfig.setMinioEndPoint(minioEndPoint);
            systemConfig.setMinioAccessKey(minioAccessKey);
            systemConfig.setMinioSecretKey(minioSecretKey);
            systemConfig.setMinioBucket(minioBucket);
            systemConfig.setMinioPictureBaseUrl(minioPictureBaseUrl);
            systemConfig.setUploadMinio(uploadMinio);
        }
        return systemConfig;
    }


    /**
     * 通过Web端的token获取系统配置文件 【传入Admin端的token】
     *
     * @param token
     * @return
     */
    public Map<String, String> getSystemConfigByWebToken(String token) {
        // 判断该token的有效性
        String webUserJsonResult = stringRedisTemplate.opsForValue().get(RedisConf.USER_TOKEN + Constants.SYMBOL_COLON + token);
        if (StringUtils.isEmpty(webUserJsonResult)) {
            throw new QueryException(ErrorCode.INVALID_TOKEN, MessageConf.INVALID_TOKEN);
        }
        // 从Redis中获取的SystemConf 或者 通过feign获取的
        Map<String, String> resultMap = new HashMap<>();
        //从Redis中获取内容
        String jsonResult = stringRedisTemplate.opsForValue().get(RedisConf.SYSTEM_CONFIG);
        // 判断Redis中是否有数据
        if (StringUtils.isNotEmpty(jsonResult)) {
            resultMap = (Map<String, String>) JsonUtils.jsonToMap(jsonResult, String.class);
        } else {
            // 进行七牛云校验
            String resultStr = webFeignClient.getSystemConfig(token);
            Map<String, Object> resultTempMap = JsonUtils.jsonToMap(resultStr);
            if (resultTempMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultTempMap.get(SysConf.CODE).toString())) {
                resultMap = (Map<String, String>) resultTempMap.get(SysConf.DATA);
                //将从token存储到redis中，设置30分钟后过期
                stringRedisTemplate.opsForValue().set(RedisConf.SYSTEM_CONFIG, JsonUtils.objectToJson(resultMap), 30, TimeUnit.MINUTES);
            }
        }
        return resultMap;
    }

}
