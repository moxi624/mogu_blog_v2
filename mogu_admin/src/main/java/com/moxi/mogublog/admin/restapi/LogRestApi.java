package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.security.AuthorityVerify;
import com.moxi.mogublog.commons.entity.ExceptionLog;
import com.moxi.mogublog.commons.entity.SysLog;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mogublog.xo.service.SysLogService;
import com.moxi.mogublog.xo.vo.ExceptionLogVO;
import com.moxi.mogublog.xo.vo.LinkVO;
import com.moxi.mogublog.xo.vo.SysLogVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.GetList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @AuthorityVerify
    @ApiOperation(value = "获取操作日志列表", notes = "获取操作日志列表", response = String.class)
    @PostMapping(value = "/getLogList")
    public String getLogList(@Validated({GetList.class}) @RequestBody SysLogVO sysLogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, sysLogService.getPageList(sysLogVO));
    }

    @AuthorityVerify
    @ApiOperation(value = "获取系统异常列表", notes = "获取系统异常列表", response = String.class)
    @PostMapping(value = "/getExceptionList")
    public String getExceptionList(@Validated({GetList.class}) @RequestBody ExceptionLogVO exceptionLogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, exceptionLogService.getPageList(exceptionLogVO));
    }
}

