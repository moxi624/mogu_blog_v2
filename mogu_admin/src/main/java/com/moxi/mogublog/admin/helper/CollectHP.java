package com.moxi.mogublog.admin.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Collect;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.BlogSO;
import com.moxi.mogublog.xo.service.CollectSO;
import com.moxi.mogublog.xo.service.UserSO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.helper.BaseHP;

/**
 * 收藏管理HP
 * @author xuzhixiang
 * @date 2017年12月28日17:43:02
 *
 */
public class CollectHP extends BaseHP{
	
	static CollectSO collectSO;
	
	static UserSO userSO;
	
	static BlogSO blogSO;
	
	private static CollectSO getCollectSO () {
		if (collectSO == null) {
			collectSO = getApplicationContext().getBean(CollectSO.class);
		}
		return collectSO;
	}
	
	private static UserSO getUserSO () {
		if (userSO == null) {
			userSO = getApplicationContext().getBean(UserSO.class);
		}
		return userSO;
	}
	
	private static BlogSO getBlogSO () {
		if (blogSO == null) {
			blogSO = getApplicationContext().getBean(BlogSO.class);
		}
		return blogSO;
	}
	
	/**
	 * 获取收藏list
	 * @return
	 */
	public static Map<String, Object> getList(Map<String, Object> model) {
		User user = null;
		Blog blog = null;
		Map<String, Object> map = getMap();
		map.put(SysConf.status, EStatus.ENABLE);
		List<Collect> list= getCollectSO().getList(map);
		for(Collect collect : list) {		
			user = getUserSO().get(collect.getUseruid());
			blog = getBlogSO().get(collect.getBloguid());
			if(user != null && blog != null) {
				collect.setUsername(user.getUsername());
				collect.setCollectname(blog.getTitle());
			}			
		}
		model.put(SysConf.rows, list);
		model.put(SysConf.total, list.size());
		return model;
	}
	
	/**
	 * 搜索收藏list
	 * @author xuzhixiang
	 * @date 2017年12月28日17:42:57
	 * @return
	 */
	public static Map<String, Object> getSearchList(Map<String, Object> model, String keyword) {
		//TODO  增加收藏列表的关键字查询
		User user = null;
		Blog blog = null;
		Map<String, Object> map = getMap();
		if(StrUtils.isNotEmpty(keyword)) {
			map.put(SysConf.keyword, keyword);
		}	
		List<Collect> list= getCollectSO().getList(map);
		List<Collect> newList = new ArrayList<Collect>();
		for(Collect collect : list) {
			if(collect.getStatus() == EStatus.DISABLED) {
				continue;
			}
			user = getUserSO().get(collect.getUseruid());
			blog = getBlogSO().get(collect.getBloguid());
			if(user != null) {
				collect.setUsername(user.getUsername());				
			}
			if(blog != null) {
				collect.setCollectname(blog.getTitle());
			}
			newList.add(collect);
		}
		model.put(SysConf.rows, newList);
		model.put(SysConf.total, newList.size());
		return model;
	}
}
