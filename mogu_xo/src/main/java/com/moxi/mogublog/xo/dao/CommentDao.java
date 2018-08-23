package com.moxi.mogublog.xo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mougblog.base.dao.BaseDao;

/**
 * 评论Dao
 * @author xuzhxiang
 * @date 2017年11月3日18:54:56
 *
 */
@Repository
public interface CommentDao extends BaseDao<Comment>{
	/**
	 * ͨ通过map获取一级评论
	 * @param map
	 * @return
	 */
	public List<Comment> getFirstList(Map<String, Object> map);
}
