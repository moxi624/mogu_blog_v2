package com.moxi.mougblog.base.exception.exceptionType;

import java.io.Serializable;

/**
 * 自定义新增操作相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:41:19
 */
public class AddException extends RuntimeException implements Serializable {

    public AddException() {
        super();
    }

    public AddException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddException(String message) {
        super(message);
    }

    public AddException(Throwable cause) {
        super(cause);
    }

}
