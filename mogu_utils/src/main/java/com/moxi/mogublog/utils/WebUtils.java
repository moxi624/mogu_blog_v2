package com.moxi.mogublog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * web有关的工具类
 *
 * @author xzx19950624@qq.com
 * @date 2017年9月24日23:27:03
 */
@Slf4j
public class WebUtils {

    // 一级域名
    private static final String RE_TOP = "(\\w*\\.?){1}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";

    // 二级域名提取
    private static final String RE_TOP_2 = "(\\w*\\.?){2}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";

    // 三级域名提取
    private static final String RE_TOP_3 = "(\\w*\\.?){3}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";

    /**
     * HTML字符转义
     * <p>
     * * @return String 过滤后的字符串
     * * @see 对输入参数中的敏感字符进行过滤替换,防止用户利用JavaScript等方式输入恶意代码
     * * @see String input = <img src='http://t1.baidu.com/it/fm=0&gp=0.jpg'/>
     * * @see HtmlUtils.htmlEscape(input);         //from spring.jar
     * * @see StringEscapeUtils.escapeHtml(input); //from commons-lang.jar
     * * @see 尽管Spring和Apache都提供了字符转义的方法,但Apache的StringEscapeUtils功能要更强大一些
     * * @see StringEscapeUtils提供了对HTML,Java,JavaScript,SQL,XML等字符的转义和反转义
     * * @see 但二者在转义HTML字符时,都不会对单引号和空格进行转义,而本方法则提供了对它们的转义
     *
     * @param input
     * @return
     */
    public static String htmlEscape(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        input = input.replaceAll("&", "&amp;");
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll(" ", "&nbsp;");

        //IE暂不支持单引号的实体名称,而支持单引号的实体编号,故单引号转义成实体编号,其它字符转义成实体名称
        input = input.replaceAll("'", "&#39;");

        //双引号也需要转义，所以加一个斜线对其进行转义
        input = input.replaceAll("\"", "&quot;");

        //不能把\n的过滤放在前面，因为还要对<和>过滤，这样就会导致<br/>失效了
        input = input.replaceAll("\n", "<br/>");

        return input;
    }

    /**
     * 获取URL的 一级，二级，三级域名
     * @param url
     * @param level：域名等级
     * @return
     */
    public static String getDomainName(String url, Integer level) {
        Pattern pattern;
        switch (level) {
            case 1: {pattern = Pattern.compile(RE_TOP , Pattern.CASE_INSENSITIVE);};break;
            case 2: {pattern = Pattern.compile(RE_TOP_2 , Pattern.CASE_INSENSITIVE);};break;
            case 3: {pattern = Pattern.compile(RE_TOP_3 , Pattern.CASE_INSENSITIVE);};break;
            default:{
                log.error("传入level有误");
                return null;
            }
        }
        String result = "";
        try {
            Matcher matcher = pattern.matcher(url);
            matcher.find();
            result = matcher.group();
        } catch (Exception e) {
            log.info("获取域名出错");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 格式化数据获取图片列表
     *
     * @param result
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getPicture(String result) {

        List<String> picUrls = new ArrayList<>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(picMap.get("code"))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get("data");
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    picUrls.add((String) picData.get(i).get("url"));
                }
            }
        }
        return picUrls;
    }

    /**
     * 获取图片，返回Map
     *
     * @author xzx19950624@qq.com
     * @date 2018年10月21日下午12:55:18
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getPictureMap(String result) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(picMap.get("code"))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get("data");
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (StringUtils.isEmpty(picData.get(i).get("url")) || StringUtils.isEmpty(picData.get(i).get("uid"))) {
                        continue;
                    }
                    map.put("url", (String) picData.get(i).get("url"));
                    map.put("uid", (String) picData.get(i).get("uid"));
                    resultList.add(map);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取结果集的内容
     *
     * @param result
     * @return
     */
    public static <T> T getData(String result, Class<T> beanType) {
        if (com.moxi.mogublog.utils.StringUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(dataMap.get("code"))) {

            Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
            T t = JsonUtils.mapToPojo(data, beanType);
            return t;
        }
        return null;
    }

    /**
     * 获取结果集的内容，返回的是 List<POJO>
     *
     * @param result
     * @return
     */
    public static <T> List<T> getList(String result, Class<T> beanType) {
        if (com.moxi.mogublog.utils.StringUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(dataMap.get("code"))) {

            Map<String, Object> data = (Map<String, Object>) dataMap.get("data");

            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("records");
            List<T> resultList = new ArrayList<>();
            list.forEach(item -> {
                resultList.add(JsonUtils.mapToPojo(item, beanType));
            });
            return resultList;
        }
        return null;
    }
}
