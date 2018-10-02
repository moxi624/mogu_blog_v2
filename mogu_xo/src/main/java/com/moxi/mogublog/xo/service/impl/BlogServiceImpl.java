package com.moxi.mogublog.xo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.mapper.BlogMapper;
import com.moxi.mogublog.xo.mapper.BlogSortMapper;
import com.moxi.mogublog.xo.mapper.TagMapper;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.enums.EStatus;
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
	
	@Autowired
	TagMapper tagMapper;
	
	@Autowired
	BlogSortMapper blogSortMapper;
	
	@Autowired
	BlogMapper blogMapper;
	
	@Override
	public List<Blog> setTagByBlogList(List<Blog> list) {
		for(Blog item : list) {
			if(item != null) {
				setTagByBlog(item);	
			}			
		}
		return list;
	}

	@Override
	public Blog setTagByBlog(Blog blog) {
		String tagUid = blog.getTagUid();
		if(!StringUtils.isEmpty(tagUid)) {			
			String uids[] = tagUid.split(",");
			List<Tag> tagList = new ArrayList<Tag>();
			for(String uid : uids) {
				Tag  tag = tagMapper.selectById(uid);
				if(tag != null && tag.getStatus() != EStatus.DISABLED) {
					tagList.add(tag);						
				}
			}
			blog.setTagList(tagList);
		}
		return blog;
	}

	@Override
	public Blog setSortByBlog(Blog blog) {
		
		if(blog != null && !StringUtils.isEmpty(blog.getBlogSortUid())) {
			BlogSort blogSort = blogSortMapper.selectById(blog.getBlogSortUid());
			blog.setBlogSort(blogSort);
		}		
		return blog;
	}

	@Override
	public List<Blog> getBlogListByLevel(Integer level) {
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("level", level);
		List<Blog> list = blogMapper.selectList(queryWrapper);		
		return list;
	}

	@Override
	public IPage<Blog> getBlogPageByLevel(Page<Blog> page, Integer level) {
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("level", level);		
		return blogMapper.selectPage(page, queryWrapper);
	}

}
