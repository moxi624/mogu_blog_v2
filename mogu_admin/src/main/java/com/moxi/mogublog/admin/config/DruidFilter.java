package com.moxi.mogublog.admin.config;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import com.alibaba.druid.support.http.WebStatFilter;
/**
 * Druid拦截器配置
 * @author Administrator
 *
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*", initParams = {
		@WebInitParam(name = "exclusions", value = "*.js,*.css,*.ico,/druid/*") })
public class DruidFilter extends WebStatFilter {

}
