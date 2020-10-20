//package com.moxi.mogublog.picture.restapi;
//
//import com.google.gson.Gson;
//import com.moxi.mogublog.commons.feign.AdminFeignClient;
//import com.moxi.mogublog.picture.global.SysConf;
//import com.moxi.mogublog.picture.service.QiniuService;
//import com.moxi.mogublog.utils.JsonUtils;
//import com.moxi.mogublog.utils.ResultUtil;
//import com.moxi.mogublog.utils.StringUtils;
//import com.qiniu.common.QiniuException;
//import com.qiniu.common.Zone;
//import com.qiniu.http.Response;
//import com.qiniu.storage.Configuration;
//import com.qiniu.storage.UploadManager;
//import com.qiniu.storage.model.DefaultPutRet;
//import com.qiniu.util.Auth;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * Minio文件上传RestApi
// *
// * @author 陌溪
// * @since 2020年10月19日11:09:54
// */
//@RestController
//@RequestMapping("/minio")
//@Api(value = "Minio相关接口", tags = {"Minio相关接口"})
//@Slf4j
//public class MinioRestApi {
//
//    @Autowired
//    AdminFeignClient adminFeignClient;
//
//    @Autowired
//    private QiniuService qiniuService;
//
//    /**
//     * 多文件上传Minio
//     *
//     * @param files
//     * @return
//     */
//    @ApiOperation(value = "多文件上传七牛云", notes = "多文件上传七牛云")
//    @RequestMapping(value = "/imgs", method = RequestMethod.POST)
//    public String uploadImg(@RequestParam("file") MultipartFile[] files) {
//
//        String resultStr = adminFeignClient.getSystemConfig();
//
//        Map<String, Object> resultTempMap = JsonUtils.jsonToMap(resultStr);
//
//        // 七牛云配置
//        Map<String, String> qiNiuConfig = new HashMap<>();
//
//        if (resultTempMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultTempMap.get(SysConf.CODE).toString())) {
//            Map<String, String> resultMap = (Map<String, String>) resultTempMap.get(SysConf.DATA);
//            qiNiuConfig.put("qiNiuAccessKey", resultMap.get("qiNiuAccessKey"));
//            qiNiuConfig.put("qiNiuSecretKey", resultMap.get("qiNiuSecretKey"));
//            qiNiuConfig.put("qiNiuBucket", resultMap.get("qiNiuBucket"));
//            qiNiuConfig.put("qiNiuArea", resultMap.get("qiNiuArea"));
//        } else {
//            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
//        }
//
//        // 验证非空
//        if (StringUtils.isBlank(files[0].getOriginalFilename())) {
//            return ResultUtil.result(SysConf.ERROR, "文件不能为空");
//        } else {
//            Map<String, List<String>> map = new HashMap<>();
//
//            map = qiniuService.uploadImgs(files, qiNiuConfig);
//
//            List<String> resultList = map.get("result");
//
//            log.info("图片上传返回结果:" + resultList);
//
//            if (SysConf.ERROR.equals(resultList.get(0))) {
//                return ResultUtil.result(SysConf.ERROR, "上传失败");
//            } else {
//                return ResultUtil.result(SysConf.SUCCESS, resultList);
//            }
//        }
//
//    }
//
//}
//
