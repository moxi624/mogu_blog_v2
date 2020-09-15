package com.moxi.mougblog.base.exception.exceptionType;

import com.moxi.mougblog.base.global.BaseMessageConf;
import com.moxi.mougblog.base.global.ErrorCode;

import java.io.Serializable;

/**
 * 自定义新增操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:41:19
 */
public class AddException extends RuntimeException implements Serializable {

    /**
     * 异常状态码
     */
    private String code;

    public AddException() {
        super(BaseMessageConf.INSERT_DEFAULT_ERROR);
        this.code = ErrorCode.INSERT_DEFAULT_ERROR;
    }

    public AddException(String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.INSERT_DEFAULT_ERROR;
    }

    public AddException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AddException(String message) {
        super(message);
        this.code = ErrorCode.INSERT_DEFAULT_ERROR;
    }

    public AddException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AddException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
