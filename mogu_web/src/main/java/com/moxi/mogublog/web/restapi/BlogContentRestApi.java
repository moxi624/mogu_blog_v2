package com.moxi.mogublog.web.restapi;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	private static Logger log = LogManager.getLogger(BlogContentRestApi.class);
	
	@ApiOperation(value="通过Uid获取博客内容", notes="通过Uid获取博客内容")
	@GetMapping("/getBlogByUid")
	public String getBlogByUid (HttpServletRequest request,
			@ApiParam(name = "uid", value = "博客UID", required = false) @RequestParam(name = "uid", required = false) String uid			) {
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "UID不能为空");
		}

		Blog blog = blogService.getById(uid);
		
		if(blog != null) {
			blogService.setTagByBlog(blog);	
			
			//获取分类
			blogService.setSortByBlog(blog);	
			
			//设置博客标题图
			setPhotoListByBlog(blog);	
		}
		log.info("返回结果");		
		return ResultUtil.result(SysConf.SUCCESS, blog);
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

