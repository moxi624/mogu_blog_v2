package com.moxi.mogublog.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.NetworkDisk;
import com.moxi.mogublog.commons.entity.Storage;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.mapper.NetworkDiskMapper;
import com.moxi.mogublog.picture.service.NetworkDiskService;
import com.moxi.mogublog.picture.service.StorageService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.MinioUtil;
import com.moxi.mogublog.picture.util.MoGuFileUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.picture.vo.NetworkDiskVO;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.upload.FileUtil;
import com.moxi.mougblog.base.enums.EFilePriority;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.UpdateException;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件服务实现类
 *
 * @author
 * @since 2018-09-17
 */
@Slf4j
@RefreshScope
@Service
public class NetworkDiskServiceImpl extends SuperServiceImpl<NetworkDiskMapper, NetworkDisk> implements NetworkDiskService {

    @Autowired
    private NetworkDiskService networkDiskService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FeignUtil feignUtil;
    @Autowired
    private QiniuUtil qiniuUtil;
    @Autowired
    private MinioUtil minioUtil;
    @Value(value = "${file.upload.path}")
    private String UPLOAD_PATH;

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
    public List<NetworkDisk> selectFilePathTree() {
        QueryWrapper<NetworkDisk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_DIR, SysConf.ONE);
        return networkDiskService.list(queryWrapper);
    }

    @Override
    public List<NetworkDisk> selectFileList(NetworkDisk networkDisk) {
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        // 获取配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getSystemConfigMap(request.getAttribute(SysConf.TOKEN).toString());
        String picturePriority = qiNiuResultMap.get(SysConf.PICTURE_PRIORITY);
        QueryWrapper<NetworkDisk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByAsc(SQLConf.CREATE_TIME);
        // 根据扩展名查找
        if (networkDisk.getFileType() != 0) {
            // 判断是否是其它文件
            if (FileUtil.OTHER_TYPE == networkDisk.getFileType()) {
                queryWrapper.notIn(SQLConf.EXTEND_NAME, FileUtil.getFileExtendsByType(networkDisk.getFileType()));
            } else {
                queryWrapper.in(SQLConf.EXTEND_NAME, FileUtil.getFileExtendsByType(networkDisk.getFileType()));
            }
        } else if (StringUtils.isNotEmpty(networkDisk.getFilePath())) {
            // 没有扩展名时，查找全部
            queryWrapper.eq(SQLConf.FILE_PATH, networkDisk.getFilePath());
        }
        List<NetworkDisk> list = networkDiskService.list(queryWrapper);
        list.forEach(item -> {
            if (EFilePriority.QI_NIU.equals(picturePriority)) {
                item.setFileUrl(qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL) + item.getQiNiuUrl());
            } else if (EFilePriority.MINIO.equals(picturePriority)) {
                item.setFileUrl(qiNiuResultMap.get(SysConf.MINIO_PICTURE_BASE_URL) + item.getMinioUrl());
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
        if (StringUtils.isNotEmpty(uid)) {
            log.error("删除的文件不能为空");
        }
        NetworkDisk networkDisk = networkDiskService.getById(uid);
        String uploadLocal = qiNiuConfig.get(SysConf.UPLOAD_LOCAL);
        String uploadQiNiu = qiNiuConfig.get(SysConf.UPLOAD_QI_NIU);
        String uploadMinio = qiNiuConfig.get(SysConf.UPLOAD_MINIO);

        // 修改为删除状态
        networkDisk.setStatus(EStatus.DISABLED);
        networkDisk.updateById();

        // 判断删除的是文件 or 文件夹
        if (SysConf.ONE == networkDisk.getIsDir()) {
            // 删除的是文件夹，那么需要把文件下所有的文件获得，进行删除
            // 获取文件的路径，查询出该路径下所有的文件
            String path = networkDisk.getFilePath() + networkDisk.getFileName();
            QueryWrapper<NetworkDisk> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            // 查询以  path%  开头的
            queryWrapper.likeRight(SQLConf.FILE_PATH, path);
            List<NetworkDisk> list = networkDiskService.list(queryWrapper);

            if (list.size() > 0) {
                // 将所有的状态设置成失效
                list.forEach(item -> {
                    item.setStatus(EStatus.DISABLED);
                });
                Boolean isUpdateSuccess = networkDiskService.updateBatchById(list);
                if (isUpdateSuccess) {

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

                    // 删除Minio中的文件
                    if (EOpenStatus.OPEN.equals(uploadMinio)) {
                        List<String> fileList = new ArrayList<>();
                        list.forEach(item -> {
                            fileList.add(item.getMinioUrl());
                        });
                        minioUtil.deleteBatchFile(fileList);
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

            // 删除Minio中的文件
            if (EOpenStatus.OPEN.equals(uploadMinio)) {
                String minioUrl = networkDisk.getMinioUrl();
                if (StringUtils.isNotEmpty(minioUrl)) {
                    String[] minUrlArray = minioUrl.split("/");
                    // 找到文件名
                    minioUtil.deleteFile(minUrlArray[minUrlArray.length - 1]);
                } else {
                    log.error("删除的文件不存在Minio文件地址");
                }
            }

            Storage storage = storageService.getStorageByAdmin();
            long storageSize = 0L;
            try {
                storageSize = storage.getStorageSize() - networkDisk.getFileSize();
            } catch (Exception e) {
                log.info("本地文件空间不存在!");
            }
            storage.setStorageSize(storageSize > 0 ? storageSize : 0L);
            storageService.updateById(storage);
        }
    }

    @Override
    public void deleteFileByIds(List<Integer> fileidList) {

    }

    @Override
    public void updateFilepathByFilepath(NetworkDiskVO networkDiskVO) {
        String oldFilePath = networkDiskVO.getOldFilePath();
        String newFilePath = networkDiskVO.getNewFilePath();
        String fileName = networkDiskVO.getFileName();
        String fileOldName = networkDiskVO.getFileOldName();
        String extendName = networkDiskVO.getExtendName();

        if ("null".equals(networkDiskVO.getExtendName())) {
            extendName = null;
        }
        // 判断移动的路径是否相同【拼接出原始目录】
        String fileOldPath = oldFilePath + fileOldName + "/";
        if (fileOldPath.equals(newFilePath)) {
            throw new UpdateException("不能选择自己");
        }
        //移动根目录
        QueryWrapper<NetworkDisk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.FILE_PATH, networkDiskVO.getOldFilePath());
        queryWrapper.eq(SQLConf.FILE_NAME, networkDiskVO.getFileName());
        if (StringUtils.isNotEmpty(extendName)) {
            queryWrapper.eq(SQLConf.EXTEND_NAME, extendName);
        } else {
            queryWrapper.isNull(SQLConf.EXTEND_NAME);
        }
        List<NetworkDisk> networkDiskList = networkDiskService.list(queryWrapper);
        for (NetworkDisk networkDisk : networkDiskList) {
            // 修改新的路径
            networkDisk.setFilePath(newFilePath);
            // 修改旧文件名
            networkDisk.setFileOldName(networkDiskVO.getFileOldName());
            // 如果扩展名为空，代表是文件夹，还需要修改文件名
            if (StringUtils.isEmpty(extendName)) {
                networkDisk.setFileName(networkDiskVO.getFileOldName());
            }
        }

        if (networkDiskList.size() > 0) {
            networkDiskService.updateBatchById(networkDiskList);
        }

        //移动子目录
        oldFilePath = oldFilePath + fileName + "/";
        newFilePath = newFilePath + fileOldName + "/";

        oldFilePath = oldFilePath.replace("\\", "\\\\\\\\");
        oldFilePath = oldFilePath.replace("'", "\\'");
        oldFilePath = oldFilePath.replace("%", "\\%");
        oldFilePath = oldFilePath.replace("_", "\\_");

        //为null说明是目录，则需要移动子目录
        if (extendName == null) {
            //移动根目录
            QueryWrapper<NetworkDisk> childQueryWrapper = new QueryWrapper<>();
            childQueryWrapper.likeRight(SQLConf.FILE_PATH, oldFilePath);
            List<NetworkDisk> childList = networkDiskService.list(childQueryWrapper);
            for (NetworkDisk networkDisk : childList) {
                String filePath = networkDisk.getFilePath();
                networkDisk.setFilePath(filePath.replace(oldFilePath, newFilePath));
            }
            if (childList.size() > 0) {
                networkDiskService.updateBatchById(childList);
            }
        }
    }

    @Override
    public List<NetworkDisk> selectFileByExtendName(List<String> filenameList, String adminUid) {
        return null;
    }
}
