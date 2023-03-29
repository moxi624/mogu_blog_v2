package com.moxi.mogublog.picture.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.gson.Gson;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EQiNiuArea;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.global.ErrorCode;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 七牛云工具类
 *
 * @author 陌溪
 * @date 2020年1月20日20:02:36
 * @comments: 七牛云图片配置
 */
@Slf4j
@Component
public class QiniuUtil {

    @Autowired
    FeignUtil feignUtil;

    /**
     * 七牛云上传图片
     *
     * @param localFilePath
     * @return
     */
    public String uploadQiniu(File localFilePath, Map<String, String> qiNiuConfig) throws QiniuException {

        //构造一个带指定Zone对象的配置类
        Configuration cfg = setQiNiuArea(qiNiuConfig.get(SysConf.QI_NIU_AREA));
        //生成上传凭证，然后准备上传
        String accessKey = qiNiuConfig.get(SysConf.QI_NIU_ACCESS_KEY);
        String secretKey = qiNiuConfig.get(SysConf.QI_NIU_SECRET_KEY);
        String bucket = qiNiuConfig.get(SysConf.QI_NIU_BUCKET);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String key = StringUtils.getUUID();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        log.info("{七牛图片上传key: " + putRet.key + ",七牛图片上传hash: " + putRet.hash + "}");
        return putRet.key;
    }

    /**
     * 七牛云上传图片
     *
     * @param localFilePath
     * @return
     */
    public String uploadQiniu(File localFilePath, SystemConfig qiNiuConfig) throws QiniuException {
        // 自动选择上传区域
        Configuration cfg = new Configuration(Region.autoRegion());
        //生成上传凭证，然后准备上传
        String accessKey = qiNiuConfig.getQiNiuAccessKey();
        String secretKey = qiNiuConfig.getQiNiuSecretKey();
        String bucket = qiNiuConfig.getQiNiuBucket();
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String key = StringUtils.getUUID();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        log.info("{七牛图片上传key: " + putRet.key + ",七牛图片上传hash: " + putRet.hash + "}");
        return putRet.key;
    }


