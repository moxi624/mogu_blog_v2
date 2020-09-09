package com.moxi.mougblog.base.exception.exceptionType;

import java.io.Serializable;

/**
 * 自定义获取操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:58:07
 */
public class ReadException extends RuntimeException implements Serializable {

    public ReadException() {
        super();
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadException(String message) {
        super(message);
    }

    public ReadException(Throwable cause) {
        super(cause);
    }

}
