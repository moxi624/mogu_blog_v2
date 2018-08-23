package com.moxi.mogublog.xo.service;

import java.util.List;
import java.util.Map;

import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mougblog.base.service.BaseSO;

/**
 * 评论SO
 * @author xuzhxiang
 * @date 2017年11月3日18:57:54
 *
 */
public interface CommentSO extends BaseSO<Comment>{
	/**
	 * 获取一级评论
	 * @param map
	 * @return
	 */
	public List<Comment> getFirstList(Map<String, Object> map);
}
