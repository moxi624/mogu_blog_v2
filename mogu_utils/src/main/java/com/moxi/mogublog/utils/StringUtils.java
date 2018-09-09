package com.moxi.mogublog.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * 对字符串转换的一些操作
 * @author xuzhixiang
 *
 */
public class StringUtils {
	
	private static int machineId = 1; //集群号
	
	
	
	public static Logger log = Logger.getLogger(StringUtils.class);
	
	/**
	 * 把String 转换为 long
	 * @param str
	 * @param defaultData
	 * @return
	 */
	public static long getLong(String str,Long defaultData) {
		Long lnum = defaultData;
		
		if(isEntity(str)) {
			return lnum;
		}
		try {
			lnum = Long.valueOf(str.trim()).longValue();
		} catch (NumberFormatException e) {
			log.warn("把String 转换为 long======== "+str);
		}
		return lnum;
		
	}
	public static Boolean getBoolean(String str,Boolean defaultData) {
		Boolean lnum = defaultData;
		
		if(isEntity(str)) {
			return lnum;
		}
		try {
			lnum = Boolean.valueOf(str.trim()).booleanValue();
		} catch (NumberFormatException e) {
			log.warn("把String 转换为 long======== "+str);
		}
		return lnum;
		
	}
	/**
	 * 把String转换成int数据
	 * @param str
	 * @param defaultData
	 * @return
	 */
	public static int getInt(String str,Integer defaultData) {
		int inum = defaultData;
		
		if(isEntity(str)) {
			return inum;
		}
		
		try {
			inum =   Integer.valueOf(str.trim()).intValue();
		} catch (NumberFormatException e) {
			log.warn("把String转换成int数据========== "+str);
		}
		return inum;
	}
	/**
	 * 把String转换成double数据
	 * @param str
	 * @param defaultData
	 * @return
	 */
	public static double getDouble(String str,Double defaultData) {
		double dnum = defaultData;
		if(isEntity(str)) {
			return dnum;
		}
		try {
			dnum =   Double.valueOf(str.trim()).doubleValue();
		} catch (NumberFormatException e) {
			log.warn("把String转换成double数据========== "+str);
		}
		return dnum;
	}
	/**
	 * 把String转换成float数据
	 * @param str
	 * @param defaultData
	 * @return
	 */
	public static float getFloat(String str,Float defaultData) {
		float dnum = defaultData;
		if(isEntity(str)) {
			return dnum;
		}
		try {
			dnum =   Float.valueOf(str.trim()).floatValue();
		} catch (NumberFormatException e) {
			log.warn("把String转换成float数据========== "+str);
		}
		return dnum;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param s
	 * @return
	 */
	public static Boolean isEntity(String s) {
		  if(s == null || s.length() <= 0) {
			  return true;
		  }
		return false;
	}
	/**
	 * 按code截取字符串
	 * @return
	 */
	public static String[] split(String str,String code) {
		String[] split;
		if(isEntity(str)) {
			split = null;
		}else {
			split = str.split(code);
		}
		return split;
	}
	
	/**
	 * 把字符串按code 转换为List<Long>
	 * @param str
	 * @return
	 */
	public static List<Long> changeStringToLong(String str,String code){
		String[] split = split(str, code);
		List<Long> lnums =  new ArrayList<>();
		for(String s: split) {
			if(!isEntity(s)) {
				long lnum = getLong(s, 0l);
				lnums.add(lnum);
			}
			
		}
		return lnums;
	}
	/**
	 * 把字符串按code 转换为List<String>
	 * @param str
	 * @return
	 */
	public static List<String> changeStringToString(String str,String code){
		String[] split = split(str, code);
		List<String> lnums =  new ArrayList<>();
		for(String s: split) {
			//long lnum = getLong(s, 0l);
			lnums.add(s);
		}
		return lnums;
	}
	/**
	 * 把字符串按code 转换为List<Long>
	 * @param str
	 * @return
	 */
	public static List<Integer> changeStringToInteger(String str,String code){
		String[] split = split(str, code);
		List<Integer> inums =  new ArrayList<>();
		for(String s: split) {
			int inum = getInt(s, 0);
			inums.add(inum);
		}
		return inums;
	}
	
	
	/**
	 * 生成唯一订单号
	 * @return
	 */
	public static String getOrderNumberByUUID() {
		
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if (hashCodeV < 0) {//有可能是负数
	        hashCodeV = -hashCodeV;
	    }
		String orderNumber=machineId + String.format("%015d", hashCodeV);
		return orderNumber;
		
	}
	
	/**
	 * 生成唯一商户退款单号
	 * @return
	 */
	public static String getOutRefundNoByUUID() {
		
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if (hashCodeV < 0) {//有可能是负数
	        hashCodeV = -hashCodeV;
	    }
		String out_refund_no="BACK"+machineId + String.format("%015d", hashCodeV);
		return out_refund_no;
		
	}
	
	/**
	 * 获取UUID，去掉了-
	 * @author xuzhixiang
	 * @return
	 * @date 2017年9月24日16:16:11
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		log.debug("获取32位的UUID的调试日志-->>" + uuid);
		return uuid;
	}
	
	/**
	 *list小于0的数据就过滤了
	 * 把list的数组变成1，3，4，5，6，
	 * @param stringList
	 * @return
	 */
	public static String listToString(List<Long> list,String code){
	    
		
	    String s = "";
	    if(list==null||list.size()<=0) {
	    	return s;
	    }
	    for (Long l : list) {
	        if(l.longValue()>0) {
	        	s = s +l+code;
	        }
	    }
	    return s;
	}

}
