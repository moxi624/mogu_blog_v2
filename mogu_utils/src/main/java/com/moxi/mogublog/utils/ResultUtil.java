package com.moxi.mogublog.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回统一接口
 *
 * @author 陌溪
 * 2018年9月9日19:22:20
 */
public class ResultUtil {
    final static String CODE = "code";
    final static String SUCCESS = "success";
    final static String ERROR = "error";
    final static String DATA = "data";
    final static String MESSAGE = "message";
    final static int NUM_TWO = 2;

    /**
     * @param code success error
     * @param data 返回的数据
     * @return String
     */
    public static String result(Object code, Object data) {
        return resultWithData(code, data);
    }

    /**
     * 返回结果【只携带数据】
     *
     * @param code success error
     * @param data 返回的数据
     * @return String
     */
    public static String resultWithData(Object code, Object data) {
        Map<Object, Object> map = new HashMap<>(NUM_TWO);
        map.put(CODE, code);
        map.put(DATA, data);
        return JsonUtils.objectToJson(map);
    }

    /**
     * 返回结果【只携带消息】
     *
     * @param code success error
     * @return String
     */
    public static String resultWithMessage(Object code, String message) {
        Map<Object, Object> map = new HashMap<>(NUM_TWO);
        map.put(CODE, code);
        map.put(MESSAGE, message);
        return JsonUtils.objectToJson(map);
    }

    /**
     * 返回结果【携带数据和消息】
     *
     * @param code
     * @param data
     * @param message
     * @return
     */
    public static String resultWithDataAndMessage(Object code, Object data, String message) {
        Map<Object, Object> map = new HashMap<>(NUM_TWO);
        map.put(CODE, code);
        map.put(DATA, data);
        map.put(MESSAGE, message);
        return JsonUtils.objectToJson(map);
    }

    /**
     * 返回成功的结果【只携带数据】
     *
     * @param data
     * @return
     */
    public static String successWithData(Object data) {
        return resultWithData(SUCCESS, data);
    }

    /**
     * 返回成功的结果【只携带消息】
     *
     * @param message
     * @return
     */
    public static String successWithMessage(String message) {
        return resultWithMessage(SUCCESS, message);
    }

    /**
     * 返回成功的结果【携带数据和消息】
     *
     * @param message
     * @return
     */
    public static String successWithDataAndMessage(Object data, String message) {
        return resultWithDataAndMessage(SUCCESS, data, message);
    }

    /**
     * 返回失败的结果【只携带消息】
     *
     * @param message
     * @return
     */
    public static String errorWithMessage(String message) {
        return resultWithMessage(ERROR, message);
    }

    /**
     * 返回失败的结果【只携带数据】
     *
     * @param data
     * @return
     */
    public static String errorWithData(Object data) {
        return resultWithData(ERROR, data);
    }
}
