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
	
	
	public void addWebVisit(String userUid, String ip, String behavior, String moduleUid, String otherData);
}
