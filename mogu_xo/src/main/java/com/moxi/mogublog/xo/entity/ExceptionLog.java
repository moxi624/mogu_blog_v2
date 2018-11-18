package com.moxi.mogublog.xo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 操作日志异常记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@TableName("t_exception_log")
@SuppressWarnings("rawtypes")
public class ExceptionLog extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851055162892178225L;
	
	@TableId(value = "uid", type = IdType.UUID)
	private String uid; // 唯一uid
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String exceptionJson; //异常对象json格式
	
	private String exceptionMessage;//异常简单信息,等同于e.getMessage
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date happenTime;//操作时间
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getExceptionJson() {
		return exceptionJson;
	}

	public void setExceptionJson(String exceptionJson) {
		this.exceptionJson = exceptionJson;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}
	
	@Override
	protected Serializable pkVal() {
		return this.uid;
	}

}
