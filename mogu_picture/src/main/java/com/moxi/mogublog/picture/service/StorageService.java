package com.moxi.mogublog.picture.service;

import com.moxi.mogublog.picture.entity.File;
import com.moxi.mogublog.picture.entity.NetworkDisk;
import com.moxi.mogublog.picture.entity.Storage;
import com.moxi.mougblog.base.service.SuperService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 存储信息服务类
 * </p>
 *
 * @author 陌溪
 * @since 2020年6月13日17:22:32
 */
public interface StorageService extends SuperService<Storage> {

    /**
     * 上传文件
     *
     * @param request  请求
     * @param fileList 文件列表
     */
    void uploadFile(HttpServletRequest request, NetworkDisk networkDisk, List<File> fileList);

    /**
     * 查询存储信息
     *
     * @param storageBean
     * @return
     */
    Storage selectStorageBean(Storage storageBean);

    /**
     * 添加存储信息
     *
     * @param storageBean
     */
    void insertStorageBean(Storage storageBean);

    /**
     * 更新存储信息
     *
     * @param storageBean
     */
    void updateStorageBean(Storage storageBean);

    /**
     * 通过Admin查询存储信息
     *
     * @return
     */
    Storage getStorageByAdmin();
}
