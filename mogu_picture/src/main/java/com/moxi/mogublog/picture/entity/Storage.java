package com.moxi.mogublog.picture.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * @author: 存储信息实体类
 * @create: 2020年6月13日17:03:02
 */
@TableName("t_storage")
@Data
public class Storage extends SuperEntity<Storage> {

    /**
     * 管理员UID
     */
    private String adminUid;

    /**
     * 存储大小
     */
    private long storageSize;
}
