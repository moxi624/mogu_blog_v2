package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.security.AuthorityVerify;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Feedback;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.FeedbackService;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.vo.FeedbackVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
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
 * 反馈表 RestApi
 * </p>
 *
 * @author 陌溪
 * @since 2020年3月16日08:38:07
 */
@RestController
@Api(value = "反馈RestApi", tags = {"FeedbackRestApi"})
@RequestMapping("/feedback")
@Slf4j
public class FeedbackRestApi {

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    UserService userService;

    @AuthorityVerify
    @ApiOperation(value = "获取反馈列表", notes = "获取反馈列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody FeedbackVO feedbackVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(feedbackVO.getTitle())) {
            queryWrapper.like(SQLConf.TITLE, feedbackVO.getTitle());
        }

        if (feedbackVO.getFeedbackStatus() != null) {
            queryWrapper.eq(SQLConf.FEEDBACK_STATUS, feedbackVO.getFeedbackStatus());
        }

        Page<Feedback> page = new Page<>();
        page.setCurrent(feedbackVO.getCurrentPage());
        page.setSize(feedbackVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Feedback> pageList = feedbackService.page(page, queryWrapper);

        List<Feedback> feedbackList = pageList.getRecords();
        List<String> userUids = new ArrayList<>();
        feedbackList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUserUid())) {
                userUids.add(item.getUserUid());
            }
        });
        List<User> userList = userService.getUserListByIds(userUids);
        Map<String, User> map = new HashMap<>();
        userList.forEach(item -> {
            item.setPassWord("");
            map.put(item.getUid(), item);
        });

        feedbackList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUserUid())) {
                item.setUser(map.get(item.getUserUid()));
            }
        });

        pageList.setRecords(feedbackList);

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑反馈")
    @ApiOperation(value = "编辑反馈", notes = "编辑反馈", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody FeedbackVO feedbackVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        if (request.getAttribute(SysConf.ADMIN_UID) != null) {
            ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }

        Feedback feedback = feedbackService.getById(feedbackVO.getUid());
        feedback.setTitle(feedbackVO.getTitle());
        feedback.setContent(feedbackVO.getContent());
        feedback.setFeedbackStatus(feedbackVO.getFeedbackStatus());
        feedback.setReply(feedbackVO.getReply());
        feedback.setUpdateTime(new Date());
        if (request.getAttribute(SysConf.ADMIN_UID) != null) {
            feedback.setAdminUid(request.getAttribute(SysConf.ADMIN_UID).toString());
        }
        feedback.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @AuthorityVerify
    @OperationLogger(value = "批量删除反馈")
    @ApiOperation(value = "批量删除反馈", notes = "批量删除反馈", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody List<FeedbackVO> feedbackVOList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        if (request.getAttribute(SysConf.ADMIN_UID) != null) {
            ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        final String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        if (feedbackVOList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        feedbackVOList.forEach(item -> {
            uids.add(item.getUid());
        });

        Collection<Feedback> feedbackList = feedbackService.listByIds(uids);

        feedbackList.forEach(item -> {
            item.setAdminUid(adminUid);
            item.setStatus(EStatus.DISABLED);
        });

        Boolean save = feedbackService.updateBatchById(feedbackList);

        if (save) {
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DELETE_FAIL);
        }
    }

}

