package com.moxi.mogublog.picture.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 文件实体类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
@TableName("t_file")
public class File extends SuperEntity<File> {

    private static final long serialVersionUID = 1L;

    private String fileOldName;

    private Long fileSize;

    private String fileSortUid;

    private String picExpandedName; // 图片扩展名

    private String picName; //图片名称

    private String picUrl; //图片url地址

    private String adminUid;

    private String userUid;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getFileOldName() {
        return fileOldName;
    }

    public void setFileOldName(String fileOldName) {
        this.fileOldName = fileOldName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSortUid() {
        return fileSortUid;
    }

    public void setFileSortUid(String fileSortUid) {
        this.fileSortUid = fileSortUid;
    }

    public String getPicExpandedName() {
        return picExpandedName;
    }

    public void setPicExpandedName(String picExpandedName) {
        this.picExpandedName = picExpandedName;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

}
