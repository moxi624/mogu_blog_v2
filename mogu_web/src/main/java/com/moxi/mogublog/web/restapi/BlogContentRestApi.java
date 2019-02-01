package com.moxi.mogublog.web.restapi;


import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EBehavior;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 文章详情 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/content")
@Api(value="文章详情RestApi",tags={"BlogContentRestApi"})
public class BlogContentRestApi {
	
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
	
	@Autowired
	private WebVisitService webVisitService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private static Logger log = LogManager.getLogger(BlogContentRestApi.class);
	
	@ApiOperation(value="通过Uid获取博客内容", notes="通过Uid获取博客内容")
	@GetMapping("/getBlogByUid")
	public String getBlogByUid (HttpServletRequest request,
			@ApiParam(name = "uid", value = "博客UID", required = false) @RequestParam(name = "uid", required = false) String uid			) {
		
		String ip = IpUtils.getIpAddr(request);
				
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "UID不能为空");
		}
	
		Blog blog = blogService.getById(uid);
			
		
		if(blog != null) {
			
			//设置博客标签
			blogService.setTagByBlog(blog);				
			
			//获取分类
			blogService.setSortByBlog(blog);				
			
			//设置博客标题图
			setPhotoListByBlog(blog);
			
			//从Redis取出数据，判断该用户是否点击过
			String jsonResult = stringRedisTemplate.opsForValue().get("BLOG_CLICK:" + ip + "#" + uid);
						
			if(StringUtils.isEmpty(jsonResult)) {
				
				//给博客点击数增加
				blogService.addBlogClickCount(blog);
				
			    //将该用户点击记录存储到redis中, 24小时后过期	
				stringRedisTemplate.opsForValue().set("BLOG_CLICK:" + ip + "#" + uid, blog.getClickCount().toString(),
						24, TimeUnit.HOURS);											
			}
			
			//增加记录（可以考虑使用AOP）
	        webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.BLOG_CONTNET, blog.getUid(), null);
			
		}
		
		log.info("返回结果");		
		return ResultUtil.result(SysConf.SUCCESS, blog);
	}
	
	@ApiOperation(value="通过Uid给博客点赞", notes="通过Uid给博客点赞")
	@GetMapping("/praiseBlogByUid")
	public String praiseBlogByUid (HttpServletRequest request,
			@ApiParam(name = "uid", value = "博客UID", required = false) @RequestParam(name = "uid", required = false) String uid			) {
		
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "UID不能为空");
		}
		
		Blog blog = blogService.getById(uid);
		
		//从Redis取出数据，判断该用户是否点击过
		String pariseJsonResult = stringRedisTemplate.opsForValue().get("BLOG_PRAISE:" + uid);
		
		if(StringUtils.isEmpty(pariseJsonResult)) {

		    //给该博客点赞数置为1
			stringRedisTemplate.opsForValue().set("BLOG_PRAISE:" + uid, "1");
			
		} else {
			Integer count = Integer.valueOf(pariseJsonResult) + 1;
			
			//给该博客点赞 +1
			stringRedisTemplate.opsForValue().set("BLOG_PRAISE:" + uid, count.toString());
		}
		
		//增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.BLOG_CONTNET, blog.getUid(), null);
        
		return ResultUtil.result(SysConf.SUCCESS, "");
	}
	
	@ApiOperation(value="根据标签获取相关的博客", notes="根据标签获取相关的博客")
	@GetMapping("/getSameBlog")
	public String getNewBlog (HttpServletRequest request,
			@ApiParam(name = "tagUid", value = "博客标签UID",required = true) @RequestParam(name = "tagUid", required = true) String tagUid,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		if(StringUtils.isEmpty(tagUid)) {
			return ResultUtil.result(SysConf.ERROR, "标签不能为空");
		} 
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.eq(SQLConf.TagUid, tagUid);
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

