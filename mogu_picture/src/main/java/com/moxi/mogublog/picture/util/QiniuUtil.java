package com.moxi.mogublog.picture.util;

import com.google.gson.Gson;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EQiNiuArea;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author 陌溪
 * @date 2020年1月20日20:02:36
 * @comments: 七牛云图片配置
 */
@Slf4j
@Component
public class QiniuUtil {

    /**
     * 七牛云上传图片
     * @param localFilePath
     * @return
     */
    public String uploadQiniu (File localFilePath, Map<String, String> qiNiuConfig) throws QiniuException {

        //生成上传凭证，然后准备上传
        String accessKey = qiNiuConfig.get("qiNiuAccessKey");
        String secretKey = qiNiuConfig.get("qiNiuSecretKey");
        String bucket = qiNiuConfig.get("qiNiuBucket");
        String area = qiNiuConfig.get("qiNiuArea");

        //构造一个带指定Zone对象的配置类
        Configuration cfg = null;


        //zong2() 代表华南地区
        String as0 = EQiNiuArea.as0.getCode();
        switch (EQiNiuArea.valueOf(area).getCode()) {
            case "z0": {
                cfg = new Configuration(Zone.zone0());
            }; break;
            case "z1": {
                cfg = new Configuration(Zone.zone1());
            }; break;
            case "z2": {
                cfg = new Configuration(Zone.zone2());
            }; break;
            case "na0": {
                cfg = new Configuration(Zone.zoneNa0());
            }; break;
            case "as0": {
                cfg = new Configuration(Zone.zoneAs0());
            }; break;
            default:{
                return null;
            }
        }

        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        String key = StringUtils.getUUID();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        String result = null;

        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        log.info("{七牛图片上传key: "+ putRet.key+",七牛图片上传hash: "+ putRet.hash+"}");

        result = putRet.key;

        return result;
    }

}