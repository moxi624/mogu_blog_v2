package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
public interface CommentService extends SuperService<Comment> {

    /**
     * 获取评论数目
     *
     * @author xzx19950624@qq.com
     * @date 2018年10月22日下午3:43:38
     */
    public Integer getCommentCount(int status);

}
