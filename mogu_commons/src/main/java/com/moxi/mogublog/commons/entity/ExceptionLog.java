package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 操作日志异常记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Data
@TableName("t_exception_log")
public class ExceptionLog extends SuperEntity<ExceptionLog> {

    private static final long serialVersionUID = -4851055162892178225L;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 描述
     */
    private String operation;

    /**
     * 参数
     */
    private String params;

    /**
     * 异常对象json格式
     */
    private String exceptionJson;

    /**
     * 异常简单信息,等同于e.getMessage
     */
    private String exceptionMessage;
}
