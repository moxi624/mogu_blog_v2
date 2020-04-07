package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;


/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author 陌溪
 * @since 2020年2月15日19:58:46
 */
@Data
@TableName("t_sys_dict_type")
public class SysDictType extends SuperEntity<SysDictType> {

    /**
     * 自增键 oid
     */
    private Long oid;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 是否发布  1：是，0:否，默认为0
     */
    private String isPublish;

    /**
     * 创建人UID
     */
    private String createByUid;

    /**
     * 最后更新人UID
     */
    private String updateByUid;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序字段
     */
    private Integer sort;

}
