package com.moxi.mougblog.base.entity;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * Entity基类
 * @author xuzhixiang
 * @date 2018年9月17日09:47:12
 *
 */
@SuppressWarnings("rawtypes")
public class SuperEntity<T extends Model> extends Model<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4851055162892178225L;
	
	@TableId(value = "uid", type = IdType.UUID)
	private String uid; // 唯一uid	
	
	private int status; // 0 失效  1 生效
	
	/**
	 * @TableField 配置需要填充的字段
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date createTime; //创建时间
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime; //更新时间
	
	public SuperEntity() {
		
		this.status = 1;
		this.createTime = new Date(); 
		this.updateTime = new Date(); 
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.uid;
	}

}