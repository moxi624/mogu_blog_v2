package com.moxi.mogublog.xo.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.dao.CommentDao;
import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.service.CommentSO;
import com.moxi.mougblog.base.serviceImpl.BaseSOImpl;

/**
 * 评论Service实现
 * @author xuzhixinag
 * @date 2017年11月3日19:00:12
 *
 */
@Service
public class CommentSOImpl extends BaseSOImpl<Comment> implements CommentSO{
	
	@Autowired
	CommentDao dao;
	
	public List<Comment> getFirstList(Map<String, Object> map) {
		return dao.getFirstList(map);
	}

}
