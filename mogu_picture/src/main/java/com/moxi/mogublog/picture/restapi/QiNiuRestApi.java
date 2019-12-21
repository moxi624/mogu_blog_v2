package com.moxi.mogublog.picture.restapi;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 骑牛图片上传RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2019年11月26日16:15:29
 */
@RestController
@RequestMapping("/qiNiuFile")
@Api(value = "七牛RestApi", tags = {"QiNiuRestApi"})
public class QiNiuRestApi {


    public static void main(String[] args) {
        Configuration cfg = new Configuration(Zone.zone2());                //zong2() 代表华南地区
        UploadManager uploadManager = new UploadManager(cfg);

        String accessKey = "";      //AccessKey的值
        String secretKey = "";      //SecretKey的值
        String bucket = "mogublog";                                          //存储空间名
        String localFilePath = "D:\\CNN.png";     //上传图片路径

        String key = "CNN.png";                                               //在七牛云中图片的命名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

}

