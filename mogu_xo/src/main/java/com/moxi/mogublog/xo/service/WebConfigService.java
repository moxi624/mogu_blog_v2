package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.commons.entity.WebConfig;
import com.moxi.mogublog.xo.vo.WebConfigVO;
import com.moxi.mougblog.base.service.SuperService;

/**
 * 网站配置表 服务类
 *
 * @author 陌溪
 * @date 2018年11月11日14:55:05
 */
public interface WebConfigService extends SuperService<WebConfig> {

    /**
     * 获取网站配置
     *
     * @return
     */
    WebConfig getWebConfig();

    /**
     * 获取网站名称
     * @return
     */
    String getWebSiteName();

    /**
     * 通过显示列表获取配置
     *
     * @return
     */
    WebConfig getWebConfigByShowList();

    /**
     * 修改网站配置
     *
     * @param webConfigVO
     * @return
     */
    String editWebConfig(WebConfigVO webConfigVO);

    /**
     * 是否开启该登录方式【账号密码、码云、Github、QQ、微信】
     * @param loginType
     * @return
     */
    Boolean isOpenLoginType(String loginType);
}
