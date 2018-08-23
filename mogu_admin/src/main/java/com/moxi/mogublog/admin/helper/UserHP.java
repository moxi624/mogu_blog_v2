package com.moxi.mogublog.admin.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.UserSO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.helper.BaseHP;


/**
 * 用户管理HP
 * @author xuzhixiang
 * @date 2017年12月29日14:32:05
 *
 */
public class UserHP extends BaseHP{
	
	static UserSO userSO; 
	
	private static UserSO getUserSO () {
		if (userSO == null) {
			userSO = getApplicationContext().getBean(UserSO.class);
		}
		return userSO;
	}
	
	/**
	 * 获取用户list
	 * @return
	 */
	public static Map<String, Object> getList(Map<String, Object> model) {
		Map<String, Object> map = getMap();
		map.put(SysConf.status, EStatus.ENABLE);
		List<User> list= getUserSO().getList(map);
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
		List<User> list= getUserSO().getList(map);
		List<User> newList = new ArrayList<User>();
		for(User user : list) {
			if(user.getStatus() != EStatus.DISABLED) {
				newList.add(user);
			}
		}
		model.put(SysConf.rows, newList);
		model.put(SysConf.total, newList.size());
		return model;
	}
}
