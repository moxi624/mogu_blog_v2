package com.moxi.mogublog.xo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.mapper.WebVisitMapper;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
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
	
	@Autowired
	WebVisitMapper webVisitMapper; 
	
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

	@Override
	public int getWebVisitCount() {
		
		QueryWrapper<WebVisit> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
		// 获取今日开始和结束时间
		String startTime = DateUtils.getToDayStartTime();
		String endTime = DateUtils.getToDayEndTime();
		queryWrapper.between(BaseSQLConf.CREATE_TIME, startTime, endTime);
		List<WebVisit> list = webVisitMapper.selectList(queryWrapper);
		Set<String> ipSet = new HashSet<String>();		
		
		// 根据IP统计访问今日访问次数
		for (WebVisit webVisit : list) {
			if(!"".equals(webVisit.getIp())) {
				ipSet.add(webVisit.getIp());	
			}			
		}
		
		return ipSet.size();
	}

}
