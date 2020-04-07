package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Data
@TableName("t_collect")
public class Collect extends SuperEntity<Collect> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的uid
     */
    private String userUid;

    /**
     * 博客的uid
     */
    private String blogUid;
}
