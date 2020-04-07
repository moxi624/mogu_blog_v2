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
import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.commons.entity.Comment;
import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.CommentService;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.vo.CommentVO;
import com.moxi.mougblog.base.enums.ECommentSource;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 评论表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2020年1月20日16:44:25
 */
@Api(value = "评论RestApi", tags = {"CommentRestApi"})
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentRestApi {

    @Autowired
    CommentService commentService;

    @AuthorityVerify
    @ApiOperation(value = "获取评论列表", notes = "获取评论列表", response = String.class)
    @PostMapping(value = "/getList")
    public String getList(HttpServletRequest request, @Validated({GetList.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, commentService.getPageList(commentVO));
    }

    @AuthorityVerify
    @ApiOperation(value = "增加评论", notes = "增加评论", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return commentService.addComment(commentVO);
    }

    @AuthorityVerify
    @ApiOperation(value = "编辑评论", notes = "编辑评论", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return commentService.editComment(commentVO);
    }

    @AuthorityVerify
    @ApiOperation(value = "删除评论", notes = "删除评论", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return commentService.deleteComment(commentVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "删除选中评论")
    @ApiOperation(value = "删除选中评论", notes = "删除选中评论", response = String.class)
    @PostMapping("/deleteBatch")
    public String deleteBatch(HttpServletRequest request, @Validated({Delete.class}) @RequestBody List<CommentVO> commentVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return commentService.deleteBatchComment(commentVoList);
    }


}

