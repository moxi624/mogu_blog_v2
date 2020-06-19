package com.moxi.mogublog.picture.util;

import com.google.gson.Gson;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EQiNiuArea;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Configuration cfg = setQiNiuArea(qiNiuConfig);
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
     * 删除七牛云文件
     *
     * @param fileName
     * @param qiNiuConfig
     * @return
     */
    public int deleteFile(String fileName, Map<String, String> qiNiuConfig) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = setQiNiuArea(qiNiuConfig);
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
        //构造一个带指定Zone对象的配置类
        Configuration cfg = setQiNiuArea(qiNiuConfig);
        //获取上传凭证
        String accessKey = qiNiuConfig.get(SysConf.QI_NIU_ACCESS_KEY);
        String secretKey = qiNiuConfig.get(SysConf.QI_NIU_SECRET_KEY);
        String bucket = qiNiuConfig.get(SysConf.QI_NIU_BUCKET);
        int successCount = 0;
        for (String fileName : fileNameList) {
            String key = fileName;
            Auth auth = Auth.create(accessKey, secretKey);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            try {
                Response delete = bucketManager.delete(bucket, key);
                log.info("{七牛云文件 {} 删除成功", fileName);
                successCount += 1;
            } catch (QiniuException ex) {
                //如果遇到异常，说明删除失败
                log.error(ex.getMessage());
            }
        }
        return successCount == fileNameList.size();
    }

    /**
     * 设置七牛云上传区域（内部方法）
     *
     * @param qiNiuConfig
     * @return
     */
    private Configuration setQiNiuArea(Map<String, String> qiNiuConfig) {
        //生成上传凭证，然后准备上传
        String area = qiNiuConfig.get("qiNiuArea");

        //构造一个带指定Zone对象的配置类
        Configuration cfg = null;

        //zong2() 代表华南地区
        switch (EQiNiuArea.valueOf(area).getCode()) {
            case "z0": {
                cfg = new Configuration(Zone.zone0());
            }
            ;
            break;
            case "z1": {
                cfg = new Configuration(Zone.zone1());
            }
            ;
            break;
            case "z2": {
                cfg = new Configuration(Zone.zone2());
            }
            ;
            break;
            case "na0": {
                cfg = new Configuration(Zone.zoneNa0());
            }
            ;
            break;
            case "as0": {
                cfg = new Configuration(Zone.zoneAs0());
            }
            ;
            break;
            default: {
                return null;
            }
        }
        return cfg;
    }

    /**
     * 获取七牛云配置
     *
     * @return
     */
    public String getQiNiuConfig() {

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        String token = request.getAttribute(SysConf.TOKEN).toString();
        // 获取七牛云配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getQiNiuConfig(token);
        if (qiNiuResultMap == null) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
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
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        }

        if (EOpenStatus.OPEN.equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
            return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
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
        return ResultUtil.result(SysConf.SUCCESS, qiNiuConfig);
    }

}