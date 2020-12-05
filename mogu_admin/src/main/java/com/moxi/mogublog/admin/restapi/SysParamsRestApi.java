package com.moxi.mogublog.admin.restapi;


import com.moxi.mogublog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.mogublog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.SysParamsService;
import com.moxi.mogublog.xo.vo.SysParamsVO;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
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
import java.util.List;

/**
 * 参数配置 RestApi
 *
 * @author 陌溪
 * @date 2020年7月21日15:57:41
 */
@RestController
@RequestMapping("/sysParams")
@Api(value = "参数配置相关接口", tags = {"参数配置相关接口"})
@Slf4j
public class SysParamsRestApi {

    @Autowired
    private SysParamsService sysParamsService;

    @AuthorityVerify
    @ApiOperation(value = "获取参数配置列表", notes = "获取参数配置列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody SysParamsVO SysParamsVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("获取参数配置列表");
        return ResultUtil.successWithData(sysParamsService.getPageList(SysParamsVO));
    }

    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加参数配置")
    @ApiOperation(value = "增加参数配置", notes = "增加参数配置", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody SysParamsVO sysParamsVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return sysParamsService.addSysParams(sysParamsVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑参数配置")
    @ApiOperation(value = "编辑参数配置", notes = "编辑参数配置", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody SysParamsVO SysParamsVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return sysParamsService.editSysParams(SysParamsVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "批量删除参数配置")
    @ApiOperation(value = "批量删除参数配置", notes = "批量删除参数配置", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(@RequestBody List<SysParamsVO> SysParamsVoList, BindingResult result) {

        return sysParamsService.deleteBatchSysParams(SysParamsVoList);
    }
}

