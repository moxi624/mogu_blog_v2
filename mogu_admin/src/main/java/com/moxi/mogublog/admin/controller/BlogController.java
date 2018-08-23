package com.moxi.mogublog.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mogublog.admin.helper.BlogHP;
import com.moxi.mogublog.utils.FileUtils;
import com.moxi.mogublog.utils.StrUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogSO;
import com.moxi.mogublog.xo.service.TagSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;

/**
 * 博客管理Controller
 * @author xuzhixiang
 * @date 2017年9月29日19:22:53
 *
 */
@RestController
public class BlogController extends BaseController{
	
	@Autowired
	BlogSO blogSO;
	@Autowired
	TagSO tagSO;

	/**
	 * 博客管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/blog/list" }, method = RequestMethod.GET)
	@ResponseBody
	public String blogList(HttpServletRequest request,Map<String, Object> model){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Blog> list = blogSO.getList(map);
		return toJson(list);
//		model = BlogHP.getList(model);
//		return toJson(model);
	}
	
	/**
	 * 添加博客
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/add" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogAdd(HttpServletRequest request, String title, String summary, String content, String tag, @RequestParam(value="photo",required=false) MultipartFile photo) throws IllegalStateException, IOException, SolrServerException{
		Blog entity = new Blog();
		if(StrUtils.isNotEmpty(title) && StrUtils.isNotEmpty(summary)) {
			entity.setTitle(title);
			entity.setSummary(summary);
		}
		entity.setContent(content);
		if(StrUtils.isNotEmpty(tag)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SysConf.content, tag);
			Tag t = tagSO.getOne(map);
			if(t != null) {  //判断是否存在该标签
				entity.setTaguid(t.getUid());
			} else {  //不存在该标签时,创建该标签
				Tag t2 = new Tag();
				t2.setContent(tag);
				t2.setClickcount(0); 
				if(!tagSO.insert(t2)) {
					return "failer";
				}
				entity.setTaguid(t2.getUid());
			}
		}
        String path = FileUtils.uploadFile(WebConf.MoguDateUrl, WebConf.BlogImgDataUrl, photo);
		entity.setPhoto(path);
        if (blogSO.insert(entity)) {
        	BlogHP.emptyRedisString(SysConf.newblogs);
        	BlogHP.emptyRedisString(SysConf.hotblogs);
        	BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
        	BlogHP.insertIndex(blogSO.get(entity.getUid())); //建立索引
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 删除博客
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogDelete(HttpServletRequest request,Map<String, Object> model, String uid) throws SolrServerException, IOException{
		if(StrUtils.isNotEmpty(uid)) {
			Blog blog = blogSO.get(uid);
			if(blog != null) {
				blog.setStatus(EStatus.DISABLED);
				if (blogSO.update(blog)) {
		        	BlogHP.emptyRedisString(SysConf.newblogs);
		        	BlogHP.emptyRedisString(SysConf.hotblogs);
		        	BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
		        	BlogHP.emptyRedisHash(SysConf.blogcontents, blog.getOid()+"");
					BlogHP.deleteIndex(blog.getUid()); //删除SOLR索引
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 编辑博客
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/edit" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogEdit(HttpServletRequest request, Map<String, Object> model , String uid, String title, String summary, String content, String tag, @RequestParam(value="photo",required=false) MultipartFile photo ) throws IllegalStateException, IOException, SolrServerException{	
		Blog blog = blogSO.get(uid);
		if(blog != null) {
			blog.setTitle(title);
			if(StrUtils.isNotEmpty(tag)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(SysConf.content, tag);
				Tag t = tagSO.getOne(map);
				if(t != null) {  //判断是否存在该标签
					blog.setTaguid(t.getUid());
				} else {  //不存在该标签时,创建该标签
					Tag t2 = new Tag();
					t2.setContent(tag);
					t2.setClickcount(0);
					if(!tagSO.insert(t2)) {
						return "failer";
					}
					blog.setTaguid(t2.getUid());
				}
			}
			blog.setSummary(summary);
			blog.setContent(content);
			if(photo.getSize() > 0) {
				String path = FileUtils.uploadFile(WebConf.MoguDateUrl, WebConf.BlogImgDataUrl, photo);
				blog.setPhoto(path);
			}
			if(blogSO.update(blog)){
				BlogHP.insertIndex(blog);
	        	BlogHP.emptyRedisString(SysConf.newblogs);
	        	BlogHP.emptyRedisString(SysConf.hotblogs);
	        	BlogHP.emptyRedisHash(SysConf.blogcontents, blog.getOid()+"");
				return "success";
			}
		}
		return "failer";
	}
	
	/**
	 * 冻结博客
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/freeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogFreeze(HttpServletRequest request,Map<String, Object> model, String uid) throws SolrServerException, IOException{
		if(StrUtils.isNotEmpty(uid)) {
			Blog blog = blogSO.get(uid);
			if(blog != null) {
				blog.setStatus(EStatus.FREEZE);
				if (blogSO.update(blog)) {
					BlogHP.deleteIndex(blog.getUid());
		        	BlogHP.emptyRedisString(SysConf.newblogs);
		        	BlogHP.emptyRedisString(SysConf.hotblogs); //清空redis中的内容
		        	BlogHP.emptyRedisHash(SysConf.blogcontents, blog.getOid()+"");
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 解冻博客
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/unfreeze" }, method = RequestMethod.POST)
	@ResponseBody
	public String catalogueUnfreeze(HttpServletRequest request,Map<String, Object> model, String uid) throws SolrServerException, IOException{
		if(StrUtils.isNotEmpty(uid)) {
			Blog blog = blogSO.get(uid);
			if(blog != null) {
				if(blog.getStatus() == EStatus.FREEZE) {
					blog.setStatus(EStatus.ENABLE);
					if (blogSO.update(blog)) {
						BlogHP.insertIndex(blog);
						BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
			        	BlogHP.emptyRedisString(SysConf.newblogs);
			        	BlogHP.emptyRedisString(SysConf.hotblogs); //清空redis中的内容
			        	BlogHP.emptyRedisHash(SysConf.blogcontents, blog.getOid()+"");
						return "success";
					}
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 置顶博客
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/stick" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogStick(HttpServletRequest request,Map<String, Object> model, String uid) throws SolrServerException, IOException{
		if(StrUtils.isNotEmpty(uid)) {
			Blog blog = blogSO.get(uid);
			if(blog != null) {
				blog.setStatus(EStatus.STICK);
				if (blogSO.update(blog)) {
		        	BlogHP.emptyRedisString(SysConf.newblogs);
		        	BlogHP.emptyRedisString(SysConf.hotblogs);
		        	BlogHP.emptyRedisString(SysConf.hottags); //清空redis中的内容
					return "success";
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 取消置顶博客
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping(value = {"/blog/unstick" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogUnstick(HttpServletRequest request,Map<String, Object> model, String uid) throws SolrServerException, IOException{
		if(StrUtils.isNotEmpty(uid)) {
			Blog blog = blogSO.get(uid);
			if(blog != null) {
				if(blog.getStatus() == EStatus.STICK) {
					blog.setStatus(EStatus.ENABLE);
					if (blogSO.update(blog)) {
			        	BlogHP.emptyRedisString(SysConf.newblogs);
			        	BlogHP.emptyRedisString(SysConf.hotblogs);
						return "success";
					}
				}
			} 
		}
		return "failer";
	}
	
	/**
	 * 博客搜索
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/blog/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String blogSearch(HttpServletRequest request,Map<String, Object> model, String keyword){
		model.put(SysConf.base, WebConf.AdminBaseUrl);
		model = BlogHP.getSearchList(model, keyword);
		return toJson(model);
	}
}
