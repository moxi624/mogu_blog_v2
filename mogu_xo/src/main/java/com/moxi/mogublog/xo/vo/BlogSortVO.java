package com.moxi.mogublog.xo.vo;

import com.moxi.mougblog.base.validator.annotion.NotBlank;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

/**
 * BlogSortVO
 *
 * @author: 陌溪
 * @create: 2019年12月6日12:56:08
 */
@Data
public class BlogSortVO extends BaseVO<BlogSortVO> {

    /**
     * 分类名
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String sortName;

    /**
     * 分类介绍
     */
    private String content;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 无参构造方法
     */
    BlogSortVO() {

    }

}
