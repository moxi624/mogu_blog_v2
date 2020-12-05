package com.moxi.mogublog.admin.restapi;


import com.moxi.mogublog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.mogublog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.vo.TagVO;
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
 * 标签表 RestApi
 *
 * @author 陌溪
 * @since 2018-09-08
 */
@Api(value = "博客标签相关接口", tags = {"博客标签相关接口"})
@RestController
@RequestMapping("/tag")
@Slf4j
public class TagRestApi {

    @Autowired
    private TagService tagService;

    @AuthorityVerify
    @ApiOperation(value = "获取标签列表", notes = "获取标签列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("获取标签列表");
        return ResultUtil.result(SysConf.SUCCESS, tagService.getPageList(tagVO));
    }

    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加标签")
    @ApiOperation(value = "增加标签", notes = "增加标签", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("增加标签");
        return tagService.addTag(tagVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑标签")
    @ApiOperation(value = "编辑标签", notes = "编辑标签", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("编辑标签");
        return tagService.editTag(tagVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "批量删除标签")
    @ApiOperation(value = "批量删除标签", notes = "批量删除标签", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(@Validated({Delete.class}) @RequestBody List<TagVO> tagVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("批量删除标签");
        return tagService.deleteBatchTag(tagVoList);
    }

    @AuthorityVerify
    @OperationLogger(value = "置顶标签")
    @ApiOperation(value = "置顶标签", notes = "置顶标签", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody TagVO tagVO, BindingResult result) {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("置顶标签");
        return tagService.stickTag(tagVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "通过点击量排序标签")
    @ApiOperation(value = "通过点击量排序标签", notes = "通过点击量排序标签", response = String.class)
    @PostMapping("/tagSortByClickCount")
    public String tagSortByClickCount() {
        log.info("通过点击量排序标签");
        return tagService.tagSortByClickCount();
    }

    /**
     * 通过引用量排序标签
     * 引用量就是所有的文章中，有多少使用了该标签，如果使用的越多，该标签的引用量越大，那么排名越靠前
     *
     * @return
     */
    @AuthorityVerify
    @OperationLogger(value = "通过引用量排序标签")
    @ApiOperation(value = "通过引用量排序标签", notes = "通过引用量排序标签", response = String.class)
    @PostMapping("/tagSortByCite")
    public String tagSortByCite() {
        log.info("通过引用量排序标签");
        return tagService.tagSortByCite();
    }
}

