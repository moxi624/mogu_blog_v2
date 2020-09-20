package com.moxi.mougblog.base.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * HP基类
 *
 * @author 陌溪
 * @date 2017年9月29日19:47:44
 */
@Component
public class BaseHP implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取一个map
     *
     * @return
     */
    public static Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    /**
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BaseHP.applicationContext == null) {
            BaseHP.applicationContext = applicationContext;
        }
    }


    /**
     * 通过name获取 Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
