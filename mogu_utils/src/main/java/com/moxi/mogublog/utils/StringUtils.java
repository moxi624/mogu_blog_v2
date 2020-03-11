package com.moxi.mogublog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 对字符串转换的一些操作
 *
 * @author xzx19950624@qq.com
 */
public class StringUtils {

    public static Logger log = LoggerFactory.getLogger(StringUtils.class);
    private static int machineId = 1; //集群号

    /**
     * 把String 转换为 long
     *
     * @param str
     * @param defaultData
     * @return
     */
    public static long getLong(String str, Long defaultData) {
        Long lnum = defaultData;

        if (isEmpty(str)) {
            return lnum;
        }
        try {
            lnum = Long.valueOf(str.trim()).longValue();
        } catch (NumberFormatException e) {
            log.warn("把String 转换为 long======== " + str);
        }
        return lnum;

    }

    public static Boolean getBoolean(String str, Boolean defaultData) {
        Boolean lnum = defaultData;

        if (isEmpty(str)) {
            return lnum;
        }
        try {
            lnum = Boolean.valueOf(str.trim()).booleanValue();
        } catch (NumberFormatException e) {
            log.warn("把String 转换为 long======== " + str);
        }
        return lnum;

    }

    /**
     * 把String转换成int数据
     *
     * @param str
     * @param defaultData
     * @return
     */
    public static int getInt(String str, Integer defaultData) {
        int inum = defaultData;

        if (isEmpty(str)) {
            return inum;
        }

        try {
            inum = Integer.valueOf(str.trim()).intValue();
        } catch (NumberFormatException e) {
            log.warn("把String转换成int数据========== " + str);
        }
        return inum;
    }

    /**
     * 把String转换成double数据
     *
     * @param str
     * @param defaultData
     * @return
     */
    public static double getDouble(String str, Double defaultData) {
        double dnum = defaultData;
        if (isEmpty(str)) {
            return dnum;
        }
        try {
            dnum = Double.valueOf(str.trim()).doubleValue();
        } catch (NumberFormatException e) {
            log.warn("把String转换成double数据========== " + str);
        }
        return dnum;
    }

    /**
     * 把String转换成float数据
     *
     * @param str
     * @param defaultData
     * @return
     */
    public static float getFloat(String str, Float defaultData) {
        float dnum = defaultData;
        if (isEmpty(str)) {
            return dnum;
        }
        try {
            dnum = Float.valueOf(str.trim()).floatValue();
        } catch (NumberFormatException e) {
            log.warn("把String转换成float数据========== " + str);
        }
        return dnum;
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static Boolean isEmpty(String s) {
        if (s == null || s.length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * 按code截取字符串
     *
     * @return
     */
    public static String[] split(String str, String code) {
        String[] split;
        if (isEmpty(str)) {
            split = null;
        } else {
            split = str.split(code);
        }
        return split;
    }

    /**
     * 把字符串按code 转换为List<Long>
     *
     * @param str
     * @return
     */
    public static List<Long> changeStringToLong(String str, String code) {
        String[] split = split(str, code);
        List<Long> lnums = new ArrayList<>();
        for (String s : split) {
            if (!isEmpty(s)) {
                long lnum = getLong(s, 0l);
                lnums.add(lnum);
            }

        }
        return lnums;
    }

    /**
     * 把字符串按code 转换为List<String>
     *
     * @param str
     * @return
     */
    public static List<String> changeStringToString(String str, String code) {
        String[] split = split(str, code);
        List<String> lnums = new ArrayList<>();
        for (String s : split) {
            //long lnum = getLong(s, 0l);
            lnums.add(s);
        }
        return lnums;
    }

    /**
     * 把字符串按code 转换为List<Long>
     *
     * @param str
     * @return
     */
    public static List<Integer> changeStringToInteger(String str, String code) {
        String[] split = split(str, code);
        List<Integer> inums = new ArrayList<>();
        for (String s : split) {
            int inum = getInt(s, 0);
            inums.add(inum);
        }
        return inums;
    }


    /**
     * 生成唯一订单号
     *
     * @return
     */
    public static String getOrderNumberByUUID() {

        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        String orderNumber = machineId + String.format("%015d", hashCodeV);
        return orderNumber;

    }

    /**
     * 生成唯一商户退款单号
     *
     * @return
     */
    public static String getOutRefundNoByUUID() {

        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        String out_refund_no = "BACK" + machineId + String.format("%015d", hashCodeV);
        return out_refund_no;

    }

    /**
     * 获取UUID，去掉了-
     *
     * @return
     * @author xuzhixiang
     * @date 2017年9月24日16:16:11
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        log.debug("获取32位的UUID的调试日志-->>" + uuid);
        return uuid;
    }

    /**
     * list小于0的数据就过滤了
     * 把list的数组变成1，3，4，5，6
     *
     * @param list
     * @param code
     * @return
     */
    public static String listToString(List<Long> list, String code) {


        String s = "";
        if (list == null || list.size() <= 0) {
            return s;
        }
        for (Long l : list) {
            if (l.longValue() > 0) {
                s = s + l + code;
            }
        }
        return s;
    }

    /**
     * 按code把list的数组转换成字符串
     *
     * @param list
     * @param code
     * @return
     */
    public static String listTranformString(List<String> list, String code) {


        String s = "";
        if (list == null || list.size() <= 0) {
            return s;
        }
        s = String.join(code, list);
        return s;
    }

    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            //把字符串强制转换为数字
            Integer.valueOf(str);
            //如果是数字，返回True
            return true;
        } catch (Exception e) {
            //如果抛出异常，返回False
            return false;
        }
    }

    /**
     * 某个子串是否在字符串内
     *
     * @param str
     * @param searchChar
     * @return
     */
    public static boolean contains(String str, String searchChar) {
        if (isEmpty(str)) {
            return false;
        }
        return str.indexOf(searchChar) >= 0;
    }

    /**
     * 切割字符串
     *
     * @param str
     * @param start
     * @return
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }

        return str.substring(start);
    }

    /**
     * 判断评论是否为垃圾评论（仅通过单一字符重复出现来判断，以后可以扩展更多的检测方法）
     *
     * @param content
     * @return
     */
    public static Boolean isCommentSpam(String content) {
        if (content == null) {
            return true;
        }
        char[] chars = content.toCharArray();
        // 最大重复次数
        Integer maxCount = 4;
        for (int a = 0; a < chars.length; a++) {
            Integer count = 1;
            for (int b = a; b < chars.length - 1; b++) {
                if (chars[b + 1] == chars[b]) {
                    count++;
                    // 判断字符重复的次数是否大于阈值
                    if (count >= maxCount) {
                        return true;
                    }
                    continue;
                } else {
                    break;
                }
            }
        }
        return false;
    }
}
