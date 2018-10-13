package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
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

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getToUid() {
		return toUid;
	}

	public void setToUid(String toUid) {
		this.toUid = toUid;
	}

	public String getToUserUid() {
		return toUserUid;
	}

	public void setToUserUid(String toUserUid) {
		this.toUserUid = toUserUid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBlogUid() {
		return blogUid;
	}

	public void setBlogUid(String blogUid) {
		this.blogUid = blogUid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
