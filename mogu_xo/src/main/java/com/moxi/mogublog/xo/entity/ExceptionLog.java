package com.moxi.mogublog.xo.entity;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 操作日志异常记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@TableName("t_exception_log")
public class ExceptionLog extends SuperEntity<ExceptionLog>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851055162892178225L;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String exceptionJson; //异常对象json格式
	
	private String exceptionMessage;//异常简单信息,等同于e.getMessage
	
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

}
