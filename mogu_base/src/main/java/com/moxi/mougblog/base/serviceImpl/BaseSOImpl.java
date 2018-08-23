package com.moxi.mougblog.base.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moxi.mougblog.base.dao.BaseDao;
import com.moxi.mougblog.base.service.BaseSO;


/**
 * SO实现基类
 * @author xuzhixiang
 * @date 2017年9月16日14:55:27
 * @param <T>
 */
@Service
public class BaseSOImpl<T> implements BaseSO<T>{
	
	@Autowired
	BaseDao<T> dao;	
	
	public T get(String uid) {
		return dao.get(uid);
	}

	public T getOne(Map<String, Object> map) {
		return dao.getOne(map);
	}

	public int getCount(Map<String, Object> map) {
		return dao.getCount(map);
	}

	public List<T> getList(Map<String, Object> map) {
		return dao.getList(map);
	}

	public boolean insert(T t) {
		return dao.insert(t);
	}

	public boolean update(T t) {
		return dao.update(t);
	}

	public boolean updateFields(Map<String, Object> map) {
		return dao.updateFields(map);
	}

	public boolean delete(String uid) {
		return dao.delete(uid);
	}

}
