//package com.moxi.mogublog.picture.util;
//
//import com.moxi.mogublog.commons.entity.SystemConfig;
//import com.moxi.mogublog.picture.global.MessageConf;
//import com.moxi.mogublog.utils.ResultUtil;
//import com.moxi.mougblog.base.holder.RequestHolder;
//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;
//import io.minio.errors.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * Minio上传工具类
// * @author: 陌溪
// * @create: 2020-10-19-10:22
// */
//@Component
//public class MinioUtil {
//
//    @Autowired
//    MinioClient minioClient;
//
//    @Autowired
//    FeignUtil feignUtil;
//
//    /**
//     * 文件上传
//     * @param data
//     * @return
//     * @throws Exception
//     */
//    public String uploadFile(MultipartFile data) throws Exception{
//        return ResultUtil.successWithData(this.uploadSingleFile(data));
//    }
//
//    /**
//     * 批量文件上传
//     * @param list
//     * @return
//     * @throws Exception
//     */
//    public String batchUploadFile(List<MultipartFile> list) throws Exception{
//        List<String> urlList = new ArrayList<>();
//        for (MultipartFile file: list) {
//            urlList.add(this.uploadSingleFile(file));
//        }
//        return ResultUtil.successWithData(urlList);
//    }
//
//
//    /**
//     * 上传单个文件，返回上传成功后的地址
//     * @param multipartFile
//     * @return
//     */
//    private String uploadSingleFile(MultipartFile multipartFile) throws Exception {
//        String fileName = multipartFile.getOriginalFilename();
//        InputStream inputStram = multipartFile.getInputStream();
//        minioClient.putObject(
//                PutObjectArgs.builder().bucket("mogublog").object(fileName).stream(
//                        inputStram, multipartFile.getSize(), -1)
//                        .contentType(multipartFile.getContentType())
//                        .build());
//        String url = "http://101.132.122.175:8080/mogublog/" + fileName;
//        return url;
//    }
//
//
//}
