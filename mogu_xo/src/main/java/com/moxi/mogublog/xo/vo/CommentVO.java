package com.moxi.mogublog.xo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moxi.mougblog.base.validator.annotion.IdValid;
import com.moxi.mougblog.base.validator.annotion.NotBlank;
import com.moxi.mougblog.base.validator.group.*;
import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * CommentVO
 *
 * @author: 陌溪
 * @create: 2020年1月11日16:15:52
 */
@Data
public class CommentVO extends BaseVO<CommentVO> {

    /**
     * 用户uid
     */
    @NotBlank(groups = {Insert.class, GetOne.class})
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
    @NotBlank(groups = {Insert.class})
    private String content;

    /**
     * 博客uid
     */
    private String blogUid;

    /**
     * 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等
     */
    @NotBlank(groups = {Insert.class, GetList.class})
    private String source;
}
