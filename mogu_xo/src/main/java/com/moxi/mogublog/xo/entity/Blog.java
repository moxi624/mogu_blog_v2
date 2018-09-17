package com.moxi.mogublog.xo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 博客表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@TableName("t_blog")
public class Blog extends SuperEntity<Blog> {

    private static final long serialVersionUID = 1L;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客简介
     */
    private String summary;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 标签uid
     */
    private String tagUid;

    /**
     * 博客点击数
     */
    private Integer clickCount;

    /**
     * 博客收藏数
     */
    private Integer collectCount;

    /**
     * 标题图片UID
     */
    private String fileUid;

   
    /**
     * 以下字段不存入数据库，封装为了方便使用 
     */
    @TableField(exist = false)
    private List<Tag> tagList; //标签,一篇博客对应多个标签


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
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


	public String getTagUid() {
		return tagUid;
	}


	public void setTagUid(String tagUid) {
		this.tagUid = tagUid;
	}


	public Integer getClickCount() {
		return clickCount;
	}


	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}


	public Integer getCollectCount() {
		return collectCount;
	}


	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}


	public String getFileUid() {
		return fileUid;
	}


	public void setFileUid(String fileUid) {
		this.fileUid = fileUid;
	}


	public List<Tag> getTagList() {
		return tagList;
	}


	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
