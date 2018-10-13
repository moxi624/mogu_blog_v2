package com.moxi.mogublog.web.restapi;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.ELevel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 首页 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/index")
@Api(value="首页RestApi",tags={"IndexRestApi"})
public class IndexRestApi {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	BlogSortService blogSortService;
	
	@Autowired
	LinkService linkService;
		
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	@Value(value="${BLOG.HOT_COUNT}")
	private Integer BLOG_HOT_COUNT;
	
	@Value(value="${BLOG.NEW_COUNT}")
	private Integer BLOG_NEW_COUNT;
	
	@Value(value="${BLOG.FIRST_COUNT}")
	private Integer BLOG_FIRST_COUNT;
	
	@Value(value="${BLOG.SECOND_COUNT}")
	private Integer BLOG_SECOND_COUNT;
	
	@Value(value="${BLOG.THIRD_COUNT}")
	private Integer BLOG_THIRD_COUNT;
	
	@Value(value="${BLOG.FOURTH_COUNT}")
	private Integer BLOG_FOURTH_COUNT;
	
	private static Logger log = LogManager.getLogger(IndexRestApi.class);
	
	@ApiOperation(value="通过推荐等级获取博客列表", notes="通过推荐等级获取博客列表")
	@GetMapping("/getBlogByLevel")
	public String getBlogByLevel (HttpServletRequest request,
			@ApiParam(name = "level", value = "推荐等级",required = false) @RequestParam(name = "level", required = false, defaultValue = "0") Integer level,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage) {
		
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		switch(level) {
			case ELevel.NORMAL: { page.setSize(BLOG_NEW_COUNT);} break;
			case ELevel.FIRST: { page.setSize(BLOG_FIRST_COUNT);} break;
			case ELevel.SECOND: { page.setSize(BLOG_SECOND_COUNT);} break;
			case ELevel.THIRD: { page.setSize(BLOG_THIRD_COUNT);} break;
			case ELevel.FOURTH: { page.setSize(BLOG_FOURTH_COUNT);} break;
		}
		IPage<Blog> pageList = blogService.getBlogPageByLevel(page, level);
		List<Blog> list = pageList.getRecords();		
		for(Blog item : list) {
			//获取标签
			blogService.setTagByBlog(item);			
			//获取分类
			blogService.setSortByBlog(item);			
			//设置博客标题图
			setPhotoListByBlog(item);
			
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="获取首页排行博客", notes="获取首页排行博客")
	@GetMapping("/getHotBlog")
	public String getHotBlog (HttpServletRequest request) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(0);
		page.setSize(BLOG_HOT_COUNT);
		queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();		
		for(Blog item : list) {
			//获取标签
			blogService.setTagByBlog(item);
			
			//获取分类
			blogService.setSortByBlog(item);
			
			//设置博客标题图
			setPhotoListByBlog(item);
			
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}

	@ApiOperation(value="获取首页最新的博客", notes="获取首页最新的博客")
	@GetMapping("/getNewBlog")
	public String getNewBlog (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();		
		for(Blog item : list) {
			//获取标签
			blogService.setTagByBlog(item);		
			//获取分类
			blogService.setSortByBlog(item);			
			//设置博客标题图
			setPhotoListByBlog(item);			
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="获取最热标签", notes="获取最热标签")
	@GetMapping("/getHotTag")
	public String getHotTag (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		Page<Tag> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
		IPage<Tag> pageList = tagService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="获取友情链接", notes="获取友情链接")
	@GetMapping("/getLink")
	public String getLink (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Link> queryWrapper = new QueryWrapper<Link>();
		Page<Link> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
		IPage<Link> pageList = linkService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	/**
	 * 设置博客标题图
	 * @param blog
	 */
	private void setPhotoListByBlog(Blog blog) {
		//获取标题图片
		if(blog != null && !StringUtils.isEmpty(blog.getFileUid())) {				
			String result = this.pictureFeignClient.getPicture(blog.getFileUid(), ",");
			List<String> picList = WebUtils.getPicture(result);
			log.info("##### picList: #######" + picList);
			if(picList != null && picList.size() > 0) {
				blog.setPhotoList(picList); 
			}
		}
	}
	
}

