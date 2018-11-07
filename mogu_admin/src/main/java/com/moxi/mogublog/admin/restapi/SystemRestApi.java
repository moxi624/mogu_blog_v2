package com.moxi.mogublog.admin.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.service.AdminService;

import io.swagger.annotations.Api;

/**
 * 系统设置RestApi
 * @author xzx19950624@qq.com
 * @date 2018年11月6日下午8:23:36
 */

@RestController
@RequestMapping("/system")
@Api(value="系统设置RestApi",tags={"SystemRestApi"})
public class SystemRestApi {
	
	@Autowired
	AdminService adminService;
	
	/**
	 * 获取关于我的信息
	 * @author xzx19950624@qq.com
	 * @date 2018年11月6日下午8:57:48
	 */
	public String getMe() {
				
		Admin admin = adminService.getById("5821462bc29a4570ad80e87f3aa3f02d");
		
		return ResultUtil.result(SysConf.SUCCESS, admin);
	}
	
	
}
