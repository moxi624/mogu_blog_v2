package com.moxi.mougblog.base.exception.exceptionType;

import java.io.Serializable;

/**
 * 自定义登录相关的异常
 *
 * @author 陌溪
 * @date 2020年9月9日16:41:32
 */
public class LoginException extends RuntimeException implements Serializable {

    public LoginException() {
        super();
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }

}
