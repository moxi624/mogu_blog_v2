package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 代办事项表
 * </p>
 *
 * @author xuzhixiang
 * @since 2019年6月29日10:26:30
 */
@Data
@TableName("t_todo")
public class Todo extends SuperEntity<Todo> {

    private static final long serialVersionUID = 1L;

    /**
     * 内容
     */
    private String text;

    /**
     * 管理员UID
     */
    private String adminUid;

    /**
     * 表示事项是否完成
     */
    private Boolean done;
}
