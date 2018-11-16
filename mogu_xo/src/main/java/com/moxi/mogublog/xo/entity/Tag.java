package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@TableName("t_tag")
public class Tag extends SuperEntity<Tag> {

    private static final long serialVersionUID = 1L;


    /**
     * 标签内容
     */
    private String content;


    /**
     * 标签简介
     */
    private  int clickCount;
    
    /**
     * 排序字段，数值越大，越靠前
     */
    private int sort;


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public int getClickCount() {
		return clickCount;
	}


	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public int getSort() {
		return sort;
	}


	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
