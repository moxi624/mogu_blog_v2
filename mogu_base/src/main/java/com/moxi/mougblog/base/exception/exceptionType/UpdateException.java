package com.moxi.mougblog.base.exception.exceptionType;

import java.io.Serializable;

/**
 * 自定义更新操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:41:40
 */
public class UpdateException extends RuntimeException implements Serializable {

    public UpdateException() {
        super();
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }

}
