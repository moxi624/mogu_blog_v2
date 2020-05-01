package com.moxi.mogublog.picture.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.service.QiniuService;
import com.moxi.mogublog.picture.util.Aboutfile;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.*;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ckeditor")
@Slf4j
public class CkEditorRestApi {

    /**
     * 获取基本路径
     */
    @Value(value = "${file.upload.path}")
    private String basePath;

    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String path;

    @Autowired
    FileSortService fileSortService;

    @Autowired
    FileService fileService;

    @Autowired
    AdminFeignClient adminFeignClient;

    @Autowired
    FeignUtil feignUtil;

    @Autowired
    QiniuService qiniuService;

    /**
     * 图像中的图片上传
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public Object imgUpload(HttpServletRequest request) throws IOException {
        return qiniuService.imgUpload(request);
    }

    /**
     * 复制的图片上传
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/imgUploadByUrl", method = RequestMethod.POST)
    public synchronized Object imgUploadByUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return qiniuService.uploadImgByUrl();
    }

    /**
     * 工具栏“插入\编辑超链接”的文件上传
     *
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public Object fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return qiniuService.fileUpload(request);
    }
}
