package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.service.CommentService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年10月13日16:06:46
 */
//@PreAuthorize("hasRole('Administrator')")
@Api(value = "评论RestApi", tags = {"CommentRestApi"})
@RestController
@RequestMapping("/comment")
public class CommentRestApi {
    private static Logger log = LogManager.getLogger(AdminRestApi.class);
    @Autowired
    CommentService commentService;

    @ApiOperation(value = "获取评论列表", notes = "获取评论列表", response = String.class)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>();
        if (StringUtils.isNotEmpty(keyword) && !StringUtils.isEmpty(keyword.trim())) {
            queryWrapper.like(SQLConf.CONTENT, keyword.trim());
        }

        Page<Comment> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Comment> pageList = commentService.page(page, queryWrapper);
        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "增加评论", notes = "增加评论", response = String.class)
    @PostMapping("/add")
    public String add(@RequestBody Comment comment) {

        if (StringUtils.isEmpty(comment.getUserUid()) || StringUtils.isEmpty(comment.getContent())) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        comment.setStatus(EStatus.ENABLE);
        comment.insert();
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @ApiOperation(value = "编辑评论", notes = "编辑评论", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @RequestBody Comment comment) {

        if (StringUtils.isEmpty(comment.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        comment.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
    }

    @ApiOperation(value = "删除评论", notes = "删除评论", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        Comment comment = commentService.getById(uid);
        comment.setStatus(EStatus.DISABLED);
        comment.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }
}

