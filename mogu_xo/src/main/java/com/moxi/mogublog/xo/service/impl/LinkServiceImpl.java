package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mogublog.xo.mapper.LinkMapper;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mougblog.base.enums.ELinkStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 友链表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class LinkServiceImpl extends SuperServiceImpl<LinkMapper, Link> implements LinkService {

    @Resource
    LinkMapper linkMapper;

    @Override
    public List<Link> getListByPageSize(Integer pageSize) {
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        Page<Link> page = new Page<>();
        page.setCurrent(1);
        page.setSize(pageSize);
        queryWrapper.eq(BaseSQLConf.LINK_STATUS, ELinkStatus.PUBLISH);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(BaseSQLConf.SORT);
        IPage<Link> pageList = linkMapper.selectPage(page, queryWrapper);
        return pageList.getRecords();
    }
}
