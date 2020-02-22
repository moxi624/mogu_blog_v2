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
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.vo.LinkVO;
import com.moxi.mougblog.base.enums.EStatus;
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
 * <p>
 * 友链表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
@RestController
@Api(value = "友链RestApi", tags = {"LinkRestApi"})
@RequestMapping("/link")
@Slf4j
public class LinkRestApi {

    @Autowired
    LinkService linkService;

    @ApiOperation(value = "获取友链列表", notes = "获取友链列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(linkVO.getKeyword()) && !StringUtils.isEmpty(linkVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.TITLE, linkVO.getKeyword().trim());
        }

        Page<Link> page = new Page<>();
        page.setCurrent(linkVO.getCurrentPage());
        page.setSize(linkVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<Link> pageList = linkService.page(page, queryWrapper);
        log.info("获取友链列表");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加友链")
    @ApiOperation(value = "增加友链", notes = "增加友链", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Link link = new Link();
        link.setTitle(linkVO.getTitle());
        link.setSummary(linkVO.getSummary());
        link.setUrl(linkVO.getUrl());
        link.setClickCount(0);
        link.setSort(linkVO.getSort());
        link.setStatus(EStatus.ENABLE);
        link.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @OperationLogger(value = "编辑友链")
    @ApiOperation(value = "编辑友链", notes = "编辑友链", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Link link = linkService.getById(linkVO.getUid());
        link.setTitle(linkVO.getTitle());
        link.setSummary(linkVO.getSummary());
        link.setUrl(linkVO.getUrl());
        link.setSort(linkVO.getSort());
        link.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @OperationLogger(value = "删除友链")
    @ApiOperation(value = "删除友链", notes = "删除友链", response = String.class)
    @PostMapping("/delete")
    public String delete(@Validated({Delete.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Link tag = linkService.getById(linkVO.getUid());
        tag.setStatus(EStatus.DISABLED);
        tag.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @ApiOperation(value = "置顶友链", notes = "置顶友链", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Link link = linkService.getById(linkVO.getUid());

        //查找出最大的那一个
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.SORT);
        Page<Link> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<Link> pageList = linkService.page(page, queryWrapper);
        List<Link> list = pageList.getRecords();
        Link maxSort = list.get(0);
        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        if (maxSort.getUid().equals(link.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }

        Integer sortCount = maxSort.getSort() + 1;

        link.setSort(sortCount);

        link.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}