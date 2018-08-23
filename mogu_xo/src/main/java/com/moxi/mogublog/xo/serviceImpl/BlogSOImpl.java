package com.moxi.mogublog.xo.serviceImpl;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.BlogSO;
import com.moxi.mougblog.base.serviceImpl.BaseSOImpl;

/**
 * 博客Service实现
 * @author xuzhixinag
 * @date 2017年9月16日14:57:01
 *
 */
@Service
@ComponentScan({"com.moxi.mogublog.xo.dao"})
public class BlogSOImpl extends BaseSOImpl<Blog> implements BlogSO{
	
}
