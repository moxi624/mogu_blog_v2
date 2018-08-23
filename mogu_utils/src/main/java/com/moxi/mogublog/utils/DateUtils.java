package com.moxi.mogublog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 时间日期工具类
 * @author xuzhixiang
 * @date 2017年9月24日16:33:29
 *
 */
public class DateUtils {
	private static Logger logger = LogManager.getLogger(StrUtils.class);
	
	/**
	 * 将当前时间格式化成yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception
	 */
    public static String getTime() throws Exception{
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        logger.debug("当前时间格式化的调试日志-->>" + time);
        return time;
    }
    
    /**
     * 将当前时间格式化成yyyy-MM-dd
     * @return
     * @throws Exception
     */
    public static String getDate() throws Exception {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(date);
        logger.debug("当前时间格式化的调试日志-->>" + time);
        return time;
    }
    
    /**
     * 将输入时间格式化,格式化的格式yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     * @throws Exception
     */
    public static String timeToString(Date date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        logger.debug("输入时间格式化的调试日志-->>" + time);
        return time;
    }
    
    
    
    /**
     * 将输入时间格式化,格式化的格式yyyy-MM-dd
     * @param date
     * @return
     * @throws Exception
     */
    public static String dateToString(Date date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(date);
        logger.debug("输入时间格式化的调试日志-->>" + time);
        return time;
    }

}
