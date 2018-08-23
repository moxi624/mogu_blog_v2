package com.moxi.mogublog.xo.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.dao.VisitorDao;
import com.moxi.mogublog.xo.entity.Visitor;
import com.moxi.mogublog.xo.service.VisitorSO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.SysConf;
import com.moxi.mougblog.base.serviceImpl.BaseSOImpl;

/**
 * 游客Service实现
 * @author xuzhixinag
 * @date 2017年9月16日14:57:01
 *
 */
@Service
public class VisitorSOImpl extends BaseSOImpl<Visitor> implements VisitorSO{
	
	@Autowired
	VisitorDao dao;
	
	/**
	 * 通过name获取专辑uid
	 */
	public String getSpecialUidByName(String specialname) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(SysConf.status, EStatus.ENABLE);
		map.put(SysConf.specialname , specialname);
		Visitor special = dao.getOne(map);
		if(special != null) {
			return special.getUid();
		}
		return null;
	}
}
