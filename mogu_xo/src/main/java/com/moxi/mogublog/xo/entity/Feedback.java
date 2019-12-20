package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 反馈表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Data
@TableName("t_feedback")
public class Feedback extends SuperEntity<Feedback> {

    private static final long serialVersionUID = 1L;


    /**
     * 用户uid
     */
    private String user_uid;

    /**
     * 反馈的内容
     */
    private String content;

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
