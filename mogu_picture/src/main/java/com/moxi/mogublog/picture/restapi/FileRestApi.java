package com.moxi.mogublog.picture.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.FileSort;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 图片上传RestApi
 *
 * @author 陌溪
 * @since 2018-09-17
 */
@RestController
@RequestMapping("/file")
@Api(value = "图片服务相关接口", tags = {"图片服务相关接口"})
@Slf4j
public class FileRestApi {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileSortService fileSortService;
    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String path;
    @Autowired
    private QiniuUtil qiniuUtil;
    @Autowired
    private FeignUtil feignUtil;

    @ApiOperation(value = "截图上传", notes = "截图上传")
    @RequestMapping(value = "/cropperPicture", method = RequestMethod.POST)
    public String cropperPicture(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        List<MultipartFile> filedatas = new ArrayList<>();
        filedatas.add(file);
        // 获取七牛云配置文件
        Map<String, String> qiNiuConfig = qiniuUtil.getQiNiuConfig();
        SystemConfig systemConfig = feignUtil.getSystemConfigByMap(qiNiuConfig);
        String qiNiuPictureBaseUrl = qiNiuConfig.get(SysConf.QI_NIU_PICTURE_BASE_URL);
        String localPictureBaseUrl = qiNiuConfig.get(SysConf.LOCAL_PICTURE_BASE_URL);
        String result = fileService.batchUploadFile(request, filedatas, systemConfig);
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if (SysConf.SUCCESS.equals(picMap.get(SysConf.CODE))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get(SysConf.DATA);
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    Map<String, Object> item = new HashMap<>();
                    item.put(SysConf.UID, picData.get(i).get(SysConf.UID));
                    if (EOpenStatus.OPEN.equals(qiNiuConfig.get(SysConf.PICTURE_PRIORITY))) {
                        item.put(SysConf.URL, qiNiuPictureBaseUrl + picData.get(i).get(SysConf.QI_NIU_URL));
                    } else {
                        item.put(SysConf.URL, localPictureBaseUrl + picData.get(i).get(SysConf.PIC_URL));
                    }
                    listMap.add(item);
                }
            }
        }
        return ResultUtil.result(SysConf.SUCCESS, listMap);
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

        if (StringUtils.isEmpty(code)) {
            code = Constants.SYMBOL_COMMA;
        }
        if (StringUtils.isEmpty(fileIds)) {
            log.error(MessageConf.PICTURE_UID_IS_NULL);
            return ResultUtil.result(SysConf.ERROR, MessageConf.PICTURE_UID_IS_NULL);
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> changeStringToString = StringUtils.changeStringToString(fileIds, code);
            QueryWrapper<com.moxi.mogublog.commons.entity.File> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(SQLConf.UID, changeStringToString);
            List<com.moxi.mogublog.commons.entity.File> fileList = fileService.list(queryWrapper);
            if (fileList.size() > 0) {
                for (com.moxi.mogublog.commons.entity.File file : fileList) {
                    if (file != null) {
                        Map<String, Object> remap = new HashMap<>();
                        // 获取七牛云地址
                        remap.put(SysConf.QI_NIU_URL, file.getQiNiuUrl());
                        // 获取本地地址
                        remap.put(SysConf.URL, file.getPicUrl());
                        // 后缀名，也就是类型
                        remap.put(SysConf.EXPANDED_NAME, file.getPicExpandedName());
                        remap.put(SysConf.FILE_OLD_NAME, file.getFileOldName());
                        //名称
                        remap.put(SysConf.NAME, file.getPicName());
                        remap.put(SysConf.UID, file.getUid());
                        remap.put(SQLConf.FILE_OLD_NAME, file.getFileOldName());
                        list.add(remap);
                    }
                }
            }
            return ResultUtil.result(SysConf.SUCCESS, list);
        }
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
        // 获取七牛云配置文件
        Map<String, String> qiNiuConfig = qiniuUtil.getQiNiuConfig();
        SystemConfig systemConfig = feignUtil.getSystemConfigByMap(qiNiuConfig);
        return fileService.batchUploadFile(request, filedatas, systemConfig);
    }


    /**
     * 通过URL将图片上传到自己服务器中【用于Github和Gitee的头像上传】
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

}

