package com.moxi.mogublog.admin.restapi;


import javax.servlet.http.HttpServletRequest;

import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.vo.BlogSortVO;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客分类表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月24日15:45:18
 */
//@PreAuthorize("hasRole('Administrator')")
@RestController
@RequestMapping("/blogSort")
public class BlogSortRestApi {
	
	@Autowired
	BlogSortService blogSortService;

	@Autowired
	BlogService blogService;
	
	private static Logger log = LogManager.getLogger(AdminRestApi.class);


	@ApiOperation(value="获取博客分类列表", notes="获取博客分类列表", response = String.class)
	@PostMapping("/getList")
	public String getList(@Validated({GetList.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

		// 参数校验
		ThrowableUtils.checkParamArgument(result);

		QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
		if(StringUtils.isNotEmpty(blogSortVO.getKeyword()) && !StringUtils.isEmpty(blogSortVO.getKeyword().trim())) {
			queryWrapper.like(SQLConf.CONTENT, blogSortVO.getKeyword().trim());
		}
		Page<BlogSort> page = new Page<>();
		page.setCurrent(blogSortVO.getCurrentPage());
		page.setSize(blogSortVO.getPageSize());
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);		
		queryWrapper.orderByDesc(SQLConf.SORT);		
		IPage<BlogSort> pageList = blogSortService.page(page, queryWrapper);
		log.info("获取博客分类列表");
		return ResultUtil.result(SysConf.SUCCESS, pageList);
	}
	
	@OperationLogger(value="增加博客分类")
	@ApiOperation(value="增加博客分类", notes="增加博客分类", response = String.class)	
	@PostMapping("/add")
	public String add(@Validated({Insert.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

		// 参数校验
		ThrowableUtils.checkParamArgument(result);

		// 判断添加的分类是否存在
		QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.SORT_NAME, blogSortVO.getSortName());
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		BlogSort tempSort = blogSortService.getOne(queryWrapper);
		if(tempSort != null) {
			return ResultUtil.result(SysConf.ERROR, "该分类已经存在");
		}
		
		BlogSort blogSort = new BlogSort();
		blogSort.setContent(blogSortVO.getContent());
		blogSort.setSortName(blogSortVO.getSortName());
		blogSort.setStatus(EStatus.ENABLE);
		blogSort.insert();
		return ResultUtil.result(SysConf.SUCCESS, "添加成功");
	}
	
	@OperationLogger(value="编辑博客分类")
	@ApiOperation(value="编辑博客分类", notes="编辑博客分类", response = String.class)
	@PostMapping("/edit")
	public String edit(@Validated({Update.class}) @RequestBody BlogSortVO blogSortVO, BindingResult result) {

		BlogSort blogSort = blogSortService.getById(blogSortVO.getUid());

		/**
		 * 判断需要编辑的博客分类是否存在
		 */
		if(!blogSort.getSortName().equals(blogSortVO.getSortName())) {
			QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(SQLConf.SORT_NAME, blogSortVO.getSortName());
			BlogSort tempSort = blogSortService.getOne(queryWrapper);
			if(tempSort != null) {
				return ResultUtil.result(SysConf.ERROR, "该分类已经存在");
			}
		}
		
		blogSort.setContent(blogSortVO.getContent());
		blogSort.setSortName(blogSortVO.getSortName());
		blogSort.setStatus(EStatus.ENABLE);
		blogSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
	}
	
	@OperationLogger(value="删除博客分类")
	@ApiOperation(value="删除博客分类", notes="删除博客分类", response = String.class)
	@PostMapping("/delete")
	public String delete(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		BlogSort blogSort = blogSortService.getById(uid);
		blogSort.setStatus(EStatus.DISABLED);		
		blogSort.updateById();
		return ResultUtil.result(SysConf.SUCCESS, "删除成功");
	}
	
	@ApiOperation(value="置顶分类", notes="置顶分类", response = String.class)
	@PostMapping("/stick")
	public String stick(HttpServletRequest request,
			@ApiParam(name = "uid", value = "唯一UID",required = true) @RequestParam(name = "uid", required = true) String uid			) {
		
		if(StringUtils.isEmpty(uid)) {
			return ResultUtil.result(SysConf.ERROR, "数据错误");
		}		
		BlogSort blogSort = blogSortService.getById(uid);
		
		//查找出最大的那一个
		QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc(SQLConf.SORT);
		Page<BlogSort> page = new Page<>();
		page.setCurrent(0);
		page.setSize(1);
		IPage<BlogSort> pageList = blogSortService.page(page,queryWrapper);
		List<BlogSort> list = pageList.getRecords();
		BlogSort  maxSort = list.get(0);

		if(StringUtils.isEmpty(maxSort.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "数据错误"); 
		}
		if(maxSort.getUid().equals(blogSort.getUid())) {
			return ResultUtil.result(SysConf.ERROR, "该分类已经在顶端");
		}
		
		Integer sortCount = maxSort.getSort() + 1;
		
		blogSort.setSort(sortCount);
			
		blogSort.updateById();
		
		return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
	}

	@OperationLogger(value="通过点击量排序博客分类")
	@ApiOperation(value="通过点击量排序博客分类", notes="通过点击量排序博客分类", response = String.class)
	@PostMapping("/blogSortByClickCount")
	public String blogSortByClickCount(HttpServletRequest request) {

		QueryWrapper<BlogSort> queryWrapper = new QueryWrapper();
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		// 按点击从高到低排序
		queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
		List<BlogSort> blogSortList = blogSortService.list(queryWrapper);
		// 设置初始化最大的sort值
		Integer maxSort = blogSortList.size();
		for (BlogSort item : blogSortList) {
			item.setSort(item.getClickCount());
			item.updateById();
		}
		return ResultUtil.result(SysConf.SUCCESS, "排序成功");
	}

	/**
	 * 通过引用量排序标签
	 * 引用量就是所有的文章中，有多少使用了该标签，如果使用的越多，该标签的引用量越大，那么排名越靠前
	 * @param request
	 * @return
	 */
	@OperationLogger(value="通过引用量排序博客分类")
	@ApiOperation(value="通过引用量排序博客分类", notes="通过引用量排序博客分类", response = String.class)
	@PostMapping("/blogSortByCite")
	public String blogSortByCite(HttpServletRequest request) {

		// 定义Map   key：tagUid,  value: 引用量
		Map<String, Integer> map = new HashMap<>();

		QueryWrapper<BlogSort> blogSortQueryWrapper = new QueryWrapper<>();
		blogSortQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		List<BlogSort> blogSortList = blogSortService.list(blogSortQueryWrapper);
		// 初始化所有标签的引用量
		blogSortList.forEach(item -> {
			map.put(item.getUid(), 0);
		});

		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
		// 过滤content字段
		queryWrapper.select(Blog.class, i-> !i.getProperty().equals("content"));
		List<Blog> blogList = blogService.list(queryWrapper);

		blogList.forEach(item -> {
			String blogSortUid = item.getBlogSortUid();
			if(map.get(blogSortUid) != null) {
				Integer count = map.get(blogSortUid) + 1;
				map.put(blogSortUid, count);
			} else {
				map.put(blogSortUid, 0);
			}
		});

		blogSortList.forEach(item -> {
			item.setSort(map.get(item.getUid()));
			item.updateById();
		});

		return ResultUtil.result(SysConf.SUCCESS, "排序成功");
	}
}

