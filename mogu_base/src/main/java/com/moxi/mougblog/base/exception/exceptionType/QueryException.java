package com.moxi.mougblog.base.exception.exceptionType;

import com.moxi.mougblog.base.global.BaseMessageConf;
import com.moxi.mougblog.base.global.ErrorCode;

import java.io.Serializable;

/**
 * 自定义查询操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:58:07
 */
public class QueryException extends RuntimeException implements Serializable {

    /**
     * 异常状态码
     */
    private String code;

    public QueryException() {
        super(BaseMessageConf.QUERY_DEFAULT_ERROR);
        this.code = ErrorCode.QUERY_DEFAULT_ERROR;
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.QUERY_DEFAULT_ERROR;
    }

    public QueryException(String message) {
        super(message);
        this.code = ErrorCode.QUERY_DEFAULT_ERROR;
    }

    public QueryException(String code, String message) {
        super(message);
        this.code = code;
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
