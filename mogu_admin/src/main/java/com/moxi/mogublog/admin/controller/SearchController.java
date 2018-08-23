package com.moxi.mogublog.admin.controller;

import org.springframework.stereotype.Controller;

import com.moxi.mougblog.base.controller.BaseController;
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
