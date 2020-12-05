package com.moxi.mogublog.admin.restapi;


import com.moxi.mogublog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.mogublog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.vo.BlogSortVO;
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
 * 博客分类表 RestApi
 *
 * @author 陌溪
 * @date 2018年9月24日15:45:18
 */
@RestController
@RequestMapping("/blogSort")
@Api(value = "博客分类相关接口", tags = {"博客分类相关接口"})
@Slf4j
public class BlogSortRestApi {

    @Autowired
    private BlogSortService blogSortService;

    @AuthorityVerify
    @ApiOperation(value = "获取博客分类列表", notes = "获取博客分类列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("获取博客分类列表");
        return ResultUtil.successWithData(blogSortService.getPageList(blogSortVO));
    }

    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加博客分类")
    @ApiOperation(value = "增加博客分类", notes = "增加博客分类", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("增加博客分类");
        return blogSortService.addBlogSort(blogSortVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑博客分类")
    @ApiOperation(value = "编辑博客分类", notes = "编辑博客分类", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("编辑博客分类");
        return blogSortService.editBlogSort(blogSortVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "批量删除博客分类")
    @ApiOperation(value = "批量删除博客分类", notes = "批量删除博客分类", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(@Validated({Delete.class}) @RequestBody List<BlogSortVO> blogSortVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("批量删除博客分类");
        return blogSortService.deleteBatchBlogSort(blogSortVoList);
    }

    @AuthorityVerify
    @ApiOperation(value = "置顶分类", notes = "置顶分类", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("置顶分类");
        return blogSortService.stickBlogSort(blogSortVO);

    }

    @AuthorityVerify
    @OperationLogger(value = "通过点击量排序博客分类")
    @ApiOperation(value = "通过点击量排序博客分类", notes = "通过点击量排序博客分类", response = String.class)
    @PostMapping("/blogSortByClickCount")
    public String blogSortByClickCount() {
        log.info("通过点击量排序博客分类");
        return blogSortService.blogSortByClickCount();
    }

    /**
     * 通过引用量排序标签
     * 引用量就是所有的文章中，有多少使用了该标签，如果使用的越多，该标签的引用量越大，那么排名越靠前
     *
     * @return
     */
    @AuthorityVerify
    @OperationLogger(value = "通过引用量排序博客分类")
    @ApiOperation(value = "通过引用量排序博客分类", notes = "通过引用量排序博客分类", response = String.class)
    @PostMapping("/blogSortByCite")
    public String blogSortByCite() {
        log.info("通过引用量排序博客分类");
        return blogSortService.blogSortByCite();
    }
}

