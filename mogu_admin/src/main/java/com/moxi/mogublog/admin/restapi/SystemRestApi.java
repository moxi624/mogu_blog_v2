package com.moxi.mogublog.admin.restapi;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.service.AdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	/**
	 * 获取关于我的信息
	 * @author xzx19950624@qq.com
	 * @date 2018年11月6日下午8:57:48
	 */
	
	@ApiOperation(value="获取我的信息", notes="获取我的信息")
	@GetMapping("/getMe")
	public String getMe() {
				
		Admin admin = adminService.getById("5821462bc29a4570ad80e87f3aa3f02d");
		admin.setPassWord(""); //清空密码，防止泄露
		
		//获取图片
		if(StringUtils.isNotEmpty(admin.getAvatar())) {
			String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), ",");
			admin.setPhotoList(WebUtils.getPicture(pictureList));
		}
		
		return ResultUtil.result(SysConf.SUCCESS, admin);
	}
	
	@ApiOperation(value="编辑我的信息", notes="获取我的信息")
	@PostMapping("/editMe")
	public String editMe(HttpServletRequest request, @RequestBody Admin admin) {
		
		Boolean save = adminService.updateById(admin);
		return ResultUtil.result(SysConf.SUCCESS, save);
	}
	
	
}
