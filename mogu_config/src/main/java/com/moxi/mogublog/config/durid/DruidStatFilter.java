//package com.moxi.mogublog.config.durid;
//
//import com.alibaba.druid.support.http.WebStatFilter;
//
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.annotation.WebInitParam;
//
///**
// * druid过滤器.
// *
// * @author: xuzhixiang
// * @date: 2018年8月2日09:19:05 
// */
//
//@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
//        initParams = {
//                @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
//        }
//)
//public class DruidStatFilter extends WebStatFilter {
//
//}