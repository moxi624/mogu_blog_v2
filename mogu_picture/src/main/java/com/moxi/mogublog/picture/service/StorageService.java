package com.moxi.mogublog.picture.service;

import com.moxi.mogublog.commons.entity.NetworkDisk;
import com.moxi.mogublog.commons.entity.Storage;
import com.moxi.mougblog.base.service.SuperService;
import org.springframework.web.multipart.MultipartFile;

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
     * 初始化网盘容量大小
     *
     * @param adminUid
     * @param maxStorageSize
     */
    String initStorageSize(String adminUid, Long maxStorageSize);

    /**
     * 调整网盘容量大小
     *
     * @param adminUid
     * @param maxStorageSize
     */
    String editStorageSize(String adminUid, Long maxStorageSize);

    /**
     * 根据管理员uid列表获取存储容量
     *
     * @param adminUidList
     * @return
     */
    List<Storage> getStorageByAdminUid(List<String> adminUidList);

    /**
     * 上传文件
     *
     * @param networkDisk
     * @param fileList
     */
    String uploadFile(NetworkDisk networkDisk, List<MultipartFile> fileList);

    /**
     * 查询当前用户存储信息
     *
     * @return
     */
    Storage getStorageByAdmin();
}
