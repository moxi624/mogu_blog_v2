package com.moxi.mogublog.admin.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Visitor;
import com.moxi.mogublog.xo.service.VisitorSO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.helper.BaseHP;

/**
 * 游客管理HP
 * @author xuzhixiang
 * @date 2017年12月29日14:51:08
 *
 */
public class VisitorHP extends BaseHP{
	
	static VisitorSO visitorSO; 
	
	private static VisitorSO getVisitorSO () {
		if (visitorSO == null) {
			visitorSO = getApplicationContext().getBean(VisitorSO.class);
		}
		return visitorSO;
	}
	
	/**
	 * 获取用户list
	 * @return
	 */
	public static Map<String, Object> getList(Map<String, Object> model) {
		Map<String, Object> map = getMap();
		map.put(SysConf.status, EStatus.ENABLE);
		List<Visitor> list= getVisitorSO().getList(map);
		model.put(SysConf.rows, list);
		model.put(SysConf.total, list.size());
		return model;
	}
	
	/**
	 * 搜索用户list
	 * @author xuzhixiang
	 * @date 2017年12月29日14:32:13
	 * @return
	 */
	public static Map<String, Object> getSearchList(Map<String, Object> model, String keyword) {
		Map<String, Object> map = getMap();
		if(StrUtils.isNotEmpty(keyword)) {
			map.put(SysConf.keyword, keyword);
		}	
		List<Visitor> list= getVisitorSO().getList(map);
		List<Visitor> newList = new ArrayList<Visitor>();
		for(Visitor visitor : list) {
			if(visitor.getStatus() != EStatus.DISABLED) {
				newList.add(visitor);
			}
		}
		model.put(SysConf.rows, newList);
		model.put(SysConf.total, newList.size());
		return model;
	}
}
