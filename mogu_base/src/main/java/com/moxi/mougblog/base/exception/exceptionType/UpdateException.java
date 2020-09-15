package com.moxi.mougblog.base.exception.exceptionType;

import com.moxi.mougblog.base.global.BaseMessageConf;
import com.moxi.mougblog.base.global.ErrorCode;

import java.io.Serializable;

/**
 * 自定义更新操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:41:40
 */
public class UpdateException extends RuntimeException implements Serializable {
    /**
     * 异常状态码
     */
    private String code;

    public UpdateException() {
        super(BaseMessageConf.UPDATE_DEFAULT_ERROR);
        this.code = ErrorCode.UPDATE_DEFAULT_ERROR;
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.UPDATE_DEFAULT_ERROR;
    }

    public UpdateException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public UpdateException(String message) {
        super(message);
        this.code = ErrorCode.UPDATE_DEFAULT_ERROR;
    }

    public UpdateException(String code, String message) {
        super(message);
        this.code = code;
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
