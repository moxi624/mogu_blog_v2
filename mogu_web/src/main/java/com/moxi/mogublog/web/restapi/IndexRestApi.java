package com.moxi.mogublog.web.restapi;


import java.util.ArrayList;
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
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.EStatus;

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
	private PictureFeignClient pictureFeignClient;
	
	private static Logger log = LogManager.getLogger(IndexRestApi.class);
	
	@ApiOperation(value="获取首页Banner数据", notes="获取首页Banner数据")
	@GetMapping("/getBanner")
	public String getBanner (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();		
		for(Blog item : list) {
			//获取标签
			if(item != null && !StringUtils.isEmpty(item.getTagUid())) {
				String uids[] = item.getTagUid().split(",");
				List<Tag> tagList = new ArrayList<Tag>();
				for(String uid : uids) {
					Tag  tag = tagService.getById(uid);
					if(tag != null && tag.getStatus() != EStatus.DISABLED) {
						tagList.add(tag);						
					}
				}
				item.setTagList(tagList);
			}
			
			//获取标签
			if(item != null && !StringUtils.isEmpty(item.getBlogSortUid())) {
				
				BlogSort blogSort = blogSortService.getById(item.getBlogSortUid());
				item.setBlogSort(blogSort);
			}
			
			//获取标题图片
			if(item != null && !StringUtils.isEmpty(item.getFileUid())) {				
				String result = this.pictureFeignClient.getPicture(item.getFileUid(), ",");
				List<String> picList = WebUtils.getPicture(result);
				log.info("##### picList: #######" + picList);
				if(picList != null && picList.size() > 0) {
					item.setPhotoList(picList); 
				}
			}
			
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="获取首页推荐数据", notes="获取首页推荐数据")
	@GetMapping("/getTopic")
	public String getTopic (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "2") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();		
		for(Blog item : list) {
			//获取标签
			if(item != null && !StringUtils.isEmpty(item.getTagUid())) {
				String uids[] = item.getTagUid().split(",");
				List<Tag> tagList = new ArrayList<Tag>();
				for(String uid : uids) {
					Tag  tag = tagService.getById(uid);
					if(tag != null && tag.getStatus() != EStatus.DISABLED) {
						tagList.add(tag);						
					}
				}
				item.setTagList(tagList);
			}
			
			//获取标签
			if(item != null && !StringUtils.isEmpty(item.getBlogSortUid())) {
				
				BlogSort blogSort = blogSortService.getById(item.getBlogSortUid());
				item.setBlogSort(blogSort);
			}
			
			//获取标题图片
			if(item != null && !StringUtils.isEmpty(item.getFileUid())) {				
				String result = this.pictureFeignClient.getPicture(item.getFileUid(), ",");
				List<String> picList = WebUtils.getPicture(result);
				log.info("##### picList: #######" + picList);
				if(picList != null && picList.size() > 0) {
					item.setPhotoList(picList); 
				}
			}
			
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
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();		
		for(Blog item : list) {
			//获取标签
			if(item != null && !StringUtils.isEmpty(item.getTagUid())) {
				String uids[] = item.getTagUid().split(",");
				List<Tag> tagList = new ArrayList<Tag>();
				for(String uid : uids) {
					Tag  tag = tagService.getById(uid);
					if(tag != null && tag.getStatus() != EStatus.DISABLED) {
						tagList.add(tag);						
					}
				}
				item.setTagList(tagList);
			}
			
			//获取标签
			if(item != null && !StringUtils.isEmpty(item.getBlogSortUid())) {
				
				BlogSort blogSort = blogSortService.getById(item.getBlogSortUid());
				item.setBlogSort(blogSort);
			}
			
			//获取标题图片
			if(item != null && !StringUtils.isEmpty(item.getFileUid())) {				
				String result = this.pictureFeignClient.getPicture(item.getFileUid(), ",");
				List<String> picList = WebUtils.getPicture(result);
				log.info("##### picList: #######" + picList);
				if(picList != null && picList.size() > 0) {
					item.setPhotoList(picList); 
				}
			}
			
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	
}

