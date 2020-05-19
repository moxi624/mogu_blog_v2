package com.moxi.mogublog.picture.restapi;

import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.service.QiniuService;
import com.moxi.mogublog.picture.util.FeignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/ckeditor")
@Api(value = "Ckeditor相关接口", tags = {"Ckeditor相关接口"})
@Slf4j
public class CkEditorRestApi {

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
     * 获取基本路径
     */
    @Value(value = "${file.upload.path}")
    private String basePath;
    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String path;

    /**
     * 图像中的图片上传
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "图像中的图片上传", notes = "图像中的图片上传")
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
    @ApiOperation(value = "复制的图片上传", notes = "复制的图片上传")
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
    @ApiOperation(value = "工具栏的文件上传", notes = "工具栏的文件上传")
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public Object fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return qiniuService.fileUpload(request);
    }
}
