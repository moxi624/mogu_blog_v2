package com.moxi.mougblog.base.exception.exceptionType;

/**
 * 自定义业务异常
 *
 * @author 陌溪
 * @date 2019年12月4日22:47:32
 */
public class BusinessException extends RuntimeException {

    /**
     * 异常编码
     */
    private String code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
