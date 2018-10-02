package com.moxi.mogublog.xo.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
public interface BlogService extends SuperService<Blog> {
	
	/**
	 * 给博客列表设置标签
	 * @return
	 */
	public List<Blog> setTagByBlogList(List<Blog> list);
	
	/**
	 * 给博客设置标签
	 * @param blog
	 * @return
	 */
	public Blog setTagByBlog(Blog blog);
	
	/**
	 * 给博客设置分类
	 * @param blog
	 * @return
	 */
	public Blog setSortByBlog(Blog blog);
	
	/**
	 * 通过推荐等级获取博客列表
	 * @param level
	 * @return
	 */
	public List<Blog> getBlogListByLevel(Integer level);
	
	/**
	 * 通过推荐等级获取博客Page
	 * @param level
	 * @return
	 */
	public IPage<Blog> getBlogPageByLevel(Page<Blog> page, Integer level);
}
