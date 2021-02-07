package com.moxi.mogublog.xo.vo;

import com.moxi.mogublog.commons.entity.BlogSort;
import com.moxi.mogublog.commons.entity.Tag;
import com.moxi.mougblog.base.validator.annotion.IntegerNotNull;
import com.moxi.mougblog.base.validator.annotion.NotBlank;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * BlogVO
 *
 * @author: 陌溪
 * @create: 2019年12月4日12:26:36
 */
@Data
public class BlogVO extends BaseVO<BlogVO> {

    /**
     * 博客标题
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String title;

    /**
     * 博客简介
     */
    private String summary;

    /**
     * 博客内容
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String content;

    /**
     * 标签uid
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String tagUid;
    /**
     * 博客分类UID
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String blogSortUid;
    /**
     * 标题图片UID
     */
    private String fileUid;
    /**
     * 管理员UID
     */
    private String adminUid;
    /**
     * 是否发布
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String isPublish;
    /**
     * 是否原创
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String isOriginal;
    /**
     * 如果原创，作者为管理员名
     */
    @NotBlank(groups = {Update.class})
    private String author;
    /**
     * 文章出处
     */
    private String articlesPart;
    /**
     * 推荐级别，用于首页推荐
     * 0：正常
     * 1：一级推荐(轮播图)
     * 2：二级推荐(top)
     * 3：三级推荐 ()
     * 4：四级 推荐 (特别推荐)
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer level;

    /**
     * 类型【0 博客， 1：推广】
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String type;

    /**
     * 外链【如果是推广，那么将跳转到外链】
     */
    private String outsideLink;

    /**
     * 标签,一篇博客对应多个标签
     */
    private List<Tag> tagList;

    // 以下字段不存入数据库，封装为了方便使用
    /**
     * 标题图
     */
    private List<String> photoList;
    /**
     * 博客分类
     */
    private BlogSort blogSort;
    /**
     * 点赞数
     */
    private Integer praiseCount;
    /**
     * 版权申明
     */
    private String copyright;

    /**
     * 博客等级关键字，仅用于getList
     */
    private String levelKeyword;

    /**
     * 使用Sort字段进行排序 （0：不使用， 1：使用），默认为0
     */
    private Integer useSort;

    /**
     * 排序字段，数值越大，越靠前
     */
    private Integer sort;

    /**
     * 是否开启评论(0:否， 1:是)
     */
    private String openComment;

    /**
     * OrderBy排序字段（desc: 降序）
     */
    private String orderByDescColumn;

    /**
     * OrderBy排序字段（asc: 升序）
     */
    private String orderByAscColumn;

    /**
     * 无参构造方法，初始化默认值
     */
    BlogVO() {
        this.level = 0;
        this.useSort = 0;
    }
}
