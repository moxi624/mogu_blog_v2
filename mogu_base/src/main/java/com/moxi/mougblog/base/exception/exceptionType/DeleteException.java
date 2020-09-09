package com.moxi.mougblog.base.exception.exceptionType;

import java.io.Serializable;

/**
 * 自定义删除操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:41:26
 */
public class DeleteException extends RuntimeException implements Serializable {

    public DeleteException() {
        super();
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(Throwable cause) {
        super(cause);
    }

}
