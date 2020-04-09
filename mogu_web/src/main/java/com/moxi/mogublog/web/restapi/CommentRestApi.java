package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.*;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.RedisConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.log.BussinessLog;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.CommentVO;
import com.moxi.mogublog.xo.vo.UserVO;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.ECommentSource;
import com.moxi.mougblog.base.enums.ECommentType;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.GetOne;
import com.moxi.mougblog.base.validator.group.Insert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    RedisUtil redisUtil;
    @Autowired
    WebUtil webUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private WebConfigService webConfigService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private CommentReportService commentReportService;
    @Value(value = "${data.web.url}")
    private String dataWebUrl;
    @Value(value = "${PROJECT_NAME_EN}")
    private String projectName;
    @Value(value = "${BLOG.USER_TOKEN_SURVIVAL_TIME}")
    private Long userTokenSurvivalTime;
    @Value(value = "${data.website.url}")
    private String dataWebsiteUrl;

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
        queryWrapper.isNull(SQLConf.TO_UID);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(SQLConf.TYPE, ECommentType.COMMENT);
        // 查询出所有的一级评论，进行分页显示
        IPage<Comment> pageList = commentService.page(page, queryWrapper);
        List<Comment> list = pageList.getRecords();
        List<String> firstUidList = new ArrayList<>();
        list.forEach(item -> {
            firstUidList.add(item.getUid());
        });

        if (firstUidList.size() > 0) {
            // 查询一级评论下的子评论
            QueryWrapper<Comment> notFirstQueryWrapper = new QueryWrapper<>();
            notFirstQueryWrapper.in(SQLConf.FIRST_COMMENT_UID, firstUidList);
            notFirstQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            List<Comment> notFirstList = commentService.list(notFirstQueryWrapper);
            // 将子评论加入总的评论中
            if (notFirstList.size() > 0) {
                list.addAll(notFirstList);
            }
        }

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

        // 过滤掉用户的敏感信息
        List<User> filterUserList = new ArrayList<>();
        userList.forEach(item -> {
            User user = new User();
            user.setAvatar(item.getAvatar());
            user.setUid(item.getUid());
            user.setNickName(item.getNickName());
            user.setUserTag(item.getUserTag());
            filterUserList.add(user);
        });


        // 获取用户头像
        StringBuffer fileUids = new StringBuffer();
        filterUserList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });
        String pictureList = null;
        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);
        Map<String, String> pictureMap = new HashMap<>();
        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        Map<String, User> userMap = new HashMap<>();
        filterUserList.forEach(item -> {
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

        pageList.setRecords(getCommentReplys(firstComment, toCommentListMap));
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "获取用户的评论列表和回复", notes = "获取评论列表和回复")
    @PostMapping("/getListByUser")
    public String getListByUser(HttpServletRequest request, @Validated({GetList.class}) @RequestBody UserVO userVO) {

        if (request.getAttribute(SysConf.USER_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }

        String requestUserUid = request.getAttribute(SysConf.USER_UID).toString();

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        //分页
        Page<Comment> page = new Page<>();
        page.setCurrent(userVO.getCurrentPage());
        page.setSize(userVO.getPageSize());
        queryWrapper.eq(SQLConf.TYPE, ECommentType.COMMENT);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        // 查找出 我的评论 和 我的回复
        queryWrapper.and(wrapper -> wrapper.eq(SQLConf.USER_UID, requestUserUid).or().eq(SQLConf.TO_USER_UID, requestUserUid));
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

        // 获取用户列表
        Collection<User> userList = new ArrayList<>();

        if (userUidList.size() > 0) {
            userList = userService.listByIds(userUidList);
        }

        // 过滤掉用户的敏感信息
        List<User> filterUserList = new ArrayList<>();
        userList.forEach(item -> {
            User user = new User();
            user.setAvatar(item.getAvatar());
            user.setUid(item.getUid());
            user.setNickName(item.getNickName());
            filterUserList.add(user);
        });


        // 获取用户头像
        StringBuffer fileUids = new StringBuffer();
        filterUserList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });
        String pictureList = null;
        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);
        Map<String, String> pictureMap = new HashMap<>();
        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        Map<String, User> userMap = new HashMap<>();
        filterUserList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar()) && pictureMap.get(item.getAvatar()) != null) {
                item.setPhotoUrl(pictureMap.get(item.getAvatar()));
            }
            userMap.put(item.getUid(), item);
        });

        // 将评论列表划分为 我的评论 和 我的回复
        List<Comment> commentList = new ArrayList<>();
        List<Comment> replyList = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUserUid())) {
                item.setUser(userMap.get(item.getUserUid()));
            }

            if (StringUtils.isNotEmpty(item.getToUserUid())) {
                item.setToUser(userMap.get(item.getToUserUid()));
            }


            // 设置sourceName
            if (StringUtils.isNotEmpty(item.getSource())) {
                item.setSourceName(ECommentSource.valueOf(item.getSource()).getName());
            }

            if (requestUserUid.equals(item.getUserUid())) {
                commentList.add(item);
            }
            if (requestUserUid.equals(item.getToUserUid())) {
                replyList.add(item);
            }
        });

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(SysConf.COMMENT_LIST, commentList);
        resultMap.put(SysConf.REPLY_LIST, replyList);

        return ResultUtil.result(SysConf.SUCCESS, resultMap);
    }

    /**
     * 获取用户点赞信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户点赞信息", notes = "增加评论")
    @PostMapping("/getPraiseListByUser")
    public String getPraiseListByUser(@ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                      @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(SysConf.USER_UID) == null || request.getAttribute(SysConf.TOKEN) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        String userUid = request.getAttribute(SysConf.USER_UID).toString();

        QueryWrapper<Comment> queryWrappe = new QueryWrapper<>();
        queryWrappe.eq(SQLConf.USER_UID, userUid);
        queryWrappe.eq(SQLConf.TYPE, ECommentType.PRAISE);
        queryWrappe.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrappe.orderByDesc(SQLConf.CREATE_TIME);
        Page<Comment> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        IPage<Comment> pageList = commentService.page(page, queryWrappe);
        List<Comment> praiseList = pageList.getRecords();

        List<String> blogUids = new ArrayList<>();
        praiseList.forEach(item -> {
            blogUids.add(item.getBlogUid());
        });
        Map<String, Blog> blogMap = new HashMap<>();
        if (blogUids.size() > 0) {
            Collection<Blog> blogList = blogService.listByIds(blogUids);
            blogList.forEach(blog -> {
                // 并不需要content内容
                blog.setContent("");
                blogMap.put(blog.getUid(), blog);
            });
        }

        praiseList.forEach(item -> {
            if (blogMap.get(item.getBlogUid()) != null) {
                item.setBlog(blogMap.get(item.getBlogUid()));
            }
        });

        pageList.setRecords(praiseList);

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @BussinessLog(value = "发表评论", behavior = EBehavior.PUBLISH_COMMENT)
    @ApiOperation(value = "增加评论", notes = "增加评论")
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody CommentVO commentVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(SysConf.USER_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        WebConfig webConfig = webConfigService.getOne(queryWrapper);
        // 判断是否开启全局评论功能
        if (SysConf.CAN_NOT_COMMENT.equals(webConfig.getStartComment())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.NO_COMMENTS_OPEN);
        }
        // 判断博客是否开启评论功能
        if (StringUtils.isNotEmpty(commentVO.getBlogUid())) {
            Blog blog = blogService.getById(commentVO.getBlogUid());
            if (SysConf.CAN_NOT_COMMENT.equals(blog.getStartComment())) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.BLOG_NO_OPEN_COMMENTS);
            }
        }
        String userUid = request.getAttribute(SysConf.USER_UID).toString();

        User user = userService.getById(userUid);

        // 判断字数是否超过限制
        if (commentVO.getContent().length() > SysConf.TWO_TWO_FIVE) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.COMMENT_CAN_NOT_MORE_THAN_225);
        }

        // 判断该用户是否被禁言
        if (user.getCommentStatus() == SysConf.ZERO) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.YOU_DONT_HAVE_PERMISSION_TO_SPEAK);
        }

        // 判断是否发送过多无意义评论
        String jsonResult = redisUtil.get(RedisConf.USER_PUBLISH_SPAM_COMMENT_COUNT + BaseSysConf.REDIS_SEGMENTATION + userUid);
        if (!StringUtils.isEmpty(jsonResult)) {
            Integer count = Integer.valueOf(jsonResult);
            if (count >= 5) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.PLEASE_TRY_AGAIN_IN_AN_HOUR);
            }
        }

        String content = commentVO.getContent();

        // 判断是否垃圾评论
        if (StringUtils.isCommentSpam(content)) {
            if (StringUtils.isEmpty(jsonResult)) {
                Integer count = 0;
                redisUtil.setEx(RedisConf.USER_PUBLISH_SPAM_COMMENT_COUNT + BaseSysConf.REDIS_SEGMENTATION + userUid, count.toString(), 1, TimeUnit.HOURS);
            } else {
                redisUtil.incrBy(RedisConf.USER_PUBLISH_SPAM_COMMENT_COUNT + BaseSysConf.REDIS_SEGMENTATION + userUid, 1);
            }

            return ResultUtil.result(SysConf.ERROR, MessageConf.COMMENT_IS_SPAM);
        }

        // 判断被评论的用户，是否开启了评论邮件提醒
        if (StringUtils.isNotEmpty(commentVO.getToUserUid())) {
            User toUser = userService.getById(commentVO.getToUserUid());
            if (toUser.getStartEmailNotification() == SysConf.ONE) {
                Comment toComment = commentService.getById(commentVO.getToUid());
                if (toComment != null && StringUtils.isNotEmpty(toComment.getContent())) {
                    Map<String, String> map = new HashMap<>();
                    map.put(SysConf.EMAIL, toUser.getEmail());
                    map.put(SysConf.TEXT, commentVO.getContent());
                    map.put(SysConf.TO_TEXT, toComment.getContent());
                    map.put(SysConf.NICKNAME, user.getNickName());
                    map.put(SysConf.TO_NICKNAME, toUser.getNickName());
                    map.put(SysConf.USER_UID, toUser.getUid());

                    // 获取评论跳转的链接
                    String commentSource = toComment.getSource();
                    String url = new String();
                    switch (commentSource) {
                        case "ABOUT": {
                            url = dataWebsiteUrl + "about";
                        }
                        break;
                        case "BLOG_INFO": {
                            url = dataWebsiteUrl + "info?blogUid=" + toComment.getBlogUid();
                        }
                        break;
                        case "MESSAGE_BOARD": {
                            url = dataWebsiteUrl + "messageBoard";
                        }
                        break;
                    }

                    map.put(SysConf.URL, url);

                    sendEmail(map);
                }
            }
        }

        Comment comment = new Comment();
        comment.setSource(commentVO.getSource());
        comment.setBlogUid(commentVO.getBlogUid());
        comment.setContent(commentVO.getContent());
        comment.setToUserUid(commentVO.getToUserUid());

        // 当该评论不是一级评论时，需要设置一级评论UID字段
        if (StringUtils.isNotEmpty(commentVO.getToUid())) {
            Comment toComment = commentService.getById(commentVO.getToUid());
            // 表示这个评论是非一级评论
            if (toComment != null && StringUtils.isNotEmpty(toComment.getFirstCommentUid())) {
                comment.setFirstCommentUid(toComment.getFirstCommentUid());
            } else {
                // 表示这个评论是一级评论，直接获取UID
                comment.setFirstCommentUid(toComment.getUid());
            }
        }

        comment.setUserUid(commentVO.getUserUid());
        comment.setToUid(commentVO.getToUid());
        comment.setStatus(EStatus.ENABLE);
        comment.insert();

        //获取图片
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(user.getAvatar(), SysConf.FILE_SEGMENTATION);
            if (webUtil.getPicture(pictureList).size() > 0) {
                user.setPhotoUrl(webUtil.getPicture(pictureList).get(0));
            }
        }
        comment.setUser(user);

        return ResultUtil.result(SysConf.SUCCESS, comment);
    }


    @BussinessLog(value = "举报评论", behavior = EBehavior.REPORT_COMMENT)
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
    @BussinessLog(value = "删除评论", behavior = EBehavior.DELETE_COMMENT)
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

    @ApiOperation(value = "关闭评论邮件通知", notes = "关闭评论邮件通知")
    @GetMapping("/closeEmailNotification/{userUid}")
    public String bindUserEmail(@PathVariable("userUid") String userUid) {

        User user = userService.getById(userUid);
        if (user == null) {
            ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        user.setStartEmailNotification(0);
        user.updateById();

        // 通过user中获取的token，去修改redis中的信息
        if (StringUtils.isNotEmpty(user.getValidCode())) {
            String accessToken = user.getValidCode();
            String userInfo = redisUtil.get(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken);
            if (StringUtils.isNotEmpty(userInfo)) {
                Map<String, Object> map = JsonUtils.jsonToMap(userInfo);
                // 关闭邮件通知
                map.put(SysConf.START_EMAIL_NOTIFICATION, 0);
                redisUtil.setEx(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken, JsonUtils.objectToJson(map), userTokenSurvivalTime, TimeUnit.HOURS);
            }
        }

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
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

    /**
     * 发送邮件
     */
    private void sendEmail(Map<String, String> map) {

        String email = map.get(SysConf.EMAIL);
        String text = map.get(SysConf.TEXT);
        String toText = map.get(SysConf.TO_TEXT);
        String nickName = map.get(SysConf.NICKNAME);
        String toUserNickName = map.get(SysConf.TO_NICKNAME);
        String userUid = map.get(SysConf.USER_UID);
        String url = map.get(SysConf.URL);

        String content =
                "<html>\r\n" +
                        " <head>\r\n" +
                        "  <title> mogublog </title>\r\n" +
                        " </head>\r\n" +
                        " <body>\r\n" +
                        "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'spam', 'ZC1222-PrLAp4T0Z7Z7UUMYzqLkb8a');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">    \r\n" +
                        "  <div id=\"qm_con_body\"><div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"\">\r\n" +
                        "<style>\r\n" +
                        "  .qmbox .email-body{color:#40485B;font-size:14px;font-family:-apple-system, \"Helvetica Neue\", Helvetica, \"Nimbus Sans L\", \"Segoe UI\", Arial, \"Liberation Sans\", \"PingFang SC\", \"Microsoft YaHei\", \"Hiragino Sans GB\", \"Wenquanyi Micro Hei\", \"WenQuanYi Zen Hei\", \"ST Heiti\", SimHei, \"WenQuanYi Zen Hei Sharp\", sans-serif;background:#f8f8f8;}.qmbox .pull-right{float:right;}.qmbox a{color:#FE7300;text-decoration:underline;}.qmbox a:hover{color:#fe9d4c;}.qmbox a:active{color:#b15000;}.qmbox .logo{text-align:center;margin-bottom:20px;}.qmbox .panel{background:#fff;border:1px solid #E3E9ED;margin-bottom:10px;}.qmbox .panel-header{font-size:18px;line-height:30px;padding:10px 20px;background:#fcfcfc;border-bottom:1px solid #E3E9ED;}.qmbox .panel-body{padding:20px;}.qmbox .container{width:100%;max-width:600px;padding:20px;margin:0 auto;}.qmbox .text-center{text-align:center;}.qmbox .thumbnail{padding:4px;max-width:100%;border:1px solid #E3E9ED;}.qmbox .btn-primary{color:#fff;font-size:16px;padding:8px 14px;line-height:20px;border-radius:2px;display:inline-block;background:#FE7300;text-decoration:none;}.qmbox .btn-primary:hover,.qmbox .btn-primary:active{color:#fff;}.qmbox .footer{color:#9B9B9B;font-size:12px;margin-top:40px;}.qmbox .footer a{color:#9B9B9B;}.qmbox .footer a:hover{color:#fe9d4c;}.qmbox .footer a:active{color:#b15000;}.qmbox .email-body#mail_to_teacher{line-height:26px;color:#40485B;font-size:16px;padding:0px;}.qmbox .email-body#mail_to_teacher .container,.qmbox .email-body#mail_to_teacher .panel-body{padding:0px;}.qmbox .email-body#mail_to_teacher .container{padding-top:20px;}.qmbox .email-body#mail_to_teacher .textarea{padding:32px;}.qmbox .email-body#mail_to_teacher .say-hi{font-weight:500;}.qmbox .email-body#mail_to_teacher .paragraph{margin-top:24px;}.qmbox .email-body#mail_to_teacher .paragraph .pro-name{color:#000000;}.qmbox .email-body#mail_to_teacher .paragraph.link{margin-top:32px;text-align:center;}.qmbox .email-body#mail_to_teacher .paragraph.link .button{background:#4A90E2;border-radius:2px;color:#FFFFFF;text-decoration:none;padding:11px 17px;line-height:14px;display:inline-block;}.qmbox .email-body#mail_to_teacher ul.pro-desc{list-style-type:none;margin:0px;padding:0px;padding-left:16px;}.qmbox .email-body#mail_to_teacher ul.pro-desc li{position:relative;}.qmbox .email-body#mail_to_teacher ul.pro-desc li::before{content:'';width:3px;height:3px;border-radius:50%;background:red;position:absolute;left:-15px;top:11px;background:#40485B;}.qmbox .email-body#mail_to_teacher .blackboard-area{height:600px;padding:40px;background-image:url();color:#FFFFFF;}.qmbox .email-body#mail_to_teacher .blackboard-area .big-title{font-size:32px;line-height:45px;text-align:center;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc{margin-top:8px;}.qmbox .email-body#mail_to_teacher .blackboard-area .desc p{margin:0px;text-align:center;line-height:28px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(odd){float:left;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card:nth-child(even){float:right;margin-top:45px;}.qmbox .email-body#mail_to_teacher .blackboard-area .card .title{font-size:18px;text-align:center;margin-bottom:10px;}\r\n" +
                        "</style>\r\n" +
                        "<meta>\r\n" +
                        "<div class=\"email-body\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"container\">\r\n" +
                        "<div class=\"logo\">\r\n" +
                        "<img src=\"http://image.moguit.cn/favicon.png\",height=\"100\" width=\"100\">\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "<div class=\"panel-header\" style=\"background-color: rgb(246, 244, 236);\">\r\n" +
                        "评论提醒\r\n" +
                        "\r\n" +
                        "</div>\r\n" +
                        "<div class=\"panel-body\">\r\n" +
                        "<p>您好 <a href=\"mailto:" + email + "\" rel=\"noopener\" target=\"_blank\">" + toUserNickName + "<wbr></a>！</p>\r\n" +
                        "<p>" + nickName + " 对您的评论：" + "<a href=\"" + url + "\">" + toText + "</a>" + "   进行了回复</p>\r\n" +
                        "\r\n" +
                        "<p>回复内容为：" + "<a href=\"" + url + "\">" + text + "</a>" + "</p>\r\n" +
                        "\r\n" +
                        "<p>如果邮件通知干扰了您，可以点击右侧链接关闭通知：" + "<a href=\"" + dataWebUrl + "/web/comment/closeEmailNotification/" + userUid + "\">点击这里</a>" + "</p>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<div class=\"footer\">\r\n" +
                        "<a href=\" " + dataWebsiteUrl + "\">@" + projectName + "</a>\n" +
                        "<div class=\"pull-right\"></div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "</div>\r\n" +
                        "<style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div></div><!-- --><style>#mailContentContainer .txt {height:auto;}</style>  </div>\r\n" +
                        " </body>\r\n" +
                        "</html>";


        Map<String, Object> result = new HashMap<>();

        result.put("receiver", email);
        result.put("text", content);

        //发送到RabbitMq
        rabbitTemplate.convertAndSend("exchange.direct", "mogu.email", result);
    }


}

