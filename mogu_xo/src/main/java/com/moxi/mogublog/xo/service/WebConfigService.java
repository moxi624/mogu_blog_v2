package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.commons.entity.WebConfig;
import com.moxi.mogublog.xo.vo.WebConfigVO;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 网站配置表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月11日14:55:05
 */
public interface WebConfigService extends SuperService<WebConfig> {

    /**
     * 获取网站配置
     *
     * @return
     */
    public WebConfig getWebConfig();

    /**
     * 通过显示列表获取配置
     * @return
     */
    public WebConfig getWebConfigByShowList();

    /**
     * 修改网站配置
     *
     * @param webConfigVO
     * @return
     */
    public String editWebConfig(WebConfigVO webConfigVO);
}
