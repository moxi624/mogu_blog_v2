package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * 图片实体类
 * @author xuzhixiang
 * @date 2018年9月17日16:08:58
 *
 */

@TableName("t_picture")
public class Picture extends SuperEntity<Picture>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2646201532621057453L;
	

	private String fileUid; //图片的UID

	private String picName; //图片名称

	private String pictureSortUid; //所属相册分类id
	
    /**
     * 以下字段不存入数据库，封装为了方便使用 
     */
    @TableField(exist = false)
    private String pictureUrl;  //图片路径


	public String getFileUid() {
		return fileUid;
	}

	public void setFileUid(String fileUid) {
		this.fileUid = fileUid;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPictureSortUid() {
		return pictureSortUid;
	}

	public void setPictureSortUid(String pictureSortUid) {
		this.pictureSortUid = pictureSortUid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
}
