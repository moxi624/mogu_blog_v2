package com.moxi.mogublog.picture.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * @author: 网盘文件实体类
 * @create: 2020年6月13日17:03:05
 */
@TableName("t_network_disk")
@Data
public class NetworkDisk extends SuperEntity<NetworkDisk> {

    /**
     * 管理员UID
     */
    private String adminUid;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 时间戳名称
     */
    private String timestampName;

    /**
     * 扩展名
     */
    private String extendName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 是否是目录
     */
    private int isDir;

    /**
     * 以下字段不存入数据库
     */

    @TableField(exist = false)
    private String oldFilePath;

    @TableField(exist = false)
    private String newFilePath;

    @TableField(exist = false)
    private String files;

    @TableField(exist = false)
    private int fileType;
}
