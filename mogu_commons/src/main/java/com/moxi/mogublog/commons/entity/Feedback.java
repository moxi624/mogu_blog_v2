package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 反馈表
 * </p>
 *
 * @author xuzhixiang
 * @since 2020年3月16日08:35:26
 */
@Data
@TableName("t_feedback")
public class Feedback extends SuperEntity<Feedback> {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员UID
     */
    private String adminUid;

    /**
     * 用户uid
     */
    private String userUid;

    /**
     * 标题
     */
    private String title;

    /**
     * 反馈的内容
     */
    private String content;

    /**
     * 回复
     */
    private String reply;

    /**
     * 反馈状态： 0：已开启  1：进行中  2：已完成  3：已拒绝
     */
    private Integer feedbackStatus;

    // 以下字段不存入数据库，封装为了前端使用

    /**
     * 反馈用户
     */
    @TableField(exist = false)
    private User user;

}
