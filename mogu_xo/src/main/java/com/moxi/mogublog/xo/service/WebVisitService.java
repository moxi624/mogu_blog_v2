package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * Web访问记录 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年12月8日09:42:05
 */
public interface WebVisitService extends SuperService<WebVisit> {
	
	/**
	 * 增加访问记录
	 * @param userUid
	 * @param ip
	 * @param behavior
	 * @param moduleUid
	 * @param otherData
	 */
	public void addWebVisit(String userUid, String ip, String behavior, String moduleUid, String otherData);
	
	/**
	 * 获取今日网站访问人数
	 * @return
	 */
	public int getWebVisitCount();
}
