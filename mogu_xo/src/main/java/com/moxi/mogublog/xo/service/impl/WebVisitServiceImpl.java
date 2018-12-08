package com.moxi.mogublog.xo.service.impl;

import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.mapper.WebVisitMapper;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;

/**
 * <p>
 * 用户访问记录表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class WebVisitServiceImpl extends SuperServiceImpl<WebVisitMapper, WebVisit> implements WebVisitService {

	@Override
	public void addWebVisit(String userUid, String ip, String behavior, String moduleUid, String otherData) {
		
		//增加记录（可以考虑使用AOP）
		WebVisit webVisit = new WebVisit();
		webVisit.setIp(ip);
		webVisit.setUserUid(userUid);
		webVisit.setBehavior(behavior);		
		webVisit.setModuleUid(moduleUid);
		webVisit.setOtherData(otherData);
		webVisit.insert();
	}

}
