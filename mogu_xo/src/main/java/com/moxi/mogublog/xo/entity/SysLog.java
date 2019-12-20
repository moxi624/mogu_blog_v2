package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;


/**
 * <p>
 * 操作日志记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Data
@TableName("t_sys_log")
public class SysLog extends SuperEntity<SysLog> {
    /**
     *
     */
    private static final long serialVersionUID = -4851055162892178225L;

    /**
     * 操作用户名
     */
    private String userName;

    /**
     * 操作人uid
     */
    private String adminUid;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式 GET POST
     */
    private String type;

    /**
     * 请求类路径
     */
    private String classPath;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 描述
     */
    private String operation;
}
