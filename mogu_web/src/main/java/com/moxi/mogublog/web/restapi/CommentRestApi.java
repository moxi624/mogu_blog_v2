package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.util.WebUtils;
import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.entity.CommentReport;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.entity.WebConfig;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mogublog.xo.vo.CommentVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.GetOne;
import com.moxi.mougblog.base.validator.group.Insert;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 评论RestApi
 *
 * @author 陌溪
 * @date 2020年1月11日16:11:55
 */
@RestController
@RequestMapping("/web/comment")
@Api(value = "评论RestApi", tags = {"CommentRestApi"})
@Slf4j
public class CommentRestApi {


    @Autowired
    private WebVisitService webVisitService;

    @Autowired
    private WebConfigService webConfigService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @Autowired
    private CommentReportService commentReportService;

    @Autowired
    WebUtils webUtils;

    /**
     * 获取评论列表
     *
     * @param request
     * @param commentVO
     * @param result
     * @return
     */
    @ApiOperation(value = "获取评论列表", notes = "获取评论列表")
    @PostMapping("/getList")
    public String getList(HttpServletRequest request, @Validated({GetList.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(commentVO.getBlogUid())) {
            queryWrapper.like(SQLConf.BLOG_UID, commentVO.getBlogUid());
        }

        queryWrapper.eq(SQLConf.SOURCE, commentVO.getSource());
        //分页
        Page<Comment> page = new Page<>();
        page.setCurrent(commentVO.getCurrentPage());
        page.setSize(commentVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Comment> pageList = commentService.page(page, queryWrapper);
        List<Comment> list = pageList.getRecords();

        List<String> userUidList = new ArrayList<>();
        list.forEach(item -> {
            String userUid = item.getUserUid();
            String toUserUid = item.getToUserUid();
            if (StringUtils.isNotEmpty(userUid)) {
                userUidList.add(item.getUserUid());
            }
            if (StringUtils.isNotEmpty(toUserUid)) {
                userUidList.add(item.getToUserUid());
            }
        });
        Collection<User> userList = new ArrayList<>();

        if (userUidList.size() > 0) {
            userList = userService.listByIds(userUidList);
        }

        // 获取用户头像
        StringBuffer fileUids = new StringBuffer();
        userList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });
        String pictureList = null;
        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureList);
        Map<String, String> pictureMap = new HashMap<>();
        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        Map<String, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar()) && pictureMap.get(item.getAvatar()) != null) {
                item.setPhotoUrl(pictureMap.get(item.getAvatar()));
            }
            userMap.put(item.getUid(), item);
        });

        Map<String, Comment> commentMap = new HashMap<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUserUid())) {
                item.setUser(userMap.get(item.getUserUid()));
            }
            if (StringUtils.isNotEmpty(item.getToUserUid())) {
                item.setToUser(userMap.get(item.getToUserUid()));
            }
            commentMap.put(item.getUid(), item);
        });

        Map<String, List<Comment>> toCommentListMap = new HashMap<>();

        for (int a = 0; a < list.size(); a++) {
            List<Comment> tempList = new ArrayList<>();
            for (int b = 0; b < list.size(); b++) {
                if (list.get(a).getUid().equals(list.get(b).getToUid())) {
                    tempList.add(list.get(b));
                }
            }
            toCommentListMap.put(list.get(a).getUid(), tempList);
        }

        List<Comment> firstComment = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isEmpty(item.getToUid())) {
                firstComment.add(item);
            }
        });

        getCommentReplys(firstComment, toCommentListMap);


        return ResultUtil.result(SysConf.SUCCESS, getCommentReplys(firstComment, toCommentListMap));
    }

    @ApiOperation(value = "增加评论", notes = "增加评论")
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        WebConfig webConfig = webConfigService.getOne(queryWrapper);
        if(SysConf.CAN_NOT_COMMENT.equals(webConfig.getStartComment())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.NO_COMMENTS_OPEN);
        }
        ThrowableUtils.checkParamArgument(result);

        if (commentVO.getContent().length() > SysConf.TWO_TWO_FIVE) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.COMMENT_CAN_NOT_MORE_THAN_225);
        }
        Comment comment = new Comment();
        comment.setSource(commentVO.getSource());
        comment.setBlogUid(commentVO.getBlogUid());
        comment.setContent(commentVO.getContent());
        comment.setUserUid(commentVO.getUserUid());
        comment.setToUid(commentVO.getToUid());
        comment.setToUserUid(commentVO.getToUserUid());
        comment.setStatus(EStatus.ENABLE);
        comment.insert();

        User user = userService.getById(commentVO.getUserUid());

        //获取图片
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(user.getAvatar(), SysConf.FILE_SEGMENTATION);
            if (webUtils.getPicture(pictureList).size() > 0) {
                user.setPhotoUrl(webUtils.getPicture(pictureList).get(0));
            }
        }
        comment.setUser(user);

        return ResultUtil.result(SysConf.SUCCESS, comment);
    }

    @ApiOperation(value = "举报评论", notes = "举报评论")
    @PostMapping("/report")
    public String reportComment(HttpServletRequest request, @Validated({GetOne.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);


        Comment comment = commentService.getById(commentVO.getUid());

        // 判断评论是否被删除
        if (comment == null || comment.getStatus() == EStatus.DISABLED) {

            return ResultUtil.result(SysConf.ERROR, MessageConf.COMMENT_IS_NOT_EXIST);
        }

        // 判断举报的评论是否是自己的
        if (comment.getUserUid().equals(commentVO.getUserUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.CAN_NOT_REPORT_YOURSELF_COMMENTS);
        }

        // 查看该用户是否重复举报该评论
        QueryWrapper<CommentReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_UID, commentVO.getUserUid());
        queryWrapper.eq(SQLConf.REPORT_COMMENT_UID, comment.getUid());
        List<CommentReport> commentReportList = commentReportService.list(queryWrapper);
        if (commentReportList.size() > 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.CAN_NOT_REPEAT_REPORT_COMMENT);
        }

        CommentReport commentReport = new CommentReport();
        commentReport.setContent(commentVO.getContent());
        commentReport.setProgress(0);
        // 从VO中获取举报的用户uid
        commentReport.setUserUid(commentVO.getUserUid());
        commentReport.setReportCommentUid(comment.getUid());
        // 从entity中获取被举报的用户uid
        commentReport.setReportUserUid(comment.getUserUid());
        commentReport.setStatus(EStatus.ENABLE);
        commentReport.insert();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 通过UID删除评论
     *
     * @param request
     * @param commentVO
     * @param result
     * @return
     */
    @ApiOperation(value = "删除评论", notes = "删除评论")
    @PostMapping("/delete")
    public String deleteBatch(HttpServletRequest request, @Validated({Delete.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);

        Comment comment = commentService.getById(commentVO.getUid());

        // 判断该评论是否能够删除
        if (!comment.getUserUid().equals(commentVO.getUserUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DATA_NO_PRIVILEGE);
        }

        comment.setStatus(EStatus.DISABLED);

        comment.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    /**
     * 获取评论所有回复
     *
     * @param list
     * @param toCommentListMap
     * @return
     */
    private List<Comment> getCommentReplys(List<Comment> list, Map<String, List<Comment>> toCommentListMap) {


        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        } else {

            list.forEach(item -> {
                String commentUid = item.getUid();
                List<Comment> replyCommentList = toCommentListMap.get(commentUid);

                List<Comment> replyComments = getCommentReplys(replyCommentList, toCommentListMap);

                item.setReplyList(getCommentReplys(replyCommentList, toCommentListMap));

            });

            return list;
        }
    }

}

