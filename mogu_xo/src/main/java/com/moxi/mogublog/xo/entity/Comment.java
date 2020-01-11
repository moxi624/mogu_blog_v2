package com.moxi.mogublog.xo.entity;

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
     * 一级评论Uid，如果该评论为一级评论，那么firstUid为uid，否者为一级评论的Uid
     * 该字段是为了方便获取一级评论下所有的评论
     */
//    private String firstUid;

    /**
     * 回复某个人的uid
     */
    private String toUserUid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 博客uid
     */
    private String blogUid;

    /**
     * 本条评论是哪个用户说的
     */
    @TableField(exist = false)
    private User user;

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

}
