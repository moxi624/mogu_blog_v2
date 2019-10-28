package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.tree.Tree;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 归档 RestApi
 * @author xzx19950624@qq.com
 * @date 2019年10月24日15:29:35
 */
@RestController
@RequestMapping("/sort")
@Api(value="归档 RestApi",tags={"SortRestApi"})
public class SortRestApi {

	@Autowired
	BlogService blogService;

	@Autowired
	TagService tagService;

	@Autowired
	BlogSortService blogSortService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private WebVisitService webVisitService;
	
	private static Logger log = LogManager.getLogger(SortRestApi.class);
	
	/**
	 * 获取归档的信息
	 * @author xzx19950624@qq.com
	 * @date 2018年11月6日下午8:57:48
	 */
	
	@ApiOperation(value="归档", notes="归档")
	@GetMapping("/getSortList")
	public String getSortList(HttpServletRequest request) {

		//从Redis中获取内容
		String monthResult = stringRedisTemplate.opsForValue().get("MONTH_SET");

		//判断redis中时候包含归档的内容
		if(StringUtils.isNotEmpty(monthResult)) {
			List list = JsonUtils.jsonArrayToArrayList(monthResult);
			return ResultUtil.result(SysConf.SUCCESS, list);
		}

		// 第一次启动的时候归档
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);

		//因为首页并不需要显示内容，所以需要排除掉内容字段
		queryWrapper.select(Blog.class, i-> !i.getProperty().equals("content"));
		List<Blog> list = blogService.list(queryWrapper);

		//给博客增加标签和分类
		list = setBlog(list);

		Map<String, List<Blog>> map = new HashMap<>();
		Iterator iterable = list.iterator();
		Set<String> monthSet = new TreeSet<>();
		while(iterable.hasNext()) {
			Blog blog = (Blog)iterable.next();
			Date createTime = blog.getCreateTime();

			String month = new SimpleDateFormat("yyyy年MM月").format(createTime).toString();

			monthSet.add(month);

			if(map.get(month) == null) {
				List<Blog> blogList = new ArrayList<>();
				blogList.add(blog);
				map.put(month, blogList);
			} else {
				List<Blog> blogList = map.get(month);
				blogList.add(blog);
				map.put(month, blogList);

			}
		}

		// 缓存该月份下的所有文章  key: 月份   value：月份下的所有文章
		map.forEach((key, value) -> {
			stringRedisTemplate.opsForValue().set("BOLG_SORT_BY_MONTH:" + key, JsonUtils.objectToJson(value).toString());
		});

		//将从数据库查询的数据缓存到redis中
		stringRedisTemplate.opsForValue().set("MONTH_SET", JsonUtils.objectToJson(monthSet).toString());

		return ResultUtil.result("success", monthSet);
	}

	@ApiOperation(value="通过月份获取文章", notes="通过月份获取文章")
	@GetMapping("/getArticleByMonth")
	public String getArticleByMonth(HttpServletRequest request,
						@ApiParam(name = "monthDate", value = "归档的日期",required = false) @RequestParam(name = "monthDate", required = false) String monthDate) {

		if(StringUtils.isEmpty(monthDate)) {
			return ResultUtil.result("error", "传入日期不能为空");
		}

		//增加点击记录
		webVisitService.addWebVisit(null, IpUtils.getIpAddr(request), EBehavior.VISIT_SORT.getBehavior(), null, monthDate);

		//从Redis中获取内容
		String contentResult = stringRedisTemplate.opsForValue().get("BOLG_SORT_BY_MONTH:" + monthDate);

		//判断redis中时候包含该日期下的文章
		if(StringUtils.isNotEmpty(contentResult)) {
			List list = JsonUtils.jsonArrayToArrayList(contentResult);
			return ResultUtil.result(SysConf.SUCCESS, list);
		}

		// 第一次启动的时候归档
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		//因为首页并不需要显示内容，所以需要排除掉内容字段
		queryWrapper.select(Blog.class, i-> !i.getProperty().equals("content"));
		List<Blog> list = blogService.list(queryWrapper);

		//给博客增加标签和分类
		list = setBlog(list);

		Map<String, List<Blog>> map = new HashMap<>();
		Iterator iterable = list.iterator();
		Set<String> monthSet = new TreeSet<>();
		while(iterable.hasNext()) {
			Blog blog = (Blog)iterable.next();
			Date createTime = blog.getCreateTime();

			String month = new SimpleDateFormat("yyyy年MM月").format(createTime).toString();

			monthSet.add(month);

			if(map.get(month) == null) {
				List<Blog> blogList = new ArrayList<>();
				blogList.add(blog);
				map.put(month, blogList);
			} else {
				List<Blog> blogList = map.get(month);
				blogList.add(blog);
				map.put(month, blogList);
			}
		}

		// 缓存该月份下的所有文章  key: 月份   value：月份下的所有文章
		map.forEach((key, value) -> {
			stringRedisTemplate.opsForValue().set("BOLG_SORT_BY_MONTH:" + key, JsonUtils.objectToJson(value).toString());
		});

		//将从数据库查询的数据缓存到redis中
		stringRedisTemplate.opsForValue().set("MONTH_SET", JsonUtils.objectToJson(monthSet).toString());

		return ResultUtil.result("success", map.get(monthDate));
	}

	/**
	 * 设置博客的分类标签和分类
	 * @param list
	 * @return
	 */
	private List<Blog> setBlog(List<Blog> list) {
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

		Collection<BlogSort> sortList = new ArrayList<>();
		Collection<Tag> tagList = new ArrayList<>();
		if (sortUids.size() > 0) {
			sortList = blogSortService.listByIds(sortUids);
		}
		if (tagUids.size() > 0) {
			tagList = tagService.listByIds(tagUids);
		}

		Map<String, BlogSort> sortMap = new HashMap<String, BlogSort> ();
		Map<String, Tag> tagMap = new HashMap<String, Tag>();
		Map<String, String> pictureMap = new HashMap<String, String>();

		sortList.forEach(item -> {
			sortMap.put(item.getUid(), item);
		});

		tagList.forEach(item -> {
			tagMap.put(item.getUid(), item);
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
		}
		return list;
	}

}

