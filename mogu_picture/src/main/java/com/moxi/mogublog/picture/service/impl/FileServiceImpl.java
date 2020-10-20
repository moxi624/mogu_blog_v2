package com.moxi.mogublog.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.File;
import com.moxi.mogublog.commons.entity.FileSort;
import com.moxi.mogublog.commons.entity.PictureSort;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.mapper.FileMapper;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.service.LocalFileService;
import com.moxi.mogublog.picture.service.QiniuService;
import com.moxi.mogublog.picture.util.AboutFileUtil;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.*;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.InsertException;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.global.ErrorCode;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import com.moxi.mougblog.base.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 文件服务实现类【包含本地文件服务、七牛云文件服务、Minio文件服务】
 *
 * @author 陌溪
 * @since 2018-09-17
 */
@Slf4j
@Service
public class FileServiceImpl extends SuperServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    FileSortService fileSortService;
    @Autowired
    LocalFileService localFileService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    FeignUtil feignUtil;
    @Autowired
    private FileService fileService;
    @Value(value = "${file.upload.path}")
    private String path;

    @Override
    public String batchUploadFile(HttpServletRequest request, List<MultipartFile> filedatas, SystemConfig systemConfig) {

        String uploadQiNiu = systemConfig.getUploadQiNiu();
        String uploadLocal = systemConfig.getUploadLocal();

        // 判断来源
        String source = request.getParameter(SysConf.SOURCE);
        //如果是用户上传，则包含用户uid
        String userUid = "";
        //如果是管理员上传，则包含管理员uid
        String adminUid = "";
        //项目名
        String projectName = "";
        //模块名
        String sortName = "";

        // 判断图片来源
        if (SysConf.PICTURE.equals(source)) {
            // 当从vue-mogu-web网站过来的，直接从参数中获取
            userUid = request.getParameter(SysConf.USER_UID);
            adminUid = request.getParameter(SysConf.ADMIN_UID);
            projectName = request.getParameter(SysConf.PROJECT_NAME);
            sortName = request.getParameter(SysConf.SORT_NAME);
        } else if (SysConf.ADMIN.equals(source)) {
            // 当图片从mogu-admin传递过来的时候
            userUid = request.getAttribute(SysConf.USER_UID).toString();
            adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
            projectName = request.getAttribute(SysConf.PROJECT_NAME).toString();
            sortName = request.getAttribute(SysConf.SORT_NAME).toString();
        } else {
            userUid = request.getAttribute(SysConf.USER_UID).toString();
            adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
            projectName = request.getAttribute(SysConf.PROJECT_NAME).toString();
            sortName = request.getAttribute(SysConf.SORT_NAME).toString();
        }

        //projectName现在默认base
        if (StringUtils.isEmpty(projectName)) {
            projectName = "base";
        }

        //TODO 检测用户上传，如果不是网站的用户就不能调用
        if (StringUtils.isEmpty(userUid) && StringUtils.isEmpty(adminUid)) {
            return ResultUtil.result(SysConf.ERROR, "请先注册");
        }

        QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, sortName);
        queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<FileSort> fileSorts = fileSortService.list(queryWrapper);

        FileSort fileSort = null;
        if (fileSorts.size() >= 1) {
            fileSort = fileSorts.get(0);
        } else {
            return ResultUtil.result(SysConf.ERROR, "文件不被允许上传");
        }

        String sortUrl = fileSort.getUrl();
        //判断url是否为空，如果为空，使用默认
        if (StringUtils.isEmpty(sortUrl)) {
            sortUrl = "base/common/";
        } else {
            sortUrl = fileSort.getUrl();
        }

        List<File> lists = new ArrayList<>();
        //文件上传
        if (filedatas != null && filedatas.size() > 0) {
            for (MultipartFile filedata : filedatas) {
                String oldName = filedata.getOriginalFilename();
                long size = filedata.getSize();
                //获取扩展名，默认是jpg
                String picExpandedName = FileUtils.getPicExpandedName(oldName);
                //获取新文件名
                String newFileName = System.currentTimeMillis() + Constants.SYMBOL_POINT + picExpandedName;
                String localUrl = "";
                String qiNiuUrl = "";
                try {
                    MultipartFile tempFileData = filedata;
                    // 上传七牛云，判断是否能够上传七牛云
                    if (EOpenStatus.OPEN.equals(uploadQiNiu)) {
                        qiNiuUrl = qiniuService.uploadFile(tempFileData);
                    }

                    // 判断是否能够上传至本地
                    if (EOpenStatus.OPEN.equals(uploadLocal)) {
                        localUrl = localFileService.uploadFile(filedata, fileSort);
                    }
                } catch (Exception e) {
                    log.info("上传文件异常: {}", e.getMessage());
                    e.getStackTrace();
                    return ResultUtil.result(SysConf.ERROR, "文件上传失败，请检查系统配置");
                }

                File file = new File();
                file.setCreateTime(new Date(System.currentTimeMillis()));
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(oldName);
                file.setFileSize(size);
                file.setPicExpandedName(picExpandedName);
                file.setPicName(newFileName);
                file.setPicUrl(localUrl);
                file.setStatus(EStatus.ENABLE);
                file.setUserUid(userUid);
                file.setAdminUid(adminUid);
                file.setQiNiuUrl(qiNiuUrl);
                file.insert();
                lists.add(file);
            }
            //保存成功返回数据
            return ResultUtil.result(SysConf.SUCCESS, lists);
        }
        return ResultUtil.result(SysConf.ERROR, "请上传图片");
    }

    @Override
    public String uploadPictureByUrl(FileVO fileVO) {
        Map<String, String> resultMap = fileVO.getSystemConfig();
        SystemConfig systemConfig = feignUtil.getSystemConfigByMap(resultMap);
        String userUid = fileVO.getUserUid();
        String adminUid = fileVO.getAdminUid();
        String projectName = fileVO.getProjectName();
        String sortName = fileVO.getSortName();
        List<String> urlList = fileVO.getUrlList();
        //projectName现在默认base
        if (StringUtils.isEmpty(projectName)) {
            projectName = "base";
        }
        //这里可以检测用户上传，如果不是网站的用户或会员就不能调用
        if (StringUtils.isEmpty(userUid) && StringUtils.isEmpty(adminUid)) {
            throw new InsertException(ErrorCode.INSERT_DEFAULT_ERROR, "请先注册");
        }
        QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, sortName);
        queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<FileSort> fileSorts = fileSortService.list(queryWrapper);
        FileSort fileSort = null;
        if (fileSorts.size() > 0) {
            fileSort = fileSorts.get(0);
        } else {
            throw new InsertException(ErrorCode.INSERT_DEFAULT_ERROR, "文件不被允许上传, 请填写文件分类信息");
        }
        List<com.moxi.mogublog.commons.entity.File> lists = new ArrayList<>();
        //文件上传
        if (urlList != null && urlList.size() > 0) {
            for (String itemUrl : urlList) {
                //获取新文件名(默认为jpg)
                String newFileName = System.currentTimeMillis() + ".jpg";
                // 将图片上传到本地服务器中以及七牛云中
                String picurl = "";
                String qiNiuUrl = "";
                // 判断是否能够上传至本地
                if (EOpenStatus.OPEN.equals(systemConfig.getUploadLocal())) {
                    picurl = localFileService.uploadPictureByUrl(itemUrl, fileSort);
                }
                // 上传七牛云，判断是否能够上传七牛云
                if (EOpenStatus.OPEN.equals(systemConfig.getUploadQiNiu())) {
                    qiNiuUrl = qiniuService.uploadPictureByUrl(itemUrl, systemConfig);
                }
                com.moxi.mogublog.commons.entity.File file = new com.moxi.mogublog.commons.entity.File();
                file.setCreateTime(new Date(System.currentTimeMillis()));
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(itemUrl);
                file.setFileSize(0L);
                file.setPicExpandedName("jpg");
                file.setPicName(newFileName);
                file.setPicUrl(picurl);
                file.setQiNiuUrl(qiNiuUrl);
                file.setStatus(EStatus.ENABLE);
                file.setUserUid(userUid);
                file.setAdminUid(adminUid);
                fileService.save(file);
                lists.add(file);
            }
            //保存成功返回数据
            return ResultUtil.result(SysConf.SUCCESS, lists);
        }
        return ResultUtil.result(SysConf.ERROR, "请上传图片");
    }

    @Override
    public Object ckeditorUploadFile(HttpServletRequest request) {
        String token = request.getParameter(SysConf.TOKEN);
        // 从Redis中获取七牛云配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getSystemConfigMap(token);
        SystemConfig systemConfig = feignUtil.getSystemConfigByMap(qiNiuResultMap);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        //引用自己设计的一个工具类
        AboutFileUtil af = new AboutFileUtil();
        // 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile file = multiRequest.getFile(iter.next());
            if (file != null) {
                //获取旧名称
                String oldName = file.getOriginalFilename();
                //获取扩展名
                String expandedName = FileUtils.getPicExpandedName(oldName);
                //判断是否是图片
                if (!af.isPic(expandedName)) {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "请上传正确的图片");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

                //对图片大小进行限制
                if (file.getSize() > (10 * 1024 * 1024)) {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "图片大小不能超过10M");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

                // 设置图片上传服务必要的信息
                request.setAttribute(SysConf.USER_UID, SysConf.DEFAULT_UID);
                request.setAttribute(SysConf.ADMIN_UID, SysConf.DEFAULT_UID);
                request.setAttribute(SysConf.PROJECT_NAME, SysConf.BLOG);
                request.setAttribute(SysConf.SORT_NAME, SysConf.ADMIN);

                List<MultipartFile> fileData = new ArrayList<>();
                fileData.add(file);
                String result = fileService.batchUploadFile(request, fileData, systemConfig);
                Map<String, Object> resultMap = JsonUtils.jsonToMap(result);
                String code = resultMap.get(SysConf.CODE).toString();
                if (SysConf.SUCCESS.equals(code)) {
                    List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get(SysConf.DATA);
                    if (resultList.size() > 0) {
                        Map<String, Object> picture = resultList.get(0);
                        String fileName = picture.get(SysConf.PIC_NAME).toString();
                        map.put(SysConf.UPLOADED, 1);
                        map.put(SysConf.FILE_NAME, fileName);
                        // 设置显示方式
                        if (EOpenStatus.OPEN.equals(systemConfig.getPicturePriority())) {
                            String qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
                            String qiNiuUrl = picture.get(SysConf.QI_NIU_URL).toString();
                            map.put(SysConf.URL, qiNiuPictureBaseUrl + qiNiuUrl);
                        } else {
                            String localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
                            // 设置图片服务根域名
                            String url = localPictureBaseUrl + picture.get(SysConf.PIC_URL).toString();
                            map.put(SysConf.URL, url);
                        }
                    }
                } else {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "上传失败");
                    map.put(SysConf.ERROR, errorMap);
                }
            }
        }
        return map;
    }

    @Override
    public String ckeditorUploadCopyFile() {
        return null;
    }
}
