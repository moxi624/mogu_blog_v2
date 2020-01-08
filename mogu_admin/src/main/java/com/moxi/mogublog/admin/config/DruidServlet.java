package com.moxi.mogublog.admin.config;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.alibaba.druid.support.http.StatViewServlet;
/**
 * DruidServlet配置
 * @author Administrator
 *
 */
@WebServlet(urlPatterns = "/druid/*",
initParams = {
@WebInitParam(name="allow",value = "127.0.0.1"),
        @WebInitParam(name="loginUsername",value = "admin",description = "druid用户名"),
        @WebInitParam(name="loginPassword",value = "houjinye",description = "druid密码"),
        @WebInitParam(name="resetEnable",value = "false")
})
public class DruidServlet extends StatViewServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
