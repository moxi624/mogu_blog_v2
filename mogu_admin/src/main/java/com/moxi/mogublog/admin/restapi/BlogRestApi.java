package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.security.AuthorityVerify;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.commons.entity.Admin;
import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.commons.entity.BlogSort;
import com.moxi.mogublog.commons.entity.Tag;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.vo.BlogVO;
import com.moxi.mougblog.base.enums.ELevel;
import com.moxi.mougblog.base.enums.EOriginal;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 博客表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */

@RestController
@RequestMapping("/blog")
@Api(value = "博客RestApi", tags = {"BlogRestApi"})
@Slf4j
public class BlogRestApi {

    @Autowired
    BlogService blogService;

    @AuthorityVerify
    @ApiOperation(value = "获取博客列表", notes = "获取博客列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, blogService.getPageList(blogVO));
    }

    @AuthorityVerify
    @OperationLogger(value = "增加博客")
    @ApiOperation(value = "增加博客", notes = "增加博客", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        return blogService.addBlog(blogVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑博客")
    @ApiOperation(value = "编辑博客", notes = "编辑博客", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return blogService.editBlog(blogVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "推荐博客排序调整")
    @ApiOperation(value = "推荐博客排序调整", notes = "推荐博客排序调整", response = String.class)
    @PostMapping("/editBatch")
    public String editBatch(@RequestBody List<BlogVO> blogVOList) {
        return blogService.editBatch(blogVOList);
    }

    @AuthorityVerify
    @OperationLogger(value = "删除博客")
    @ApiOperation(value = "删除博客", notes = "删除博客", response = String.class)
    @PostMapping("/delete")
    public String delete(@Validated({Delete.class}) @RequestBody BlogVO blogVO, BindingResult result) {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return blogService.deleteBlog(blogVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "删除选中博客")
    @ApiOperation(value = "删除选中博客", notes = "删除选中博客", response = String.class)
    @PostMapping("/deleteBatch")
    public String deleteBatch(@RequestBody List<BlogVO> blogVoList) {
        return blogService.deleteBatchBlog(blogVoList);
    }

}