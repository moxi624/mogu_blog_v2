package com.moxi.mougblog.base.result;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author 陌溪
 * @date 2020年9月9日18:38:44
 */
@Getter
public enum ResultCode {

    /**
     * 规范响应体中的响应码和响应信息
     */
    SUCCESS(200, "操作成功"),

    FAILED(500, "操作失败"),

    VALIDATE_FAILED(501, "参数校验失败"),

    ERROR(502, "未知错误"),

    /**
     * 登录相关的错误提示
     */
    ERROR_LOGIN_NOT_EMPTY(510, "账号或密码不能为空"),
    ERROR_LOGIN_LOCK(511, "密码输错次数过多,已被锁定30分钟");

    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
