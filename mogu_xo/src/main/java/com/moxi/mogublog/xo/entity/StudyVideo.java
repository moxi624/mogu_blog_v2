package com.moxi.mogublog.xo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 学习视频表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年10月19日20:46:51
 */
@TableName("t_resource_sort")
public class StudyVideo extends SuperEntity<StudyVideo> {

    private static final long serialVersionUID = 1L;

    
    /**
     * 视频名称
     */
    private String name;
    
    /**
     * 视频简介
     */
    private String summary;
    
    /**
     * 视频内容介绍
     */
    private String content;
       
    /**
     * 百度云完整路径
     */
    private String baiduPath;
    
    /**
     * 视频封面图片UID
     */
    private String fileUid;
    
    /**
     * 资源分类UID
     */
    private String resourceSortUid;
    
    /**
     * 点击数
     */
    private String clickCount;
    
    @TableField(exist = false)
    private List<String> photoList; //学习视频标题图

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBaiduPath() {
		return baiduPath;
	}

	public void setBaiduPath(String baiduPath) {
		this.baiduPath = baiduPath;
	}

	public String getFileUid() {
		return fileUid;
	}

	public void setFileUid(String fileUid) {
		this.fileUid = fileUid;
	}

	public String getResourceSortUid() {
		return resourceSortUid;
	}

	public void setResourceSortUid(String resourceSortUid) {
		this.resourceSortUid = resourceSortUid;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<String> photoList) {
		this.photoList = photoList;
	}
}
