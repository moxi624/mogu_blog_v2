package com.moxi.mogublog.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.BlogSO;
import com.moxi.mougblog.base.controller.BaseController;
import com.moxi.mougblog.base.enums.EStatus;
/**
 * 搜索管理Controller
 * @author Administrator
 *
 */
@Controller
public class SearchController extends BaseController{
	
//	@Autowired
//	BlogSO blogSO;
//	@Autowired
//	HttpSolrServer httpSolrServer;
//	
//	/**
//	 * 初始化索引
//	 * @param request
//	 * @param model
//	 * @return
//	 * @throws SolrServerException
//	 * @throws IOException
//	 */
//	@RequestMapping(value = {"/initindex" }, method = RequestMethod.POST)
//	@ResponseBody
//	public String initIndex(HttpServletRequest request,Map<String, Object> model) {
//		model.put(SysConf.base, WebConf.AdminBaseUrl);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put(SysConf.status, EStatus.ENABLE);
//		List<Blog> list = blogSO.getList(map);
//		try {
//			//删除所有的索引库
//			httpSolrServer.deleteByQuery("*:*");
//			//建立索引
//			for(Blog blog : list) {
//		        SolrInputDocument document = new SolrInputDocument();
//		        document.addField(SysConf.id, blog.getUid());
//		        document.addField(SysConf.uid, blog.getUid());
//		        document.addField(SysConf.oid, blog.getOid());
//		        document.addField(SysConf.title, blog.getTitle());
//		        document.addField(SysConf.summary, blog.getSummary());
//		        document.addField(SysConf.content, blog.getContent());
//		        document.addField(SysConf.taguid, blog.getTitle());
//		        document.addField(SysConf.clickcount, blog.getClickcount());
//		        document.addField(SysConf.collectcount, blog.getCollectcount());
//		        document.addField(SysConf.photo, blog.getPhoto());
//		        document.addField(SysConf.createtime, blog.getCreatetime());
//		        document.addField(SysConf.updatetime, blog.getUpdatetime());
//		        httpSolrServer.add(document);
//		        httpSolrServer.commit();
//			}
//			model.put(SysConf.info, SysConf.success);
//		} catch (Exception e) {
//			model.put(SysConf.info, SysConf.failure);
//		}	
//        return toJson(model);
//	}
//	

}
