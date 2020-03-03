package com.moxi.mogublog.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回统一接口
 *
 * @author xzx19950624@qq.com
 * 2018年9月9日19:22:20
 */
public class ResultUtil {
    /**
     * @param code success error
     * @param data 返回的数据
     * @return String
     * @author xuzhixiang
     * 2018年9月9日19:22:16
     */
    public static String result(Object code, Object data) {
        Map<Object, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("data", data);
        return JsonUtils.objectToJson(map);
    }

}
