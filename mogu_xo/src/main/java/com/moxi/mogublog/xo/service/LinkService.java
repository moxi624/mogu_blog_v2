package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mougblog.base.service.SuperService;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
public interface LinkService extends SuperService<Link> {

    /**
     * 通过页大小获取标签列表
     * @param pageSize
     * @return
     */
    public List<Link> getListByPageSize(Integer pageSize);
}
