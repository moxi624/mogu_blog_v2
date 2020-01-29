package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.entity.User;
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

import javax.annotation.Resource;
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
    WebUtils webUtils;

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;
    @Autowired
    private PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取评论列表", notes = "获取评论列表", response = String.class)
    @PostMapping(value = "/getList")
    public String getList(HttpServletRequest request, @Validated({GetList.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(commentVO.getKeyword()) && !StringUtils.isEmpty(commentVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.CONTENT, commentVO.getKeyword().trim());
        }

        Page<Comment> page = new Page<>();
        page.setCurrent(commentVO.getCurrentPage());
        page.setSize(commentVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Comment> pageList = commentService.page(page, queryWrapper);
        List<Comment> commentList = pageList.getRecords();
        Set<String> userUidSet = new HashSet<>();
        Set<String> blogUidSet = new HashSet<>();
        commentList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUserUid())) {
                userUidSet.add(item.getUserUid());
            }
            if (StringUtils.isNotEmpty(item.getToUserUid())) {
                userUidSet.add(item.getToUserUid());
            }
            if (StringUtils.isNotEmpty(item.getBlogUid())) {
                blogUidSet.add(item.getBlogUid());
            }
        });

        // 获取博客
        Collection<Blog> blogList = new ArrayList<>();
        if (blogUidSet.size() > 0) {
            blogList = blogService.listByIds(blogUidSet);
        }
        Map<String, Blog> blogMap = new HashMap<>();
        blogList.forEach(item -> {
            // 评论管理并不需要查看博客内容，因此将其排除
            item.setContent("");
            blogMap.put(item.getUid(), item);
        });

        // 获取头像
        Collection<User> userCollection = new ArrayList<>();
        if (userUidSet.size() > 0) {
            userCollection = userService.listByIds(userUidSet);
        }

        final StringBuffer fileUids = new StringBuffer();
        userCollection.forEach(item -> {
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
        Map<String, User> userap = new HashMap<>();
        userCollection.forEach(item -> {
            if (pictureMap.get(item.getAvatar()) != null) {
                item.setPhotoUrl(pictureMap.get(item.getAvatar()));
                userap.put(item.getUid(), item);
            }
        });

        commentList.forEach(item -> {
            ECommentSource commentSource = ECommentSource.valueOf(item.getSource());
            item.setSourceName(commentSource.getName());

            if (StringUtils.isNotEmpty(item.getUserUid())) {
                item.setUser(userap.get(item.getUserUid()));
            }
            if (StringUtils.isNotEmpty(item.getToUserUid())) {
                item.setToUser(userap.get(item.getToUserUid()));
            }
            if (StringUtils.isNotEmpty(item.getBlogUid())) {
                item.setBlog(blogMap.get(item.getBlogUid()));
            }
        });

        pageList.setRecords(commentList);

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "增加评论", notes = "增加评论", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Comment comment = new Comment();
        comment.setSource(commentVO.getSource());
        comment.setBlogUid(commentVO.getBlogUid());
        comment.setContent(commentVO.getContent());
        comment.setUserUid(commentVO.getUserUid());
        comment.setToUid(commentVO.getToUid());
        comment.setToUserUid(commentVO.getToUserUid());
        comment.setStatus(EStatus.ENABLE);
        comment.insert();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @ApiOperation(value = "编辑评论", notes = "编辑评论", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Comment comment = commentService.getById(commentVO.getUid());
        comment.setSource(commentVO.getSource());
        comment.setBlogUid(commentVO.getBlogUid());
        comment.setContent(commentVO.getContent());
        comment.setUserUid(commentVO.getUserUid());
        comment.setToUid(commentVO.getToUid());
        comment.setToUserUid(commentVO.getToUserUid());
        comment.setStatus(EStatus.ENABLE);
        comment.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @ApiOperation(value = "删除评论", notes = "删除评论", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody CommentVO commentVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Comment comment = commentService.getById(commentVO.getUid());
        comment.setStatus(EStatus.DISABLED);
        comment.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @OperationLogger(value = "删除选中评论")
    @ApiOperation(value = "删除选中评论", notes = "删除选中评论", response = String.class)
    @PostMapping("/deleteBatch")
    public String deleteBatch(HttpServletRequest request, @Validated({Delete.class}) @RequestBody List<CommentVO> commentVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        if (commentVoList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        commentVoList.forEach(item -> {
            uids.add(item.getUid());
        });
        Collection<Comment> commentList = commentService.listByIds(uids);

        commentList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
        });

        commentService.updateBatchById(commentList);

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }


}

