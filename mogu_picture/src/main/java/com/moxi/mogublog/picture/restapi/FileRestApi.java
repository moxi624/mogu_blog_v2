package com.moxi.mogublog.picture.restapi;


import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.MinioUtil;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传接口 【总的文件接口，需要调用本地文件、七牛云、Minio上传服务】
 *
 * @author 陌溪
 * @date 2020年10月21日15:32:03
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件服务相关接口", tags = {"文件服务相关接口"})
@Slf4j
public class FileRestApi {

    @Autowired
    MinioUtil minioUtil;
    @Autowired
    private FeignUtil feignUtil;
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "截图上传", notes = "截图上传")
    @RequestMapping(value = "/cropperPicture", method = RequestMethod.POST)
    public String cropperPicture(@RequestParam("file") MultipartFile file) {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(file);
        return fileService.cropperPicture(multipartFileList);
    }

    /**
     * 获取文件的信息接口
     * fileIds 获取文件信息的ids
     * code ids用什么分割的，默认“,”
     *
     * @return
     */
    @ApiOperation(value = "通过fileIds获取图片信息接口", notes = "获取图片信息接口")
    @GetMapping("/getPicture")
    public String getPicture(
            @ApiParam(name = "fileIds", value = "文件ids", required = false) @RequestParam(name = "fileIds", required = false) String fileIds,
            @ApiParam(name = "code", value = "切割符", required = false) @RequestParam(name = "code", required = false) String code) {
        log.info("获取图片信息: {}", fileIds);
        return fileService.getPicture(fileIds, code);
    }

    /**
     * 多文件上传
     * 上传图片接口   传入 userId sysUserId ,有那个传哪个，记录是谁传的,
     * projectName 传入的项目名称如 base 默认是base
     * sortName 传入的模块名， 如 admin，user ,等，不在数据库中记录的是不会上传的
     *
     * @return
     */
    @ApiOperation(value = "多图片上传接口", notes = "多图片上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filedatas", value = "文件数据", required = true),
            @ApiImplicitParam(name = "userUid", value = "用户UID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sysUserId", value = "管理员UID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "projectName", value = "项目名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sortName", value = "模块名", required = false, dataType = "String")
    })
    @PostMapping("/pictures")
    public synchronized Object uploadPics(HttpServletRequest request, List<MultipartFile> filedatas) {
        // 获取系统配置文件
        SystemConfig systemConfig = feignUtil.getSystemConfig();
        return fileService.batchUploadFile(request, filedatas, systemConfig);
    }

    /**
     * 通过URL将图片上传到自己服务器中【主要用于Github和Gitee的头像上传】
     *
     * @param fileVO
     * @param result
     * @return
     */
    @ApiOperation(value = "通过URL上传图片", notes = "通过URL上传图片")
    @PostMapping("/uploadPicsByUrl")
    public Object uploadPicsByUrl(@Validated({GetList.class}) @RequestBody FileVO fileVO, BindingResult result) {
        return fileService.uploadPictureByUrl(fileVO);
    }


    /**
     * Ckeditor图像中的图片上传
     *
     * @return
     */
    @ApiOperation(value = "Ckeditor图像中的图片上传", notes = "Ckeditor图像中的图片上传")
    @RequestMapping(value = "/ckeditorUploadFile", method = RequestMethod.POST)
    public Object ckeditorUploadFile(HttpServletRequest request) {
        return fileService.ckeditorUploadFile(request);
    }

    /**
     * Ckeditor复制的图片上传
     *
     * @return
     */
    @ApiOperation(value = "复制的图片上传", notes = "复制的图片上传")
    @RequestMapping(value = "/ckeditorUploadCopyFile", method = RequestMethod.POST)
    public synchronized Object ckeditorUploadCopyFile() {
        return fileService.ckeditorUploadCopyFile();
    }

    /**
     * Ckeditor工具栏 “插入\编辑超链接”的文件上传
     *
     * @return
     */
    @ApiOperation(value = "工具栏的文件上传", notes = "工具栏的文件上传")
    @RequestMapping(value = "/ckeditorUploadToolFile", method = RequestMethod.POST)
    public Object ckeditorUploadToolFile(HttpServletRequest request) {
        return fileService.ckeditorUploadToolFile(request);
    }
}

