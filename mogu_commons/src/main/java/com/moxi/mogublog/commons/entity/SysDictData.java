package com.moxi.mogublog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;


/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author 陌溪
 * @since 2020年2月15日19:58:46
 */
@Data
@TableName("t_sys_dict_data")
public class SysDictData extends SuperEntity<SysDictData> {

    /**
     * 自增键 oid
     */
    private Long oid;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 字典类型UID
     */
    private String dictTypeUid;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（1是 0否）,默认为0
     */
    private Integer isDefault;

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

    // 以下字段不存入数据库，封装为了前端使用

    /**
     * 字典类型
     */
    @TableField(exist = false)
    private SysDictType sysDictType;
}
