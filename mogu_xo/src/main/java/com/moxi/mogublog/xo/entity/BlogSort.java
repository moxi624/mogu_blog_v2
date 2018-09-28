package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 博客分类表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年9月24日15:12:45
 */
@TableName("t_blog_sort")
public class BlogSort extends SuperEntity<BlogSort> {

    private static final long serialVersionUID = 1L;


    /**
     * 分类内容
     */
    private String content;
    
    /**
     * 分类简介
     */
    private String doc;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
