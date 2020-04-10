package com.moxi.mogublog.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.File;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.mapper.FileMapper;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.*;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
@Slf4j
@Service
public class FileServiceImpl extends SuperServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    FileSortService fileSortService;

    @Override
    public String uploadImgs(String path, HttpServletRequest request, List<MultipartFile> filedatas, Map<String, String> qiNiuConfig) {

        String uploadQiNiu = qiNiuConfig.get(SysConf.UPLOAD_QI_NIU);
        String uploadLocal = qiNiuConfig.get(SysConf.UPLOAD_LOCAL);

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
        if(SysConf.PICTURE.equals(source)) {
            // 当从vue-mogu-web网站过来的，直接从参数中获取
            userUid = request.getParameter(SysConf.USER_UID);
            adminUid = request.getParameter(SysConf.ADMIN_UID);
            projectName = request.getParameter(SysConf.PROJECT_NAME);
            sortName = request.getParameter(SysConf.SORT_NAME);
        } else if(SysConf.ADMIN.equals(source)) {
            // 当图片从mogu-admin传递过来的时候
            userUid = request.getAttribute(SysConf.USER_UID).toString();
            adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
            projectName = request.getAttribute(SysConf.PROJECT_NAME).toString();
            sortName = request.getAttribute(SysConf.SORT_NAME).toString();
        }
        else {
            userUid = request.getAttribute(SysConf.USER_UID).toString();
            adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
            projectName = request.getAttribute(SysConf.PROJECT_NAME).toString();
            sortName = request.getAttribute(SysConf.SORT_NAME).toString();
        }

        //projectName现在默认base
        if (StringUtils.isEmpty(projectName)) {
            projectName = "base";
        }

        //这里可以检测用户上传，如果不是网站的用户就不能调用
        if (StringUtils.isEmpty(userUid) && StringUtils.isEmpty(adminUid)) {
            return ResultUtil.result(SysConf.ERROR, "请先注册");
        } else {

        }

        QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, sortName);
        queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<FileSort> fileSorts = fileSortService.list(queryWrapper);

        System.out.println("fileSorts" + JsonUtils.objectToJson(fileSorts));

        FileSort fileSort = null;
        if (fileSorts.size() > 0) {
            fileSort = fileSorts.get(0);
            log.info("====fileSort====" + JsonUtils.objectToJson(fileSort));
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

        List<com.moxi.mogublog.picture.entity.File> lists = new ArrayList<>();
        //文件上传
        if (filedatas != null && filedatas.size() > 0) {
            for (MultipartFile filedata : filedatas) {

                String oldName = filedata.getOriginalFilename();
                long size = filedata.getSize();

                //以前的文件名
                log.info("上传文件====：" + oldName);

                //文件大小
                log.info("文件大小====：" + size);

                //获取扩展名，默认是jpg
                String picExpandedName = FileUtils.getPicExpandedName(oldName);

                //获取新文件名
                String newFileName = String.valueOf(System.currentTimeMillis() + "." + picExpandedName);
                log.info(newFileName + ":" + oldName);

                //文件路径问题
                log.info("path====" + path);
                String newPath = path + sortUrl + "/" + picExpandedName + "/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";

                String picurl = sortUrl + "/" + picExpandedName + "/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/" + newFileName;
                log.info("newPath====" + newPath);
                String saveUrl = newPath + newFileName;

                BufferedOutputStream out = null;
                QiniuUtil qn = new QiniuUtil();
                String qiNiuUrl = "";
                List<String> list = new ArrayList<>();
                java.io.File dest= null;
                java.io.File saveFile = null;
                try {

                    // 判断是否能够上传至本地
                    if("1".equals(uploadLocal)) {
                        // 保存本地，创建目录
                        java.io.File file1 = new java.io.File(newPath);
                        if (!file1.exists()) {
                            file1.mkdirs();
                        }
                        saveFile = new java.io.File(saveUrl);
                    }

                    MultipartFile tempData = filedata;

                    // 上传七牛云，判断是否能够上传七牛云
                    if("1".equals(uploadQiNiu)) {
                        // 创建一个临时目录文件
                        String tempFiles = "temp/" + newFileName;
                        dest = new java.io.File(tempFiles);
                        if (!dest.getParentFile().exists()) {
                            dest.getParentFile().mkdirs();
                        }
                        out = new BufferedOutputStream(new FileOutputStream(dest));
                        out.write(filedata.getBytes());
                        out.flush();
                        out.close();
                        qiNiuUrl = qn.uploadQiniu(dest, qiNiuConfig);
                    }

                    // 判断是否能够上传至本地
                    if("1".equals(uploadLocal)) {
                        // 序列化文件到本地
                        saveFile.createNewFile();
                        tempData.transferTo(saveFile);
                    }


                }catch (QiniuException e) {
                    log.info("==上传七牛云异常===url:" + saveUrl + "-----");
                    log.error(e.getMessage());
                    return ResultUtil.result(SysConf.ERROR, "七牛云配置有误");
                } catch (Exception e) {
                    log.info("==上传文件异常===url:" + saveUrl + "-----");
                    log.error(e.getMessage());
                    return ResultUtil.result(SysConf.ERROR, "文件上传失败");
                } finally{
                    if (dest != null && dest.getParentFile().exists()) {
                        dest.delete();
                    }
                }

                com.moxi.mogublog.picture.entity.File file = new com.moxi.mogublog.picture.entity.File();
                file.setCreateTime(new Date(System.currentTimeMillis()));
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(oldName);
                file.setFileSize(size);
                file.setPicExpandedName(picExpandedName);
                file.setPicName(newFileName);
                file.setPicUrl(picurl);
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
}
