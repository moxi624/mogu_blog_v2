package com.moxi.mogublog.admin.controller;


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
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.service.AdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/admin")
@Api(value="管理员RestApi",tags={"管理员操作接口"})
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	private static Logger log = LogManager.getLogger(AdminController.class);
	
	@ApiOperation(value="获取管理员列表", notes="获取管理员列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "currentPage", value = "当前页数", required = false, dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示数目", required = false, dataType = "Integer")
    })
	@GetMapping("/getList")
	public String getList(HttpServletRequest request,
			@RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();		
		List<Admin> list = adminService.list(queryWrapper);		
		log.info(list.toString());
		return list.toString();
	}
}

