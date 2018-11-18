package com.moxi.mogublog.xo.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <p>
 * 操作日志记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@TableName("t_sys_log")
@SuppressWarnings("rawtypes")
public class SysLog extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851055162892178225L;
	
	@TableId(value = "uid", type = IdType.UUID)
	private String uid; // 唯一uid
	
	private String userName;//操作账户名
	
	private String ip; //请求IP
    
    private String url; //请求地址
    
    private String type; //请求方式 GET POST
	
    private String classPath; //请求类路径
    
	private String method;//方法名

    private String params;//参数

    private String operation;//描述
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logDate;//操作时间
    
	public String getUid() {
		return uid;
	}



	public void setUid(String uid) {
		this.uid = uid;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getClassPath() {
		return classPath;
	}



	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}



	public String getMethod() {
		return method;
	}



	public void setMethod(String method) {
		this.method = method;
	}



	public String getParams() {
		return params;
	}



	public void setParams(String params) {
		this.params = params;
	}



	public String getOperation() {
		return operation;
	}



	public void setOperation(String operation) {
		this.operation = operation;
	}



	public Date getLogDate() {
		return logDate;
	}



	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}



	@Override
	protected Serializable pkVal() {
		return this.uid;
	}

}
