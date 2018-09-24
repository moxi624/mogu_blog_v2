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
     * 博客分类UID
     */
    private String blogSortUid;

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
     * 管理员UID
     */
    private String adminUid;
    
    /**
     * 是否原创
     */
    private String isOriginal;
    
    /**
     * 如果原创，作者为管理员名
     */
    private String author;

    /**
     * 文章出处
     */
    private String articlesPart;
    
    /**
     * 以下字段不存入数据库，封装为了方便使用 
     */
    @TableField(exist = false)
    private List<Tag> tagList; //标签,一篇博客对应多个标签
    
    @TableField(exist = false)
    private List<String> photoList; //标题图
    
    @TableField(exist = false)
    private BlogSort blogSort; //博客分类


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


	public List<String> getPhotoList() {
		return photoList;
	}


	public void setPhotoList(List<String> photoList) {
		this.photoList = photoList;
	}


	public String getAdminUid() {
		return adminUid;
	}


	public void setAdminUid(String adminUid) {
		this.adminUid = adminUid;
	}

	public String getIsOriginal() {
		return isOriginal;
	}


	public void setIsOriginal(String isOriginal) {
		this.isOriginal = isOriginal;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getArticlesPart() {
		return articlesPart;
	}


	public void setArticlesPart(String articlesPart) {
		this.articlesPart = articlesPart;
	}


	public String getBlogSortUid() {
		return blogSortUid;
	}


	public void setBlogSortUid(String blogSortUid) {
		this.blogSortUid = blogSortUid;
	}


	public BlogSort getBlogSort() {
		return blogSort;
	}


	public void setBlogSort(BlogSort blogSort) {
		this.blogSort = blogSort;
	}
	
}
