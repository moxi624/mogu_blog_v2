package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.mogublog.commons.entity.SysLog;
import com.moxi.mogublog.xo.vo.SysLogVO;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
public interface SysLogService extends SuperService<SysLog> {

    /**
     * 获取操作日志列表
     *
     * @param sysLogVO
     * @return
     */
    public IPage<SysLog> getPageList(SysLogVO sysLogVO);
}
