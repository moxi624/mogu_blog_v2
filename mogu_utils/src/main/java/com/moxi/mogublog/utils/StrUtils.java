package com.moxi.mogublog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * String工具类
 *
 * @author xzx19950624@qq.com
 * @date 2017年9月21日21:41:28
 */
public class StrUtils {

    private static Logger logger = LoggerFactory.getLogger(StrUtils.class);

    /**
     * 获取UUID，去掉了-
     *
     * @return
     * @author xuzhixiang
     * @date 2017年9月24日16:16:11
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        logger.debug("获取32位的UUID的调试日志-->>" + uuid);
        return uuid;
    }

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return
     * @author xuzhixiang
     * @date 2017年9月24日16:21:41
     */
    public static boolean isNotEmpty(String str) {
        if (str == null || str.isEmpty())
            return false;
        return true;
    }

    /**
     * 获取三位数随机字符串
     *
     * @return
     * @author xuzhixiang
     * @date 2017年9月24日16:31:28
     */
    public static String getRandom() {
        int number;
        while (true) {
            number = (int) (Math.random() * 1000);
            if (number >= 100 && number < 1000) {
                break;
            }
        }
        String string = String.valueOf(number);
        logger.debug("获取3位的随机码字符串的调试日志-->>" + string);
        return string;
    }
}
