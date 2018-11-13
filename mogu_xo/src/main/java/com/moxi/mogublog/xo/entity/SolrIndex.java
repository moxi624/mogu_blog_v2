package com.moxi.mogublog.xo.entity;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 用于建立solr索引实体类
 * @author limboy
 * @create 2018-09-29 16:12
 */
public class SolrIndex {
    /**
     * 博客uid
     */
    @Field("id")
    private String id;
    
    /**
     * 图片Uid
     */
    @Field("file_uid")
    private String fileUid;

    /**
     * 博客标题
     */
    @Field("blog_title")
    private String title;

    /**
     * 博客简介
     */
    @Field("blog_summary")
    private String summary;

    /**
     * 标签名
     */
    @Field("blog_tag")
    private String tag;

    /**
     * 博客分类名
     */
    @Field("blog_sort")
    private String blogSort;

    /**
     * 如果原创，作者为管理员名
     */
    @Field("blog_author")
    private String author;

    /**
     * 文章更新时间
     */
    @Field("blog_updateTime")
    private Date updateTime;
    
    /**
     * 以下字段不存入solr索引库中
     * 
     */
    @Field("photo_list")
    private String photoList; //标题图

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBlogSort() {
        return blogSort;
    }

    public void setBlogSort(String blogSort) {
        this.blogSort = blogSort;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getFileUid() {
		return fileUid;
	}

	public void setFileUid(String fileUid) {
		this.fileUid = fileUid;
	}

	public String getPhotoList() {
		return photoList;
	}

	public void setPhotoList(String photoList) {
		this.photoList = photoList;
	}

	@Override
    public String toString() {
        return "SolrIndex [id=" + id + ", title=" + title + ", summary=" + summary + ", tag=" + tag + ", blogSort="
                + blogSort + ", author=" + author + ", updateTime=" + updateTime + "]";
    }

}
