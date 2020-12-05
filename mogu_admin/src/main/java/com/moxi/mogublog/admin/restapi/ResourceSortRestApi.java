package com.moxi.mogublog.admin.restapi;


import com.moxi.mogublog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.mogublog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.ResourceSortService;
import com.moxi.mogublog.xo.vo.ResourceSortVO;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
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

import java.util.List;

/**
 * 资源分类表 RestApi
 *
 * @author 陌溪
 * @date 2020年1月9日19:23:28
 */
@Api(value = "资源分类相关接口", tags = {"资源分类相关接口"})
@RestController
@RequestMapping("/resourceSort")
@Slf4j
public class ResourceSortRestApi {

    @Autowired
    private ResourceSortService resourceSortService;

    @AuthorityVerify
    @ApiOperation(value = "获取资源分类列表", notes = "获取资源分类列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);
        log.info("获取资源分类列表:{}", resourceSortVO);
        return ResultUtil.successWithData(resourceSortService.getPageList(resourceSortVO));
    }

    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加资源分类")
    @ApiOperation(value = "增加资源分类", notes = "增加资源分类", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("增加资源分类:{}", resourceSortVO);
        return resourceSortService.addResourceSort(resourceSortVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑资源分类")
    @ApiOperation(value = "编辑资源分类", notes = "编辑资源分类", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("编辑资源分类:{}", resourceSortVO);
        return resourceSortService.editResourceSort(resourceSortVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "批量删除资源分类")
    @ApiOperation(value = "批量删除资源分类", notes = "批量删除资源分类", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(@Validated({Delete.class}) @RequestBody List<ResourceSortVO> resourceSortVOList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("批量删除资源分类:{}", resourceSortVOList);
        return resourceSortService.deleteBatchResourceSort(resourceSortVOList);
    }

    @AuthorityVerify
    @OperationLogger(value = "置顶资源分类")
    @ApiOperation(value = "置顶分类", notes = "置顶分类", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("置顶分类:{}", resourceSortVO);
        return resourceSortService.stickResourceSort(resourceSortVO);
    }
}

