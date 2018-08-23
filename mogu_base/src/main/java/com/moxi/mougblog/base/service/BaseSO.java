package com.moxi.mougblog.base.service;

import java.util.List;
import java.util.Map;

/**
 * SO基类
 * @author xuzhixiang
 * @date 2017年9月16日14:42:44
 *
 * @param <T>
 */
public interface BaseSO<T> {
	/**
	 * ͨ通过uid获取T
	 * @param uid
	 * @return
	 */
	public T get(String uid);
	
	/**
	 * ͨgetOne
	 * @param map
	 * @return
	 */
	public T getOne(Map<String, Object> map);
	
	/**
	 * ͨ获取数量
	 * @param map
	 * @return
	 */
	public int getCount(Map<String, Object> map);
	
	/**
	 * 获取list
	 * @param map
	 * @return
	 */
	public List<T> getList(Map<String, Object> map);
	
	/**
	 * 插入T
	 * @param map
	 * @return
	 */
	public boolean insert(T t);
	
	/**
	 * 更新T
	 * @param t
	 * @return
	 */
	public boolean update(T t);
	
	/**
	 * 通过map更新T
	 * @param map
	 * @return
	 */
	public boolean updateFields(Map<String, Object> map);
	
	/**
	 * 删除T
	 * @param uid
	 * @return
	 */
	public boolean delete(String uid);
}
