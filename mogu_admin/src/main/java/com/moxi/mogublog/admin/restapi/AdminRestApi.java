package com.moxi.mogublog.admin.restapi;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.AdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 管理员表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/admin")
@Api(value="管理员RestApi",tags={"AdminRestApi"})
public class AdminRestApi {
	
	@Autowired
	AdminService adminService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取管理员列表", notes="获取管理员列表")
	@GetMapping("/getList")
	public String getList(HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
		Page<Admin> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		IPage<Admin> pageList = adminService.page(page, queryWrapper);
		List<Admin> list = pageList.getRecords();
		log.info(list.toString());
		return ResultUtil.result(SysConf.SUCCESS, list);
	}
	
	@ApiOperation(value="用户登录", notes="用户登录")
	@GetMapping("/login")
	public String login(HttpServletRequest request, 
			@ApiParam(name = "username", value = "用户名", required = true) @RequestParam(name = "username", required = true) String username,
			@ApiParam(name = "password", value = "密码", required = true) @RequestParam(name = "password", required = true) String password) {
		if(username.equals("admin") && password.equals("admin")) {
			return ResultUtil.result(SysConf.SUCCESS, "success");
		}
		return ResultUtil.result(SysConf.ERROR, "error");
	}
}

