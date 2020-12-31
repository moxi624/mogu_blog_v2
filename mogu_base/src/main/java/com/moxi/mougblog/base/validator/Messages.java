package com.moxi.mougblog.base.validator;

/**
 * 错误消息模板
 * @author 陌溪
 * @date 2020年12月31日21:31:58
 */
public interface Messages {
    /**
     * 类内部使用,自定义reject value
     */
    String CK_RANG_MESSAGE_LENGTH_TYPE = "length must be between 0 and 11:%s";
    String CK_NUMERIC_TYPE = "field must be a number:%s";

    /**
     * 注解默认
     */
    String CK_NOT_BLANK_DEFAULT = "can not be blank";
    String CK_NOT_NULL_DEFAULT = "can not be null";
    String CK_NUMERIC_DEFAULT = "must be a number";
    String CK_RANGE_DEFAULT = "should be an integer,between {min} and {max}";
    String ID_NOT_NULL = "can not be null";
    String PAGE_NOT_NULL = "page not be null";
    String SIZE_NOT_NULL = "size not be null";

    String ID_LENGTH_THIRTY_TWO = "length must be 32";
}

