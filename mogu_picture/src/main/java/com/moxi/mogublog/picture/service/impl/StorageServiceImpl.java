package com.moxi.mogublog.picture.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.File;
import com.moxi.mogublog.picture.entity.NetworkDisk;
import com.moxi.mogublog.picture.entity.Storage;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.mapper.StorageMapper;
import com.moxi.mogublog.picture.service.NetworkDiskService;
import com.moxi.mogublog.picture.service.StorageService;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class StorageServiceImpl extends SuperServiceImpl<StorageMapper, Storage> implements StorageService {

    @Autowired
    NetworkDiskService networkDiskService;

    @Autowired
    StorageService storageService;

    @Override
    public void uploadFile(HttpServletRequest request, NetworkDisk networkDisk, List<File> fileList) {
        List<NetworkDisk> networkDiskList = new ArrayList<>();
        Long newStorageSize = 0L;
        for (File file : fileList) {
            NetworkDisk saveNetworkDisk = new NetworkDisk();
            saveNetworkDisk.setAdminUid(request.getAttribute(SysConf.ADMIN_UID).toString());
            saveNetworkDisk.setFilePath(networkDisk.getFilePath());
            saveNetworkDisk.setQiNiuUrl(file.getQiNiuUrl());
            saveNetworkDisk.setLocalUrl(file.getPicUrl());
            saveNetworkDisk.setFileSize(file.getFileSize());
            saveNetworkDisk.setFileName(file.getPicName());
            saveNetworkDisk.setExtendName(file.getPicExpandedName());
            saveNetworkDisk.setFileOldName(file.getFileOldName());
            saveNetworkDisk.setCreateTime(new Date());
            networkDiskList.add(saveNetworkDisk);
            newStorageSize += file.getFileSize();
        }
        networkDiskService.saveBatch(networkDiskList);

        Storage storage = getStorageByAdmin();
        Long storageSize = storage.getStorageSize() + newStorageSize;
        storage.setStorageSize(storageSize);
        storageService.updateById(storage);
    }

    @Override
    public Storage selectStorageBean(Storage storageBean) {
        return null;
    }

    @Override
    public void insertStorageBean(Storage storageBean) {

    }

    @Override
    public void updateStorageBean(Storage storageBean) {

    }

    @Override
    public Storage getStorageByAdmin() {
        HttpServletRequest request = RequestHolder.getRequest();
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        QueryWrapper<Storage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.ADMIN_UID, adminUid);
        queryWrapper.last("LIMIT 1");
        Storage reStorage = storageService.getOne(queryWrapper);
        return reStorage;
    }
}
