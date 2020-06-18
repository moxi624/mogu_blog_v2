package com.moxi.mogublog.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.File;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.picture.entity.NetworkDisk;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.mapper.FileMapper;
import com.moxi.mogublog.picture.mapper.NetworkDiskMapper;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.service.NetworkDiskService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.MoGuFileUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.picture.vo.NetworkDiskVO;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.utils.upload.FileUtil;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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
public class NetworkDiskServiceImpl extends SuperServiceImpl<NetworkDiskMapper, NetworkDisk> implements NetworkDiskService {

    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String UPLOAD_PATH;

    @Autowired
    NetworkDiskService networkDiskService;

    @Autowired
    FeignUtil feignUtil;

    @Autowired
    QiniuUtil qiniuUtil;

    @Override
    public void insertFile(NetworkDisk networkDisk) {
        networkDisk.insert();
    }

    @Override
    public void batchInsertFile(List<NetworkDisk> fileBeanList) {

    }

    @Override
    public void updateFile(NetworkDisk fileBean) {

    }

    @Override
    public NetworkDisk selectFileById(NetworkDisk fileBean) {
        return null;
    }

    @Override
    public List<NetworkDisk> selectFilePathTreeByUserid(NetworkDisk fileBean) {
        return null;
    }

    @Override
    public List<NetworkDisk> selectFileList(NetworkDisk networkDisk) {
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        // 获取配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getQiNiuConfig(request.getAttribute(SysConf.TOKEN).toString());
        String picturePriority = qiNiuResultMap.get(SysConf.PICTURE_PRIORITY);

        QueryWrapper<NetworkDisk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        // 根据扩展名查找
        if(networkDisk.getFileType() != 0) {
            queryWrapper.in(SQLConf.EXTEND_NAME, FileUtil.getFileExtendsByType(networkDisk.getFileType()));
        } else if(StringUtils.isNotEmpty(networkDisk.getFilePath())) {
            // 没有扩展名时，查找全部
            queryWrapper.eq(SQLConf.FILE_PATH, networkDisk.getFilePath());
        }
        List<NetworkDisk> list = networkDiskService.list(queryWrapper);
        list.forEach(item -> {
            if(EOpenStatus.OPEN.equals(picturePriority)) {
                item.setFileUrl(qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL) + item.getQiNiuUrl());
            } else {
                item.setFileUrl(qiNiuResultMap.get(SysConf.LOCAL_PICTURE_BASE_URL) + item.getLocalUrl());
            }
        });
        return list;
    }

    @Override
    public List<NetworkDisk> selectFileListByIds(List<Integer> fileidList) {
        return null;
    }

    @Override
    public List<NetworkDisk> selectFileTreeListLikeFilePath(String filePath) {
        return null;
    }

    @Override
    public void deleteFile(NetworkDiskVO networkDiskVO, Map<String, String> qiNiuConfig) {
        String uid = networkDiskVO.getUid();
        if(StringUtils.isNotEmpty(uid)) {

        }
        NetworkDisk networkDisk = networkDiskService.getById(uid);
        String uploadLocal = qiNiuConfig.get(SysConf.UPLOAD_LOCAL);
        String uploadQiNiu = qiNiuConfig.get(SysConf.UPLOAD_QI_NIU);

        // 修改为删除状态
        networkDisk.setStatus(EStatus.DISABLED);
        networkDisk.updateById();

        // 判断删除的是文件 or 文件夹
        if(SysConf.ONE == networkDisk.getIsDir()) {
            // 删除的是文件夹，那么需要把文件下所有的文件获得，进行删除
            // 获取文件的路径，查询出该路径下所有的文件
            String path = networkDisk.getFilePath() + networkDisk.getFileName();
            QueryWrapper<NetworkDisk> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            // 查询以  path%  开头的
            queryWrapper.likeRight(SQLConf.FILE_PATH, path);
            List<NetworkDisk> list = networkDiskService.list(queryWrapper);

            if(list.size() > 0) {
                // 将所有的状态设置成失效
                list.forEach(item -> {
                    item.setStatus(EStatus.DISABLED);
                });
                Boolean isUpdateSuccess = networkDiskService.updateBatchById(list);
                if(isUpdateSuccess) {

                    // 删除本地文件，同时移除本地文件
                    if (EOpenStatus.OPEN.equals(uploadLocal)) {
                        // 获取删除的路径
                        List<String> fileList = new ArrayList<>();
                        list.forEach(item -> {
                            fileList.add(UPLOAD_PATH + item.getLocalUrl());
                        });
                        // 批量删除本地图片
                        MoGuFileUtil.deleteFileList(fileList);
                    }
                    // 删除七牛云上文件
                    if (EOpenStatus.OPEN.equals(uploadQiNiu)) {
                        List<String> fileList = new ArrayList<>();
                        list.forEach(item -> {
                            fileList.add(item.getQiNiuUrl());
                        });
                        qiniuUtil.deleteFileList(fileList, qiNiuConfig);
                    }
                }
            }
        } else {
            // TODO 以后这里可以写成定时器，而不是马上删除，增加回收站的功能
            // 删除本地文件，同时移除本地文件
            if (EOpenStatus.OPEN.equals(uploadLocal)) {
                String localUrl = networkDisk.getLocalUrl();
                MoGuFileUtil.deleteFile(UPLOAD_PATH + localUrl);
            }
            // 删除七牛云上文件
            if (EOpenStatus.OPEN.equals(uploadQiNiu)) {
                String qiNiuUrl = networkDisk.getQiNiuUrl();
                qiniuUtil.deleteFile(qiNiuUrl, qiNiuConfig);
            }
        }
    }

    @Override
    public void deleteFileByIds(List<Integer> fileidList) {

    }

    @Override
    public void updateFilepathByFilepath(String oldfilepath, String newfilepath, String filename, String extendname) {

    }

    @Override
    public List<NetworkDisk> selectFileByExtendName(List<String> filenameList, String adminUid) {
        return null;
    }
}
