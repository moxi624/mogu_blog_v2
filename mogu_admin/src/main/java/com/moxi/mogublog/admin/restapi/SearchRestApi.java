package com.moxi.mogublog.admin.restapi;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 首页 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/search")
@Api(value="搜索RestApi",tags={"SearchRestApi"})
public class SearchRestApi {
	
	@Autowired
	private SolrTemplate solrtemplate;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	BlogSortService blogSortService;
	
	
	private static Logger log = LogManager.getLogger(SearchRestApi.class);
	
	@ApiOperation(value="导入solr索引", notes="导入solr索引")
	@GetMapping("/initindex")
	public String initIndex(HttpServletRequest request) throws SolrServerException, IOException{
		
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();
		
		queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
		
		List<Blog> blogList = blogService.list(queryWrapper);
		//建立索引
		for(Blog blog : blogList) {
	        SolrInputDocument document = new SolrInputDocument();
	        document.addField(SysConf.UID, blog.getUid());
	        document.addField(SysConf.BLOGTITLE, blog.getTitle());
	        document.addField(SysConf.SUMMARY, blog.getSummary());
	        document.addField(SysConf.TAG, getTagByBlog(blog));//获取博客标签
	        document.addField(SysConf.SORT, getBlogSortbyBlog(blog));//获取博客分类
	        document.addField(SysConf.AUTHOR, blog.getAuthor());
	        document.addField(SysConf.UPDATETIME, blog.getUpdateTime());
	        solrtemplate.saveBean(document);
	        solrtemplate.commit();
		}
		log.info("建立索引成功");
		return ResultUtil.result(SysConf.SUCCESS,"建立索引成功");
        }
	
	private String getBlogSortbyBlog(Blog blog) {
		BlogSort blogSort = blogSortService.getById(blog.getBlogSortUid());
		String blogSortContents = blogSort.getContent();
		return blogSortContents;
	}

	private String getTagByBlog(Blog blog) {
		 String tagUid = blog.getTagUid();
		 String uids[] = tagUid.split(",");
		 List<String> tagContentList = new ArrayList<>();
		 for(String uid : uids) {
				Tag  tag = tagService.getById(uid);
				if(tag != null && tag.getStatus() != EStatus.DISABLED) {
					tagContentList.add(tag.getContent());						
				}
		 	}
		 String tagContents = StringUtils.listTranformString(tagContentList, ",");
		 return tagContents;
	}
	
}

