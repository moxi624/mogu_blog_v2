package com.moxi.mogublog.xo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 资源分类表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年10月19日20:46:51
 */
@TableName("t_resource_sort")
public class ResourceSort extends SuperEntity<ResourceSort> {

    private static final long serialVersionUID = 1L;

    
    /**
     * 分类名
     */
    private String sortName;
    
    /**
     * 分类介绍
     */
    private String content;
    
    /**
     * 分类图片UID
     */
    private String fileUid;
    
    /**
     * 分类点击数
     */
    private String clickCount;
    
    /**
     * 排序字段，数值越大，越靠前
     */
    private int sort;
    
    @TableField(exist = false)
    private List<String> photoList; //分类图

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	
	public String getFileUid() {
		return fileUid;
	}

	public void setFileUid(String fileUid) {
		this.fileUid = fileUid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public List<String> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<String> photoList) {
		this.photoList = photoList;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
