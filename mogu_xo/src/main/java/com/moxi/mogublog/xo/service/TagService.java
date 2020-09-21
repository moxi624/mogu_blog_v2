package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.mogublog.commons.entity.Tag;
import com.moxi.mogublog.xo.vo.TagVO;
import com.moxi.mougblog.base.service.SuperService;

import java.util.List;

/**
 * 标签表 服务类
 *
 * @author 陌溪
 * @date 2018-09-08
 */
public interface TagService extends SuperService<Tag> {
    /**
     * 获取博客标签列表
     *
     * @param tagVO
     * @return
     */
    public IPage<Tag> getPageList(TagVO tagVO);

    /**
     * 获取全部博客标签列表
     *
     * @return
     */
    public List<Tag> getList();

    /**
     * 新增博客标签
     *
     * @param tagVO
     */
    public String addTag(TagVO tagVO);

    /**
     * 编辑博客标签
     *
     * @param tagVO
     */
    public String editTag(TagVO tagVO);

    /**
     * 批量删除博客标签
     *
     * @param tagVOList
     */
    public String deleteBatchTag(List<TagVO> tagVOList);

    /**
     * 置顶博客标签
     *
     * @param tagVO
     */
    public String stickTag(TagVO tagVO);

    /**
     * 通过点击量排序博客
     *
     * @return
     */
    public String tagSortByClickCount();

    /**
     * 通过引用量排序博客
     *
     * @return
     */
    public String tagSortByCite();

    /**
     * 获取热门标签
     *
     * @return
     */
    public IPage<Tag> getHotTag(Integer hotTagCount);

    /**
     * 获取一个排序最高的标签
     *
     * @return
     */
    public Tag getTopTag();
}
