package com.moxi.mogublog.web.restapi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.BlogSearchService;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EBehavior;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/search")
@Api(value="搜索RestApi",tags={"SearchRestApi"})
public class SearchRestApi {
	
    @Autowired
    private BlogSearchService blogSearchService;
    
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	@Autowired
	private WebVisitService webVisitService;
    
    private static Logger log = LogManager.getLogger(SearchRestApi.class);

	@ApiOperation(value="搜索Blog", notes="搜索Blog")
    @GetMapping("/searchBlog")
    public String searchBlog(HttpServletRequest request,
                             @ApiParam(name = "keywords", value = "关键字",required = true)@RequestParam(required=true)String keywords) {
		
		if(StringUtils.isEmpty(keywords)) {
			return ResultUtil.result(SysConf.ERROR, "关键字不能为空");
		}
		
        Map<String,Object> map = blogSearchService.search(keywords);
        
		//增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.BLOG_SEARCH, null, keywords);
        
        return ResultUtil.result(SysConf.SUCCESS, map);

    }
	
	@ApiOperation(value="根据标签获取相关的博客", notes="根据标签获取相关的博客")
	@GetMapping("/searchBlogByTag")
	public String searchBlogByTag (HttpServletRequest request,
			@ApiParam(name = "tagUid", value = "博客标签UID",required = true) @RequestParam(name = "tagUid", required = true) String tagUid) {
		if(StringUtils.isEmpty(tagUid)) {
			return ResultUtil.result(SysConf.ERROR, "标签不能为空");
		} 
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();		
		queryWrapper.like(SQLConf.TagUid, tagUid);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		queryWrapper.excludeColumns(Blog.class, "content");
		List<Blog> list = blogService.list(queryWrapper);		
		for(Blog item : list) {
			//获取标签
			blogService.setTagByBlog(item);		
			//获取分类
			blogService.setSortByBlog(item);			
			//设置博客标题图
			setPhotoListByBlog(item);			
		}
		log.info("返回结果");
		
		//增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.BLOG_TAG, tagUid, null);
		
		return ResultUtil.result(SysConf.SUCCESS, list);
	}
	
	@ApiOperation(value="根据分类获取相关的博客", notes="根据标签获取相关的博客")
	@GetMapping("/searchBlogBySort")
	public String searchBlogBySort (HttpServletRequest request,
			@ApiParam(name = "blogSortUid", value = "博客分类UID",required = true) @RequestParam(name = "blogSortUid", required = true) String blogSortUid) {
		if(StringUtils.isEmpty(blogSortUid)) {
			return ResultUtil.result(SysConf.ERROR, "uid不能为空");
		} 
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

		queryWrapper.eq(SQLConf.BLOG_SORT_UID, blogSortUid);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		queryWrapper.excludeColumns(Blog.class, "content");
		List<Blog> list = blogService.list(queryWrapper);		
		for(Blog item : list) {
			//获取标签
			blogService.setTagByBlog(item);		
			//获取分类
			blogService.setSortByBlog(item);			
			//设置博客标题图
			setPhotoListByBlog(item);			
		}
		log.info("返回结果");
		
		//增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.BLOG_SORT, blogSortUid, null);
		
		return ResultUtil.result(SysConf.SUCCESS, list);
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
