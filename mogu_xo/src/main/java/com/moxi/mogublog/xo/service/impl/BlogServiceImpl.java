package com.moxi.mogublog.xo.service.impl;

import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.mapper.BlogMapper;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class BlogServiceImpl extends SuperServiceImpl<BlogMapper, Blog> implements BlogService {

}
