//package com.moxi.mogublog.picture.service.impl;
//
//
//import com.moxi.mogublog.picture.global.MessageConf;
//import com.moxi.mogublog.picture.global.SysConf;
//import com.moxi.mogublog.picture.service.FileService;
//import com.moxi.mogublog.picture.service.FileSortService;
//import com.moxi.mogublog.picture.service.MinioService;
//import com.moxi.mogublog.picture.util.AboutFileUtil;
//import com.moxi.mogublog.picture.util.FeignUtil;
//import com.moxi.mogublog.utils.*;
//import com.moxi.mougblog.base.enums.EOpenStatus;
//import com.moxi.mougblog.base.exception.exceptionType.InsertException;
//import com.moxi.mougblog.base.global.ErrorCode;
//import com.moxi.mougblog.base.holder.RequestHolder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
///**
// * Minio文件上传实现类
// *
// * @author 陌溪
// * @date 2020年10月19日11:13:21
// */
//@Service
//@Slf4j
//public class MinioServiceImpl implements MinioService {
//
//    @Autowired
//    MinioService minioService;
//
//    @Override
//    public Map<String, List<String>> uploadFile(MultipartFile[] fileList) {
//        return minioService.uploadFile(fileList);
//    }
//}
