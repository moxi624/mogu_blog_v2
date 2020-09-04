package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Data
@TableName("t_comment")
public class Comment extends SuperEntity<Comment> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户uid
     */
    private String userUid;

    /**
     * 回复某条评论的uid
     */
    private String toUid;

    /**
     * 该条评论下的，一级评论UID
     */
    private String firstCommentUid;

    /**
     * 回复某个人的uid
     */
    private String toUserUid;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 博客uid
     */
    private String blogUid;

    /**
     * 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等
     */
    private String source;

    /**
     * 评论类型： 0: 评论   1: 点赞
     */
    private Integer type;

    /**
     * 本条评论是哪个用户说的
     */
    @TableField(exist = false)
    private User user;

    /**
     * 发表评论的用户名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 被回复的用户名
     */
    @TableField(exist = false)
    private String toUserName;


    /**
     * 本条评论对哪个用户说的，如果没有则为一级评论
     */
    @TableField(exist = false)
    private User toUser;

    /**
     * 本条评论下的回复
     */
    @TableField(exist = false)
    private List<Comment> replyList;

    /**
     * 本条评论回复的那条评论
     */
    @TableField(exist = false)
    private Comment toComment;

    /**
     * 评论来源名称
     */
    @TableField(exist = false)
    private String sourceName;

    /**
     * 该评论来源的博客
     */
    @TableField(exist = false)
    private Blog blog;

}
