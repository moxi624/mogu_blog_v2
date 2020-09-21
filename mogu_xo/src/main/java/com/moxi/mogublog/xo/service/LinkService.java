package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.mogublog.commons.entity.Link;
import com.moxi.mogublog.xo.vo.LinkVO;
import com.moxi.mougblog.base.service.SuperService;

import java.util.List;

/**
 * 标签表 服务类
 *
 * @author 陌溪
 * @date 2018-09-08
 */
public interface LinkService extends SuperService<Link> {

    /**
     * 通过页大小获取友链列表
     *
     * @param pageSize
     * @return
     */
    public List<Link> getListByPageSize(Integer pageSize);

    /**
     * 获取友链列表
     *
     * @param linkVO
     * @return
     */
    public IPage<Link> getPageList(LinkVO linkVO);

    /**
     * 新增友链
     *
     * @param linkVO
     */
    public String addLink(LinkVO linkVO);

    /**
     * 编辑友链
     *
     * @param linkVO
     */
    public String editLink(LinkVO linkVO);

    /**
     * 删除友链
     *
     * @param linkVO
     */
    public String deleteLink(LinkVO linkVO);

    /**
     * 置顶友链
     *
     * @param linkVO
     */
    public String stickLink(LinkVO linkVO);

    /**
     * 点击友链
     *
     * @return
     */
    public String addLinkCount(String uid);
}
