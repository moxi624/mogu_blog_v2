package com.moxi.mogublog.xo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 网站配置表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月11日14:54:12
 */
@TableName("t_web_config")
public class WebConfig extends SuperEntity<WebConfig> {

    private static final long serialVersionUID = 1L;

    
    /**
     *	网站Logo 
     */
    private String logo;    
    
    /**
     * 网站名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private  String summary;

    /**
     * 关键字
     */
    private String keyword;
    
    /**
     * 作者
     */
    private String author;
	
    /**
     * 备案号
     */
    private String recordNum;
    
    /**
     * 支付宝收款码FileId
     */
    private String aliPay;
    
    /**
     * 微信收款码FileId
     */
    private String weixinPay;
    
    /**
     * 是否开启评论(0:否， 1:是)
     */
    private String startComment;
    
    /**
     * 以下字段不存入数据库，封装为了方便使用
     */    
    @TableField(exist = false)
    private List<String> photoList; //标题图
    
    @TableField(exist = false)
    private String aliPayPhoto; //支付宝付款码
    
    @TableField(exist = false)
    private String weixinPayPhoto; //标题图
    

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}

	public String getStartComment() {
		return startComment;
	}

	public void setStartComment(String startComment) {
		this.startComment = startComment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<String> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<String> photoList) {
		this.photoList = photoList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAliPay() {
		return aliPay;
	}

	public void setAliPay(String aliPay) {
		this.aliPay = aliPay;
	}

	public String getAliPayPhoto() {
		return aliPayPhoto;
	}

	public void setAliPayPhoto(String aliPayPhoto) {
		this.aliPayPhoto = aliPayPhoto;
	}

	public String getWeixinPayPhoto() {
		return weixinPayPhoto;
	}

	public void setWeixinPayPhoto(String weixinPayPhoto) {
		this.weixinPayPhoto = weixinPayPhoto;
	}

	public String getWeixinPay() {
		return weixinPay;
	}

	public void setWeixinPay(String weixinPay) {
		this.weixinPay = weixinPay;
	}
	
}
