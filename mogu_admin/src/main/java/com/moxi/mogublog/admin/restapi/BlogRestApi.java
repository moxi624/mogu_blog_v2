package com.moxi.mogublog.admin.restapi;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.BlogSearchService;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.ELevel;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 博客表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
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
	AdminService adminService;
	
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value(value="${data.image.url}")
	private String IMG_HOST;
	
	@Value(value="${PROJECT_NAME}")
	private String PROJECT_NAME;
	
	@Value(value="${BLOG.FIRST_COUNT}")
	private Integer BLOG_FIRST_COUNT;
	
	@Value(value="${BLOG.SECOND_COUNT}")
	private Integer BLOG_SECOND_COUNT;
	
	@Value(value="${BLOG.THIRD_COUNT}")
	private Integer BLOG_THIRD_COUNT;
	
	@Value(value="${BLOG.FOURTH_COUNT}")
	private Integer BLOG_FOURTH_COUNT;
	
    @Autowired
    private BlogSearchService blogSearchService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);
		
	@ApiOperation(value="获取博客列表", notes="获取博客列表", response = String.class)	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(HttpServletRequest request,
			@ApiParam(name = "keyword", value = "关键字",required = false) @RequestParam(name = "keyword", required = false) String keyword,
			@ApiParam(name = "tagUid", value = "标签UID",required = false) @RequestParam(name = "tagUid", required = false) String tagUid,
			@ApiParam(name = "blogSortUid", value = "分类UID",required = false) @RequestParam(name = "blogSortUid", required = false) String blogSortUid,
			@ApiParam(name = "levelKeyword", value = "博客等级",required = false) @RequestParam(name = "levelKeyword", required = false) String levelKeyword,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();
		if(!StringUtils.isEmpty(keyword.trim())) {
			queryWrapper.like(SQLConf.TITLE, keyword.trim());
		}
		if(!StringUtils.isEmpty(tagUid)) {
			queryWrapper.like(SQLConf.TAG_UID, tagUid);
		}
		if(!StringUtils.isEmpty(blogSortUid)) {
			queryWrapper.like(SQLConf.BLOG_SORT_UID, blogSortUid);
		}
		if(!StringUtils.isEmpty(levelKeyword)) {
			queryWrapper.eq(SQLConf.LEVEL, levelKeyword);
		}
		
		//分页 
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();
		
		if(list.size() == 0) {
			return ResultUtil.result(SysConf.SUCCESS, pageList);
		}
		
		final StringBuffer fileUids = new StringBuffer();
		List<String> sortUids = new ArrayList<String>();
		List<String> tagUids = new ArrayList<String>();

		list.forEach( item -> {
			if(StringUtils.isNotEmpty(item.getFileUid())) {
				fileUids.append(item.getFileUid() + ",");
			}
			if(StringUtils.isNotEmpty(item.getBlogSortUid())) {
				sortUids.add(item.getBlogSortUid());
			}
			if(StringUtils.isNotEmpty(item.getTagUid())) {
				List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), ",");
				for(String itemTagUid : tagUidsTemp) {
					tagUids.add(itemTagUid);	
				}				
			}
		});
		String pictureList = null;
		
		if(fileUids != null) {
			pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), ",");
		}
		List<Map<String, Object>> picList = WebUtils.getPictureMap(pictureList);				
		Collection<BlogSort> sortList = blogSortService.listByIds(sortUids);		
		Collection<Tag> tagList = tagService.listByIds(tagUids);
		
		Map<String, BlogSort> sortMap = new HashMap<String, BlogSort> ();
		Map<String, Tag> tagMap = new HashMap<String, Tag>();
		Map<String, String> pictureMap = new HashMap<String, String>();
		
		sortList.forEach(item -> {
			sortMap.put(item.getUid(), item);
		});
		
		tagList.forEach(item -> {
			tagMap.put(item.getUid(), item);
		});
		
		picList.forEach(item -> {
			pictureMap.put(item.get("uid").toString(), item.get("url").toString());
		});
		
		
		for(Blog item : list) {
			
			//设置分类			
			if(StringUtils.isNotEmpty(item.getBlogSortUid())) {
				item.setBlogSort(sortMap.get(item.getBlogSortUid()));	
			}
						
			//获取标签
			if(StringUtils.isNotEmpty(item.getTagUid())) {
				List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), ",");
				List<Tag> tagListTemp = new ArrayList<Tag>();
				
				tagUidsTemp.forEach(tag -> {
					tagListTemp.add(tagMap.get(tag));
				});
				item.setTagList(tagListTemp);	
			}
			
			//获取图片
			if(StringUtils.isNotEmpty(item.getFileUid())) {
				List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), ",");
				List<String> pictureListTemp = new ArrayList<String>();
				
				pictureUidsTemp.forEach(picture -> {
					pictureListTemp.add(pictureMap.get(picture));
				});
				item.setPhotoList(pictureListTemp);
			}		
		}
		log.info("返回结果");
		pageList.setRecords(list);
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@OperationLogger(value="增加博客")
	@ApiOperation(value="增加博客", notes="增加博客", response = String.class)	
	@PostMapping("/add")
	public String add(HttpServletRequest request,
			@ApiParam(name = "title", value = "博客标题",required = true) @RequestParam(name = "title", required = true) String title,
			@ApiParam(name = "summary", value = "博客简介",required = false) @RequestParam(name = "summary", required = false) String summary,
			@ApiParam(name = "content", value = "博客正文",required = false) @RequestParam(name = "content", required = false) String content,
			@ApiParam(name = "tagUid", value = "标签uid",required = false) @RequestParam(name = "tagUid", required = false) String tagUid,
			@ApiParam(name = "clickCount", value = "点击数",required = false) @RequestParam(name = "clickCount", required = false, defaultValue = "1") Integer clickCount,
			@ApiParam(name = "level", value = "推荐等级",required = false) @RequestParam(name = "level", required = false, defaultValue = "0") Integer level,
			@ApiParam(name = "collectCount", value = "收藏数",required = false) @RequestParam(name = "collectCount", required = false, defaultValue = "0") Integer collectCount,
			@ApiParam(name = "isOriginal", value = "是否原创",required = false) @RequestParam(name = "isOriginal", required = false, defaultValue = "1") String isOriginal,
			@ApiParam(name = "isPublish", value = "是否发布",required = false) @RequestParam(name = "isPublish", required = false, defaultValue = "1") String isPublish,
			@ApiParam(name = "author", value = "作者",required = false) @RequestParam(name = "author", required = true) String author,
			@ApiParam(name = "articlesPart", value = "文章出处",required = false) @RequestParam(name = "articlesPart", required = false) String articlesPart,
			@ApiParam(name = "blogSortUid", value = "博客分类UID",required = false) @RequestParam(name = "blogSortUid", required = false) String blogSortUid,
			@ApiParam(name = "fileUid", value = "标题图Uid",required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {
		
 		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || StringUtils.isEmpty(blogSortUid)) {
			return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
		}
 		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
 		queryWrapper.eq(SQLConf.LEVEL, level);
 		Integer count = blogService.count(queryWrapper);
 		
 		//添加的时候进行判断
		switch(level) {

			case ELevel.FIRST: { 
				if(count > BLOG_FIRST_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "一级推荐不能超过" + BLOG_FIRST_COUNT + "个");
				}
			} break;
			
			case ELevel.SECOND: {
				if(count > BLOG_SECOND_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "二级推荐不能超过" + BLOG_SECOND_COUNT + "个");
				}
			} break;
			
			case ELevel.THIRD: {
				if(count > BLOG_THIRD_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "三级推荐不能超过" + BLOG_THIRD_COUNT + "个");
				}
			} break;
			
			case ELevel.FOURTH: { 
				if(count > BLOG_FOURTH_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "四级推荐不能超过" + BLOG_FOURTH_COUNT + "个");
				}
			} break;
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
		blog.setLevel(level);		
		blog.setArticlesPart(articlesPart);
		
		//如果是原创，作者为用户的昵称
		if(isOriginal.equals("1")) {
			Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
			if(admin != null) {
				blog.setAuthor(admin.getNickName());
				blog.setAdminUid(admin.getUid());
			}
			blog.setArticlesPart(PROJECT_NAME);
		} else {
			if(StringUtils.isEmpty(author)) {
				return ResultUtil.result(SysConf.ERROR, "作者不能为空");
			}
			blog.setAuthor(author);
		}
		blog.setIsOriginal(isOriginal);
		blog.setIsPublish(isPublish);
		blog.setStatus(EStatus.ENABLE);
		Boolean save = blogService.save(blog);
		
		//保存成功后，需要发送消息到solr 和 redis
		if(save && isPublish.equals("1")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SysConf.COMMAND, SysConf.ADD);
			map.put(SysConf.BLOG_UID, blog.getUid());
			map.put(SysConf.LEVEL, blog.getLevel());
			//发送到RabbitMq
			rabbitTemplate.convertAndSend("exchange.direct", "mogu.blog", map);	
			
			//获取图片
			if(StringUtils.isNotEmpty(blog.getFileUid())) {
				String pictureList = this.pictureFeignClient.getPicture(blog.getFileUid(), ",");	
				List<String> picList = WebUtils.getPicture(pictureList);
				blog.setPhotoList(picList);				
			}
						
			//增加solr索引
			blogSearchService.addIndex(blog);
		}

		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@OperationLogger(value="编辑博客")
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
			@ApiParam(name = "level", value = "推荐等级",required = false) @RequestParam(name = "level", required = false, defaultValue = "0") Integer level,
			@ApiParam(name = "collectCount", value = "收藏数",required = false) @RequestParam(name = "collectCount", required = false, defaultValue = "0") Integer collectCount,
			@ApiParam(name = "isOriginal", value = "是否原创",required = false) @RequestParam(name = "isOriginal", required = false, defaultValue = "1") String isOriginal,
			@ApiParam(name = "isPublish", value = "是否发布",required = false) @RequestParam(name = "isPublish", required = false, defaultValue = "1") String isPublish,
			@ApiParam(name = "author", value = "作者",required = false) @RequestParam(name = "author", required = true) String author,
			@ApiParam(name = "articlesPart", value = "文章出处",required = false) @RequestParam(name = "articlesPart", required = false) String articlesPart,
			@ApiParam(name = "fileUid", value = "标题图UID",required = false) @RequestParam(name = "fileUid", required = false) String fileUid ) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		Blog blog = blogService.getById(uid);
 		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
 		queryWrapper.eq(SQLConf.LEVEL, level);
 		Integer count = blogService.count(queryWrapper);
 		if(blog != null) {
 			//传递过来的和数据库中的不同，代表用户已经修改过等级了，那么需要将count数加1
 			if(blog.getLevel() != level) {
 				count += 1;
 			}
 		}
 		//添加的时候进行判断
		switch(level) {

			case ELevel.FIRST: { 
				if(count > BLOG_FIRST_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "一级推荐不能超过" + BLOG_FIRST_COUNT + "个");
				}
			} break;
			
			case ELevel.SECOND: {
				if(count > BLOG_SECOND_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "二级推荐不能超过" + BLOG_SECOND_COUNT + "个");
				}
			} break;
			
			case ELevel.THIRD: {
				if(count > BLOG_THIRD_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "三级推荐不能超过" + BLOG_THIRD_COUNT + "个");
				}
			} break;
			
			case ELevel.FOURTH: { 
				if(count > BLOG_FOURTH_COUNT) {
					return ResultUtil.result(SysConf.ERROR, "四级推荐不能超过" + BLOG_FOURTH_COUNT + "个");
				}
			} break;
		}
		
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setContent(content);
		blog.setTagUid(tagUid);
		blog.setBlogSortUid(blogSortUid);
		blog.setFileUid(fileUid);
		blog.setClickCount(clickCount);
		blog.setLevel(level);
		blog.setCollectCount(collectCount);		
		blog.setIsOriginal(isOriginal);		
		//如果是原创，作者为用户的昵称
		if(isOriginal.equals("1")) {
			Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());			
			if(admin != null) {
				blog.setAdminUid(admin.getUid());
				blog.setAuthor(admin.getNickName());
				blog.setArticlesPart(PROJECT_NAME);
			}
		} else {
			if(StringUtils.isEmpty(author)) {
				return ResultUtil.result(SysConf.ERROR, "作者名不能为空");
			}
			blog.setAuthor(author);
		}
		
		blog.setIsPublish(isPublish);
		blog.setArticlesPart(articlesPart);
		blog.setStatus(EStatus.ENABLE);
		Boolean save = blog.updateById();
		//保存成功后，需要发送消息到solr 和 redis
		if(save && isPublish.equals("1")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SysConf.COMMAND, SysConf.EDIT);
			map.put(SysConf.BLOG_UID, blog.getUid());
			map.put(SysConf.LEVEL, blog.getLevel());
			//发送到RabbitMq
			rabbitTemplate.convertAndSend("exchange.direct", "mogu.blog", map);
			
			//更新solr索引
			blogSearchService.updateIndex(blog);
			
		} else if(isPublish.equals("0")) {
			
			//这是需要做的是，是删除redis中的该条博客数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SysConf.COMMAND, SysConf.EDIT);
			map.put(SysConf.BLOG_UID, blog.getUid());
			map.put(SysConf.LEVEL, blog.getLevel());
			//发送到RabbitMq
			rabbitTemplate.convertAndSend("exchange.direct", "mogu.blog", map);
			
			//当设置下架状态时，删除博客索引
			blogSearchService.deleteIndex(blog.getUid());
		}
		
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@OperationLogger(value="删除博客")
	@ApiOperation(value="删除博客", notes="删除博客", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		Blog blog = blogService.getById(uid);
		blog.setStatus(EStatus.DISABLED);		
		Boolean save = blog.updateById();
		
		//保存成功后，需要发送消息到solr 和 redis
		if(save) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SysConf.COMMAND, SysConf.DELETE);
			map.put(SysConf.BLOG_UID, blog.getUid());
			map.put(SysConf.LEVEL, blog.getLevel());
			//发送到RabbitMq
			rabbitTemplate.convertAndSend("exchange.direct", "mogu.blog", map);
			
			//删除solr索引
			blogSearchService.deleteIndex(blog.getUid());
		}
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
		
}

