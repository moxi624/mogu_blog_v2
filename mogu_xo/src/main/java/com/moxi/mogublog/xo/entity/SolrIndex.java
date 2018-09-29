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
    private String uid;

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
     * 标签uid
     */
    @Field("blog_tag")
    private String tag;

    /**
     * 博客分类UID
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    @Override
    public String toString() {
        return "SolrIndex [uid=" + uid + ", title=" + title + ", summary=" + summary + ", tag=" + tag + ", blogSort="
                + blogSort + ", author=" + author + ", updateTime=" + updateTime + "]";
    }

}
