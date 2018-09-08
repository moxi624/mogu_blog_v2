package com.moxi.mogublog.xo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.mapper.BlogMapper;
import com.moxi.mogublog.xo.service.BlogService;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
