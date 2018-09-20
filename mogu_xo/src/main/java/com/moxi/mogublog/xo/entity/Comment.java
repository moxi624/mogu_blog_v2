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
    private String user_uid;

    /**
     * 回复某条评论的uid
     */
    private String to_uid;

    /**
     * 回复某个人的uid
     */
    private String to_user_uid;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 博客uid
     */
    private String blog_uid;

	public String getUser_uid() {
		return user_uid;
	}

	public void setUser_uid(String user_uid) {
		this.user_uid = user_uid;
	}

	public String getTo_uid() {
		return to_uid;
	}

	public void setTo_uid(String to_uid) {
		this.to_uid = to_uid;
	}

	public String getTo_user_uid() {
		return to_user_uid;
	}

	public void setTo_user_uid(String to_user_uid) {
		this.to_user_uid = to_user_uid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBlog_uid() {
		return blog_uid;
	}

	public void setBlog_uid(String blog_uid) {
		this.blog_uid = blog_uid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
