package com.moxi.mogublog.picture.restapi;

import com.google.gson.Gson;
import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.QiniuService;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 七牛图片上传RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2019年11月26日16:15:29
 */
@RestController
@RequestMapping("/qiNiuFile")
@Api(value = "七牛RestApi", tags = {"QiNiuRestApi"})
@Slf4j
public class QiNiuRestApi {

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    AdminFeignClient adminFeignClient;

    /**
     * 多文件上传七牛云
     * @param files
     * @return
     */
    @ApiOperation(value = "多文件上传七牛云", notes = "多文件上传七牛云")
    @RequestMapping(value="/imgs", method = RequestMethod.POST)
    public String uploadImg(@RequestParam("file") MultipartFile[] files) {

        String resultStr = adminFeignClient.getSystemConfig();

        Map<String, Object> resultTempMap = JsonUtils.jsonToMap(resultStr);

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        if(resultTempMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultTempMap.get(SysConf.CODE).toString())) {
            Map<String, String> resultMap = (Map<String, String>) resultTempMap.get(SysConf.DATA);
            qiNiuConfig.put("qiNiuAccessKey", resultMap.get("qiNiuAccessKey"));
            qiNiuConfig.put("qiNiuSecretKey", resultMap.get("qiNiuSecretKey"));
            qiNiuConfig.put("qiNiuBucket", resultMap.get("qiNiuBucket"));
            qiNiuConfig.put("qiNiuArea", resultMap.get("qiNiuArea"));
        } else {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        }

        // 验证非空
        if (StringUtils.isBlank(files[0].getOriginalFilename())) {
            return ResultUtil.result(SysConf.ERROR, "文件不能为空");
        } else {
            Map<String, List<String>> map = new HashMap<>();

            map = qiniuService.uploadImgs(files, qiNiuConfig);

            List<String> resultList = map.get("result");

            log.info("图片上传返回结果:"+resultList);

            if ("error".equals(resultList.get(0))) {
                return ResultUtil.result(SysConf.ERROR, "上传失败");
            } else {
                return ResultUtil.result(SysConf.SUCCESS, resultList);
            }
        }

    }

    public static void main(String[] args) {
        //zong2() 代表华南地区
        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);

        //AccessKey的值
        String accessKey = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";

        //SecretKey的值
        String secretKey = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";

        //存储空间名
        String bucket = "XXXXXXXXXXX";

        //上传图片路径
        String localFilePath = "D:\\1582507567527.png";

        //在七牛云中图片的命名
        String key = "1582507567527.png";
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

