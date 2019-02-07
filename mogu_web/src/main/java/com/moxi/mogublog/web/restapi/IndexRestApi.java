package com.moxi.mogublog.web.restapi;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Link;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.entity.WebConfig;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.ELevel;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;

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
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private WebConfigService webConfigService;
	
	@Autowired
	private WebVisitService webVisitService;
	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="通过推荐等级获取博客列表", notes="通过推荐等级获取博客列表")
	@GetMapping("/getBlogByLevel")
	public String getBlogByLevel (HttpServletRequest request,
			@ApiParam(name = "level", value = "推荐等级",required = false) @RequestParam(name = "level", required = false, defaultValue = "0") Integer level,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage) {
		
		//从Redis中获取内容
		String jsonResult = stringRedisTemplate.opsForValue().get("BOLG_LEVEL:" + level);
		
		//判断redis中是否有文章
		if(StringUtils.isNotEmpty(jsonResult)) {
			List list = JsonUtils.jsonArrayToArrayList(jsonResult);
			IPage pageList = new Page();
			pageList.setRecords(list);
			return ResultUtil.result(SysConf.SUCCESS, pageList);
		}		
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
				tagUids.add(item.getTagUid());
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

	    //将从数据库查询的数据缓存到redis中		
		stringRedisTemplate.opsForValue().set("BOLG_LEVEL:" + level, JsonUtils.objectToJson(list).toString());
		
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="获取首页排行博客", notes="获取首页排行博客")
	@GetMapping("/getHotBlog")
	public String getHotBlog (HttpServletRequest request) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(0);
		page.setSize(BLOG_HOT_COUNT);
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
		queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
		
		//因为首页并不需要显示内容，所以需要排除掉内容字段		
		queryWrapper.excludeColumns(Blog.class, SysConf.CONTENT);
		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();		
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
				tagUids.add(item.getTagUid());
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

	@ApiOperation(value="获取首页最新的博客", notes="获取首页最新的博客")
	@GetMapping("/getNewBlog")
	public String getNewBlog (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		
		//因为首页并不需要显示内容，所以需要排除掉内容字段		
		queryWrapper.excludeColumns(Blog.class, "content");
		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();
		
		if(list.size() <= 0) {
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
				tagUids.add(item.getTagUid());
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
	
	@ApiOperation(value="按时间戳获取博客", notes="按时间戳获取博客")
	@GetMapping("/getBlogByTime")
	public String getBlogByTime (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		Page<Blog> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		
		//因为首页并不需要显示内容，所以需要排除掉内容字段		
		queryWrapper.excludeColumns(Blog.class, "content");
		
		IPage<Blog> pageList = blogService.page(page, queryWrapper);
		List<Blog> list = pageList.getRecords();
		
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
				tagUids.add(item.getTagUid());
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
	
	@ApiOperation(value="获取最热标签", notes="获取最热标签")
	@GetMapping("/getHotTag")
	public String getHotTag (HttpServletRequest request,
			@ApiParam(name = "currentPage", value = "当前页数",required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
			@ApiParam(name = "pageSize", value = "每页显示数目",required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
		
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		Page<Tag> page = new Page<>();
		page.setCurrent(currentPage);
		page.setSize(pageSize);
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.orderByDesc(SQLConf.SORT);
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
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.orderByDesc(SQLConf.SORT);
		IPage<Link> pageList = linkService.page(page, queryWrapper);
		log.info("返回结果");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@ApiOperation(value="友情链接点击数", notes="获取友情链接")
	@GetMapping("/addLinkCount")
	public String addLinkCount (HttpServletRequest request,
			@ApiParam(name = "uid", value = "友情链接UID",required = false) @RequestParam(name = "uid", required = false) String uid) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
		Link link = linkService.getById(uid);
		if(link != null) {
			
			//增加记录（可以考虑使用AOP）
	        webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.FRIENDSHIP_LINK, uid, null);
	        
			int count = link.getClickCount() + 1;
			link.setClickCount(count);
			link.updateById();	
		}  else {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}
				
		return ResultUtil.result(SysConf.SUCCESS, "更新点击数成功");
	}
	
	
	@ApiOperation(value="获取网站配置", notes="获取友情链接")
	@GetMapping("/getWebConfig")
	public String getWebConfig (HttpServletRequest request) {
		
		QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();		
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		WebConfig webConfig = webConfigService.getOne(queryWrapper);
		
		if(StringUtils.isNotEmpty(webConfig.getLogo())) {
			String pictureList = this.pictureFeignClient.getPicture(webConfig.getLogo(), ",");
			webConfig.setPhotoList(WebUtils.getPicture(pictureList));
		}
		
		//获取支付宝收款二维码
		if(webConfig != null && StringUtils.isNotEmpty(webConfig.getAliPay())) {
			String pictureList = this.pictureFeignClient.getPicture(webConfig.getAliPay(), ",");
			if(WebUtils.getPicture(pictureList).size() > 0) {
				webConfig.setAliPayPhoto(WebUtils.getPicture(pictureList).get(0));	
			}
			
		}
		//获取微信收款二维码
		if(webConfig != null && StringUtils.isNotEmpty(webConfig.getWeixinPay())) {
			String pictureList = this.pictureFeignClient.getPicture(webConfig.getWeixinPay(), ",");
			if(WebUtils.getPicture(pictureList).size() > 0) {
				webConfig.setWeixinPayPhoto(WebUtils.getPicture(pictureList).get(0));	
			}
			
		}
		
		return ResultUtil.result(SysConf.SUCCESS, webConfig);
	}
	
}

