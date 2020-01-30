package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.ExceptionLog;
import com.moxi.mogublog.xo.entity.SysLog;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mogublog.xo.service.SysLogService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 日志记录表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月24日15:45:18
 */
@RestController
@Api(value = "管理员操作日志RestApi", tags = {"LogRestApi"})
@RequestMapping("/log")
@Slf4j
public class LogRestApi {

    @Autowired
    SysLogService sysLogService;
    @Autowired
    ExceptionLogService exceptionLogService;

    @ApiOperation(value = "获取操作日志列表", notes = "获取操作日志列表", response = String.class)
    @RequestMapping(value = "/getLogList", method = RequestMethod.GET)
    public String getLogList(HttpServletRequest request,
                             @ApiParam(name = "userName", value = "用户名", required = false) @RequestParam(name = "userName", required = false) String userName,
                             @ApiParam(name = "operation", value = "接口名", required = false) @RequestParam(name = "operation", required = false) String operation,
                             @ApiParam(name = "startTime", value = "时间段", required = false) @RequestParam(name = "startTime", required = false) String startTime,
                             @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                             @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<SysLog>();
        if (StringUtils.isNotEmpty(userName) && !StringUtils.isEmpty(userName.trim())) {
            queryWrapper.like(SQLConf.USER_NAME, userName.trim());
        }

        if (!StringUtils.isEmpty(operation)) {
            queryWrapper.like(SQLConf.OPERATION, operation);
        }

        if (!StringUtils.isEmpty(startTime)) {
            String[] time = startTime.split(",");
            if (time.length < 2) {
                return ResultUtil.result(SysConf.ERROR, "传入时间有误");
            }
            queryWrapper.between(SQLConf.CREATE_TIME, DateUtils.str2Date(time[0]), DateUtils.str2Date(time[1]));
        }

        Page<SysLog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "获取系统异常列表", notes = "获取系统异常列表", response = String.class)
    @RequestMapping(value = "/getExceptionList", method = RequestMethod.GET)
    public String getExceptionList(HttpServletRequest request,
                                   @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                                   @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                   @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<ExceptionLog> queryWrapper = new QueryWrapper<ExceptionLog>();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like(SQLConf.CONTENT, keyword);
        }
        Page<ExceptionLog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
//		queryWrapper.excludeColumns(ExceptionLog.class, "exception_json");
        queryWrapper.select(ExceptionLog.class, i -> !i.getProperty().equals("exception_json"));
        IPage<ExceptionLog> pageList = exceptionLogService.page(page, queryWrapper);
        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }
}

