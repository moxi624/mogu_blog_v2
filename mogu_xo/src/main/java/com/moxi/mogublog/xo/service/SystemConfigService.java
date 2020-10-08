package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.xo.vo.SystemConfigVO;
import com.moxi.mougblog.base.service.SuperService;

import java.util.List;

/**
 * 系统配置表 服务类
 *
 * @author 陌溪
 * @datge 2020年1月21日09:05:53
 */
public interface SystemConfigService extends SuperService<SystemConfig> {

    /**
     * 获取系统配置
     *
     * @return
     */
    public SystemConfig getConfig();

    /**
     * 通过Key前缀清空Redis缓存
     *
     * @param key
     * @return
     */
    public String cleanRedisByKey(List<String> key);

    /**
     * 修改系统配置
     *
     * @param systemConfigVO
     * @return
     */
    public String editSystemConfig(SystemConfigVO systemConfigVO);
}
