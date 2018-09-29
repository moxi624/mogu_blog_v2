package com.moxi.mogublog.xo.service;

import java.util.Map;

/**
 * solr索引维护
 * @author limbo
 *
 */
public interface BlogSearchService {
	/**
	 * 搜索
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> search(String keywords);
	
	/**
	 * 初始化索引
	 * @param
	 * @return
	 */
	public void initIndex();
	
	/**
	 * 添加索引
	 * @param 
	 * @return
	 */
	public void addIndex(String uid, String title, String summary, String tagUid, String blogSortUid,
			String author);
	
	/**
	 * 更新索引
	 * @param 
	 * @return
	 */
	public void updateIndex(String id, String title, String summary, String tagUid, String blogSortUid,
			String author);
	
	/**
	 * 删除索引
	 * @param id
	 * @return
	 */
	public void deleteIndex(String id);
	
	/**
	 * 删除全部索引
	 * @param 
	 * @return
	 */
	public void deleteAllIndex();
}
