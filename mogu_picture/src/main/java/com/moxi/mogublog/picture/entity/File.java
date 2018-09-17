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

    private Long fileSortId;

    private String picExpandedName;

    private String picName;

    private String picUrl;

    private String sysUserId;

    private String userId;


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

    public Long getFileSortId() {
        return fileSortId;
    }

    public void setFileSortId(Long fileSortId) {
        this.fileSortId = fileSortId;
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

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