    /**
     * 删除七牛云文件
     *
     * @param fileName
     * @param qiNiuConfig
     * @return
     */
    public int deleteFile(String fileName, Map<String, String> qiNiuConfig) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = setQiNiuArea(qiNiuConfig.get(SysConf.QI_NIU_AREA));
        //获取上传凭证
        String accessKey = qiNiuConfig.get(SysConf.QI_NIU_ACCESS_KEY);
        String secretKey = qiNiuConfig.get(SysConf.QI_NIU_SECRET_KEY);
        String bucket = qiNiuConfig.get(SysConf.QI_NIU_BUCKET);
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete(bucket, key);
            log.info("{七牛云文件 {} 删除成功", fileName);
            return delete.statusCode;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error(ex.getMessage());
        }
        return -1;
    }

    /**
     * 批量删除七牛云图片
     *
     * @param fileNameList
     * @param qiNiuConfig
     * @return
     */
    public Boolean deleteFileList(List<String> fileNameList, Map<String, String> qiNiuConfig) {
        if (CollUtil.isEmpty(fileNameList)){
            return true;
        }
        //构造一个带指定Zone对象的配置类
        Configuration cfg = setQiNiuArea(qiNiuConfig.get(SysConf.QI_NIU_AREA));
        //获取上传凭证
        String accessKey = qiNiuConfig.get(SysConf.QI_NIU_ACCESS_KEY);
        String secretKey = qiNiuConfig.get(SysConf.QI_NIU_SECRET_KEY);
        String bucket = qiNiuConfig.get(SysConf.QI_NIU_BUCKET);
        int successCount = 0;

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        try {
            List<String> deleteObjects = new LinkedList<>();
            for (String fileName:fileNameList){
                deleteObjects.add(fileName);
                // 七牛云规定不超过1000
                if (deleteObjects.size()==999){
                    // 构建批量删除项
                    BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
                    batchOperations.addDeleteOp(bucket, ArrayUtil.toArray(deleteObjects,String.class));
                    bucketManager.batch(batchOperations);
                    log.info("{七牛云文件 {} 删除成功", deleteObjects);
                    deleteObjects = new LinkedList<>();
                }
            }

            // 删除剩下的部分
            if (CollUtil.isNotEmpty(deleteObjects)){
                BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
                batchOperations.addDeleteOp(bucket, ArrayUtil.toArray(deleteObjects,String.class));
                bucketManager.batch(batchOperations);
                log.info("{七牛云文件 {} 删除成功", deleteObjects);
            }
        } catch (QiniuException e) {
            //如果遇到异常，说明删除失败
            log.error("删除失败:",e);
        }
        return successCount == fileNameList.size();
    }

    /**
     * 设置七牛云上传区域（内部方法）
     *
     * @param area
     * @return
     */
    private Configuration setQiNiuArea(String area) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = null;

        //zong2() 代表华南地区
        switch (EQiNiuArea.valueOf(area).getCode()) {
            case "z0": {
                cfg = new Configuration(Zone.zone0());
            }
            break;
            case "z1": {
                cfg = new Configuration(Zone.zone1());
            }
            break;
            case "z2": {
                cfg = new Configuration(Zone.zone2());
            }
            break;
            case "na0": {
                cfg = new Configuration(Zone.zoneNa0());
            }
            break;
            case "as0": {
                cfg = new Configuration(Zone.zoneAs0());
            }
            break;
            default: {
                return null;
            }
        }
        return cfg;
    }

    /**
     * 获取七牛云配置【从Redis文件中获取】
     *
     * @return
     */
    public Map<String, String> getQiNiuConfig() {
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        // 后台携带的token
        Object token = request.getAttribute(SysConf.TOKEN);
        // 参数中携带的token
        String paramsToken = request.getParameter(SysConf.TOKEN);
        // 获取平台【web：门户，admin：管理端】
        String platform = request.getParameter(SysConf.PLATFORM);
        Map<String, String> qiNiuResultMap = new HashMap<>();
        // 判断是否是web端发送过来的请求【后端发送过来的token长度为32】
        if (SysConf.WEB.equals(platform) || paramsToken.length() == Constants.THIRTY_TWO) {
            // 如果是调用web端获取配置的接口
            qiNiuResultMap = feignUtil.getSystemConfigByWebToken(paramsToken);
        } else {
            // 调用admin端获取配置接口
            if (token != null) {
                // 判断是否是后台过来的请求
                qiNiuResultMap = feignUtil.getSystemConfigMap(token.toString());
            } else {
                // 判断是否是通过params参数传递过来的
                qiNiuResultMap = feignUtil.getSystemConfigMap(paramsToken);
            }
        }

        if (qiNiuResultMap == null) {
            log.error(MessageConf.PLEASE_SET_QI_NIU);
            throw new QueryException(ErrorCode.PLEASE_SET_QI_NIU, MessageConf.PLEASE_SET_QI_NIU);
        }

        String uploadQiNiu = qiNiuResultMap.get(SysConf.UPLOAD_QI_NIU);
        String uploadLocal = qiNiuResultMap.get(SysConf.UPLOAD_LOCAL);
        String localPictureBaseUrl = qiNiuResultMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
        String qiNiuPictureBaseUrl = qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
        String qiNiuAccessKey = qiNiuResultMap.get(SysConf.QI_NIU_ACCESS_KEY);
        String qiNiuSecretKey = qiNiuResultMap.get(SysConf.QI_NIU_SECRET_KEY);
        String qiNiuBucket = qiNiuResultMap.get(SysConf.QI_NIU_BUCKET);
        String qiNiuArea = qiNiuResultMap.get(SysConf.QI_NIU_AREA);
        String picturePriority = qiNiuResultMap.get(SysConf.PICTURE_PRIORITY);

        if (EOpenStatus.OPEN.equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
            log.error(MessageConf.PLEASE_SET_QI_NIU);
            throw new QueryException(ErrorCode.PLEASE_SET_QI_NIU, MessageConf.PLEASE_SET_QI_NIU);
        }

        if (EOpenStatus.OPEN.equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
            log.error(MessageConf.PLEASE_SET_QI_NIU);
            throw new QueryException(ErrorCode.PLEASE_SET_LOCAL, MessageConf.PLEASE_SET_LOCAL);
        }

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();
        qiNiuConfig.put(SysConf.QI_NIU_ACCESS_KEY, qiNiuAccessKey);
        qiNiuConfig.put(SysConf.QI_NIU_SECRET_KEY, qiNiuSecretKey);
        qiNiuConfig.put(SysConf.QI_NIU_BUCKET, qiNiuBucket);
        qiNiuConfig.put(SysConf.QI_NIU_AREA, qiNiuArea);
        qiNiuConfig.put(SysConf.UPLOAD_QI_NIU, uploadQiNiu);
        qiNiuConfig.put(SysConf.UPLOAD_LOCAL, uploadLocal);
        qiNiuConfig.put(SysConf.PICTURE_PRIORITY, picturePriority);
        qiNiuConfig.put(SysConf.LOCAL_PICTURE_BASE_URL, localPictureBaseUrl);
        qiNiuConfig.put(SysConf.QI_NIU_PICTURE_BASE_URL, qiNiuPictureBaseUrl);
        return qiNiuConfig;
    }

}