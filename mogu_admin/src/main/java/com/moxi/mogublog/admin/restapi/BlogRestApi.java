package com.moxi.mogublog.admin.restapi;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
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
 * 博客表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@RestController
@RequestMapping("/blog")
@Api(value="博客RestApi", tags={"BlogRestApi"})
public class BlogRestApi {
	
	@Autowired
	BlogService blogService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	BlogSortService blogSortService;
	
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	@Value(value="${data.image.url}")
	private String IMG_HOST;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
	
	@ApiOperation(value="获取博客列表", notes="获取博客列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();
		if(!StringUtils.isEmpty(keyword)) {
			queryWrapper.like(SQLConf.TITLE, keyword);
		}
		
		//分页 
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		
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
	
	@ApiOperation(value="增加博客", notes="增加博客", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "title", value = "博客标题",required = true) @RequestParam(name = "title", required = true) String title,
			@ApiParam(name = "summary", value = "博客简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "content", value = "博客正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "tagUid", value = "标签uid",required = false) @RequestParam(name = "tagUid", required = false) String tagUid,
			@ApiParam(name = "clickCount", value = "点击数",required = false) @RequestParam(name = "clickCount", required = false, defaultValue = "1") Integer clickCount,
			@ApiParam(name = "collectCount", value = "收藏数",required = false) @RequestParam(name = "collectCount", required = false, defaultValue = "0") Integer collectCount,
			@ApiParam(name = "isOriginal", value = "是否原创",required = false) @RequestParam(name = "isOriginal", required = false, defaultValue = "1") String isOriginal,
			@ApiParam(name = "author", value = "作者",required = false) @RequestParam(name = "author", required = true) String author,
			@ApiParam(name = "articlesPart", value = "文章出处",required = false) @RequestParam(name = "articlesPart", required = false) String articlesPart,
			@ApiParam(name = "blogSortUid", value = "博客分类UID",required = false) @RequestParam(name = "blogSortUid", required = false) String blogSortUid,
			@ApiParam(name = "fileUid", value = "标题图Uid",required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {
		
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(author)|| StringUtils.isEmpty(blogSortUid)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
		Blog blog = new Blog();
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setContent(content);		
		blog.setTagUid(tagUid);
		blog.setBlogSortUid(blogSortUid);
		blog.setClickCount(clickCount);
		blog.setCollectCount(collectCount);
		blog.setFileUid(fileUid);
		blog.setAuthor(author);
		blog.setArticlesPart(articlesPart);
		blog.setIsOriginal(isOriginal);

		blog.setStatus(EStatus.ENABLE);
		blogService.save(blog);
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@ApiOperation(value="编辑博客", notes="编辑博客", response = String.class)
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid,
			@ApiParam(name = "title", value = "博客标题",required = false) @RequestParam(name = "title", required = false) String title,
			@ApiParam(name = "summary", value = "博客简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "content", value = "博客正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "tagUid", value = "标签UID",required = false) @RequestParam(name = "tagUid", required = false) String tagUid,
			@ApiParam(name = "blogSortUid", value = "博客分类UID",required = false) @RequestParam(name = "blogSortUid", required = false) String blogSortUid,
			@ApiParam(name = "clickCount", value = "点击数",required = false) @RequestParam(name = "clickCount", required = false) Integer clickCount,
			@ApiParam(name = "collectCount", value = "收藏数",required = false) @RequestParam(name = "collectCount", required = false, defaultValue = "0") Integer collectCount,
			@ApiParam(name = "isOriginal", value = "是否原创",required = false) @RequestParam(name = "isOriginal", required = false, defaultValue = "1") String isOriginal,
			@ApiParam(name = "author", value = "作者",required = false) @RequestParam(name = "author", required = true) String author,
			@ApiParam(name = "articlesPart", value = "文章出处",required = false) @RequestParam(name = "articlesPart", required = false) String articlesPart,
			@ApiParam(name = "fileUid", value = "标题图UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid ) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Blog blog = blogService.getById(uid);
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setContent(content);
		blog.setTagUid(tagUid);
		blog.setBlogSortUid(blogSortUid);
		blog.setFileUid(fileUid);
		blog.setClickCount(clickCount);
		blog.setCollectCount(collectCount);
		blog.setAuthor(author);
		blog.setIsOriginal(isOriginal);
		blog.setArticlesPart(articlesPart);
		blog.setStatus(EStatus.ENABLE);
		blog.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@ApiOperation(value="删除博客", notes="删除博客", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Blog blog = blogService.getById(uid);
		blog.setStatus(EStatus.DISABLED);		
		blog.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
		
}

