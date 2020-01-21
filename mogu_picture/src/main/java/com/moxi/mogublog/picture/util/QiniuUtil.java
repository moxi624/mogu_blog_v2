package com.moxi.mogublog.picture.util;

import com.google.gson.Gson;
import com.moxi.mogublog.utils.StringUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import java.io.File;

/**
 * @author 陌溪
 * @date 2020年1月20日20:02:36
 * @comments: 七牛云图片配置
 */
@Slf4j
public class QiniuUtil {

    /**
     * 设置好账号的ACCESS_KEY和SECRET_KEY
     */
    final String ACCESS_KEY = "QKD378sek_yRy0AlpWEzT_U_oni0SfrrxaP2lgWX";
    final String SECRET_KEY = "Vjh0zPBLrflxYn08YNIG4rkRYv7sqtlMccgd8QzL";

    /**
     * 要上传的空间
     */
    final String BUCKET_NAME = "mogublog";

    /**
     * 七牛云上传图片
     * @param localFilePath
     * @return
     */
    public String uoloapQiniu (File localFilePath, String fileName) throws QiniuException {

        //构造一个带指定Zone对象的配置类
        //zong2() 代表华南地区
        Configuration cfg = new Configuration(Zone.zone2());

        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //...生成上传凭证，然后准备上传
        String accessKey = ACCESS_KEY;
        String secretKey = SECRET_KEY;
        String bucket = BUCKET_NAME;

        String key = StringUtils.getUUID();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        String result = null;

        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        log.info("{七牛图片上传key: "+ putRet.key+",七牛图片上传hash: "+ putRet.hash+"}");

        result = "http://image.moguit.cn/" + putRet.key;

        return result;
    }

}