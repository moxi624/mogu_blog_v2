package com.moxi.mogublog.utils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web有关的工具类
 *
 * @author xzx19950624@qq.com
 * @date 2017年9月24日23:27:03
 */
public class WebUtils {

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
        input = input.replaceAll("'", "&#39;");   //IE暂不支持单引号的实体名称,而支持单引号的实体编号,故单引号转义成实体编号,其它字符转义成实体名称
        input = input.replaceAll("\"", "&quot;"); //双引号也需要转义，所以加一个斜线对其进行转义
        input = input.replaceAll("\n", "<br/>");  //不能把\n的过滤放在前面，因为还要对<和>过滤，这样就会导致<br/>失效了
        return input;
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
