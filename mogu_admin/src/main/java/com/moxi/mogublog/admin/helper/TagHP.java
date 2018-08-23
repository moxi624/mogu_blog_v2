package com.moxi.mogublog.admin.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.TagSO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.helper.BaseHP;
import com.moxi.mougblog.base.jedis.JedisClient;

/**
 * 标签管理HP
 * @author xuzhixiang
 * @date 2017年12月28日10:24:41
 *
 */
public class TagHP extends BaseHP{
	
	static TagSO tagSO;
	static JedisClient jedisClient;
		
	private static TagSO getTagSO () {
		if (tagSO == null) {
			tagSO = getApplicationContext().getBean(TagSO.class);
		}
		return tagSO;
	}
	private static JedisClient getJedisClient () {
		if (jedisClient == null) {
			jedisClient = getApplicationContext().getBean(JedisClient.class);
		}
		return jedisClient;
	}
	
	/**
	 * 获取标签list
	 * @return
	 */
	public static Map<String, Object> getList(Map<String, Object> model) {
		Map<String, Object> map = getMap();
		map.put(SysConf.status, EStatus.ENABLE);
		List<Tag> list= getTagSO().getList(map);
		model.put(SysConf.rows, list);
		model.put(SysConf.total, list.size());
		return model;
	}
	
	/**
	 * 搜索歌手list
	 * @author xuzhixiang
	 * @date 2017年10月2日13:32:35
	 * @return
	 */
	public static Map<String, Object> getSearchList(Map<String, Object> model, String keyword) {
		Map<String, Object> map = getMap();
		if(StrUtils.isNotEmpty(keyword)) {
			map.put(SysConf.keyword, keyword);
		}	
		List<Tag> list= getTagSO().getList(map);
		List<Tag> newlist= new ArrayList<Tag>();
		for(Tag tag : list) {
			if(tag.getStatus() != EStatus.DISABLED) {
				newlist.add(tag);
			}
		}
		model.put(SysConf.rows, newlist);
		model.put(SysConf.total, newlist.size());
		return model;
	}
	
	/**
	 * 清空Redis中String类型的缓存
	 * @param key
	 */
	public static void emptyRedisString(String key) {
		getJedisClient().del(key);
	}
	
	/**
	 * 清空Redis中Hash类型的缓存
	 * @param hashtable
	 * @param key
	 */
	public static void emptyRedisHash(String hashtable, String key) {
		getJedisClient().hdel(hashtable, key);
	}
}
