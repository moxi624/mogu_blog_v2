package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	private static Logger log = LogManager.getLogger(SortRestApi.class);
	
	/**
	 * 获取归档的信息
	 * @author xzx19950624@qq.com
	 * @date 2018年11月6日下午8:57:48
	 */
	
	@ApiOperation(value="归档", notes="归档")
	@GetMapping("/getSortByMonth")
	public String getMe(HttpServletRequest request) {

		// 第一次启动的时候归档
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		//因为首页并不需要显示内容，所以需要排除掉内容字段
		queryWrapper.select(Blog.class, i-> !i.getProperty().equals("content"));
		List<Blog> list = blogService.list(queryWrapper);

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
		Map<String, Object> result = new HashMap<>();

		result.put("month", monthSet);
		result.put("content", map);
		return ResultUtil.result("success", result);
	}

}

