package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Todo;
import com.moxi.mogublog.xo.service.TodoService;
import com.moxi.mogublog.xo.vo.TodoVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 待办事项表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
@RestController
@Api(value = "代办事项RestApi", tags = {"LinkRestApi"})
@RequestMapping("/todo")
@Slf4j
public class TodoRestApi {

    @Autowired
    TodoService todoService;

    @ApiOperation(value = "获取代办事项列表", notes = "获取代办事项列表", response = String.class)
    @PostMapping("/getList")
    public String getList(HttpServletRequest request, @Validated({GetList.class}) @RequestBody TodoVO todoVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(todoVO.getKeyword()) && !StringUtils.isEmpty(todoVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.TEXT, todoVO.getKeyword().trim());
        }

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        queryWrapper.eq(SQLConf.ADMINUID, adminUid);

        //按时间顺序倒排
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        Page<Todo> page = new Page<>();
        page.setCurrent(todoVO.getCurrentPage());
        page.setSize(todoVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        IPage<Todo> pageList = todoService.page(page, queryWrapper);
        log.info("执行获取代办事项列表");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加代办事项")
    @ApiOperation(value = "增加代办事项", notes = "增加代办事项", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody TodoVO todoVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        Todo todo = new Todo();
        todo.setText(todoVO.getText());
        //默认未完成
        todo.setDone(false);
        todo.setAdminUid(adminUid);
        todo.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @OperationLogger(value = "编辑代办事项")
    @ApiOperation(value = "编辑代办事项", notes = "编辑代办事项", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody TodoVO todoVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        Todo todo = todoService.getById(todoVO.getUid());

        if (!todo.getAdminUid().equals(adminUid)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ACCESS_NO_PRIVILEGE);
        }

        todo.setText(todoVO.getText());
        todo.setDone(todoVO.getDone());
        todo.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @OperationLogger(value = "删除代办事项")
    @ApiOperation(value = "删除代办事项", notes = "删除代办事项", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody TodoVO todoVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        Todo todo = todoService.getById(todoVO.getUid());

        if (!todo.getAdminUid().equals(adminUid)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DATA_NO_PRIVILEGE);
        }

        todo.setStatus(EStatus.DISABLED);
        todo.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @OperationLogger(value = "批量编辑代办事项")
    @ApiOperation(value = "批量编辑代办事项", notes = "批量编辑代办事项", response = String.class)
    @PostMapping("/toggleAll")
    public String toggleAll(HttpServletRequest request, @Validated({GetOne.class}) @RequestBody TodoVO todoVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        if (todoVO.getDone()) {
            todoService.toggleAll(SysConf.ONE, adminUid);
        } else {
            todoService.toggleAll(SysConf.ZERO, adminUid);
        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }


}

