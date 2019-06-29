package com.moxi.mogublog.admin.restapi;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Todo;
import com.moxi.mogublog.xo.service.TodoService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 待办事项表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
@RestController
@Api(value="代办事项RestApi",tags={"LinkRestApi"})
@RequestMapping("/todo")
public class TodoRestApi {
	
	@Autowired
	TodoService todoService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取代办事项列表", notes="获取代办事项列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Todo> queryWrapper = new QueryWrapper<Todo>();
		
		if(StringUtils.isNotEmpty(keyword) && !StringUtils.isEmpty(keyword.trim())) {
			queryWrapper.like(SQLConf.TEXT, keyword.trim());
		}
		
		String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
		
		if(adminUid.length() < 32) {
			return ResultUtil.result(SysConf.ERROR, "请重新登录!"); 
		}
		
		queryWrapper.eq(SQLConf.ADMINUID, adminUid);		
		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME); //按时间顺序倒排
		
		Page<Todo> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		IPage<Todo> pageList = todoService.page(page, queryWrapper);
		log.info("返回内容");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@OperationLogger(value="增加代办事项")
	@ApiOperation(value="增加代办事项", notes="增加代办事项", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "text", value = "代办事项",required = false) @RequestParam(name = "text", required = false) String text) {		
		
		if(StringUtils.isEmpty(text)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();		
		Todo todo = new Todo();
		todo.setText(text);	
		todo.setDone(false); //默认未完成
		todo.setAdminUid(adminUid);
		todo.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@OperationLogger(value="编辑代办事项")
	@ApiOperation(value="编辑代办事项", notes="编辑代办事项", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "done", value = "是否完成",required = false) @RequestParam(name = "done", required = false) Boolean done,
			@ApiParam(name = "text", value = "代办事项",required = false) @RequestParam(name = "text", required = false) String text) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
		
		if(adminUid.length() < 32) {
			return ResultUtil.result(SysConf.ERROR, "请重新登录!"); 
		}
		
		Todo todo = todoService.getById(uid);
		
		if(!todo.getAdminUid().equals(adminUid)) {
			return ResultUtil.result(SysConf.ERROR, "您无权编辑该内容!");
		}
		
		todo.setText(text);
		todo.setDone(done);
		todo.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@OperationLogger(value="删除代办事项")
	@ApiOperation(value="删除代办事项", notes="删除代办事项", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
		
		if(adminUid.length() < 32) {
			return ResultUtil.result(SysConf.ERROR, "请重新登录!"); 
		}
		
		Todo todo = todoService.getById(uid);
		if(!todo.getAdminUid().equals(adminUid)) {
			return ResultUtil.result(SysConf.ERROR, "您无权删除该内容!");
		}
		todo.setStatus(EStatus.DISABLED);		
		todo.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
	@OperationLogger(value="批量编辑代办事项")
	@ApiOperation(value="批量编辑代办事项", notes="批量编辑代办事项", response = String.class)
	@PostMapping("/toggleAll")
	public String toggleAll(HttpServletRequest request,
			@ApiParam(name = "done", value = "是否完成",required = true) @RequestParam(name = "done", required = true) Boolean done) {
		
		String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
		
		if(adminUid.length() < 32) {
			return ResultUtil.result(SysConf.ERROR, "请重新登录!"); 
		}
		
		if(done) {
			todoService.toggleAll(1, adminUid);	
		} else {
			todoService.toggleAll(0, adminUid);
		}		
		return ResultUtil.result(SysConf.SUCCESS, "批量更新成功");
	}
	

}

