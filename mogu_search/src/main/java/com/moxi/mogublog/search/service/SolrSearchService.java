package com.moxi.mogublog.search.service;

import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.commons.entity.Tag;
import com.moxi.mogublog.search.global.SysConf;
import com.moxi.mogublog.search.pojo.SolrIndex;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.global.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * solr索引维护实现
 *
 * @author limboy
 * @create 2018-09-29 16:19
 */
@Service
@Slf4j
public class SolrSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 搜索(带高亮)
     *
     * @param colleciton
     * @param keywords
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Map<String, Object> search(String colleciton, String keywords, Integer currentPage, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.putAll(searchList(colleciton, keywords, currentPage, pageSize));
        return map;
    }

    //初始化索引
    public void initIndex(String collection, List<Blog> blogList) {
        List<SolrIndex> solrIndexs = new ArrayList<>();
        for (Blog blog : blogList) {
            SolrIndex solrIndex = new SolrIndex();
            solrIndex.setFileUid(blog.getFileUid());
            //将图片存放索引中
            if (blog.getPhotoList() != null && blog.getPhotoList().size() > 0) {
                solrIndex.setPhotoUrl(blog.getPhotoList().get(0));
            } else {
                solrIndex.setPhotoUrl("");
            }
            solrIndex.setId(blog.getUid());
            solrIndex.setTitle(blog.getTitle());
            solrIndex.setSummary(blog.getSummary());
            solrIndex.setBlogContent(blog.getContent());

            List<Tag> tagList = blog.getTagList();
            List<String> tagContentList = new ArrayList<>();

            if (tagList != null && tagList.size() > 0) {
                tagList.forEach(item -> {
                    if (item != null) {
                        tagContentList.add(item.getContent());
                    }
                });
            }

            if (tagContentList.size() > 0) {
                solrIndex.setBlogTagName(StringUtils.listTranformString(tagContentList, Constants.SYMBOL_COMMA));
            }

            if (blog.getBlogSort() != null) {
                solrIndex.setBlogSortName(blog.getBlogSort().getSortName());
            }
            solrIndex.setBlogTagUid(blog.getTagUid());
            solrIndex.setBlogSortUid(blog.getBlogSortUid());
            solrIndex.setAuthor(blog.getAuthor());
            solrIndex.setCreateTime(blog.getCreateTime());

            solrIndexs.add(solrIndex);
        }
        solrTemplate.saveBeans(collection, solrIndexs);
        solrTemplate.commit(collection);
    }

    //添加索引
    public void addIndex(String collection, Blog blog) {

        SolrIndex solrIndex = new SolrIndex();
        //将图片存放索引中
        if (blog.getPhotoList() != null && blog.getPhotoList().size() > 0) {
            solrIndex.setPhotoUrl(blog.getPhotoList().get(0));
        } else {
            solrIndex.setPhotoUrl("");
        }
        solrIndex.setId(blog.getUid());
        solrIndex.setTitle(blog.getTitle());
        solrIndex.setSummary(blog.getSummary());
        solrIndex.setBlogContent(blog.getContent());

        List<Tag> tagList = blog.getTagList();
        List<String> tagContentList = new ArrayList<>();
        if (tagList != null) {
            tagList.forEach(item -> {
                tagContentList.add(item.getContent());
            });
        }
        solrIndex.setBlogTagName(StringUtils.listTranformString(tagContentList, Constants.SYMBOL_COMMA));

        if (blog.getBlogSort() != null) {
            solrIndex.setBlogSortName(blog.getBlogSort().getSortName());
        }

        solrIndex.setAuthor(blog.getAuthor());
        solrIndex.setCreateTime(blog.getCreateTime());
        solrTemplate.saveBean(collection, solrIndex);
        solrTemplate.commit(collection);
        log.info("添加Solr索引成功");
    }

    //更新索引
    public void updateIndex(String collection, Blog blog) {

        Optional<SolrIndex> solrIndex = solrTemplate.getById(collection, blog.getUid(), SolrIndex.class);

        //为空表示原来修改发布状态位的时候，删除掉了索引，需要重新添加
        if (solrIndex.isPresent()) {

            addIndex(collection, blog);

        } else {
            //将图片存放索引中
            if (blog.getPhotoList() != null && blog.getPhotoList().size() > 0) {
                solrIndex.get().setPhotoUrl(blog.getPhotoList().get(0));
            } else {
                solrIndex.get().setPhotoUrl("");
            }
            solrIndex.get().setId(blog.getUid());
            solrIndex.get().setTitle(blog.getTitle());
            solrIndex.get().setSummary(blog.getSummary());
            solrIndex.get().setBlogContent(blog.getContent());

            List<Tag> tagList = blog.getTagList();
            List<String> tagContentList = new ArrayList<>();
            if (tagList != null) {
                tagList.forEach(item -> {
                    tagContentList.add(item.getContent());
                });
            }
            solrIndex.get().setBlogTagName(StringUtils.listTranformString(tagContentList, Constants.SYMBOL_COMMA));
            if (blog.getBlogSort() != null) {
                solrIndex.get().setBlogSortName(blog.getBlogSort().getSortName());
            }
            solrIndex.get().setAuthor(blog.getAuthor());
            solrIndex.get().setCreateTime(blog.getCreateTime());
            solrTemplate.saveBean(collection, solrIndex);
            solrTemplate.commit(collection);
        }

    }

    public void deleteIndex(String collection, String uid) {
        solrTemplate.deleteByIds(collection, uid);
        solrTemplate.commit(collection);
    }

    public void deleteBatchIndex(String collection, List<String> ids) {
        solrTemplate.deleteByIds(collection, ids);
        solrTemplate.commit(collection);
    }

    public void deleteAllIndex(String collection) {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(collection, query);
        solrTemplate.commit(collection);
    }

//    private Map<String, Object> searchList(String collection, String keywords, Integer currentPage, Integer
//    pageSize) {
//        try {
//            HttpSolrClient solrClient = new HttpSolrClient.Builder("http://127.0.0.1:8080/solr/collection1").build();
//            SolrQuery query = new SolrQuery();
//            query.set("q", keywords);
//            //过滤器query.setFilterQueries("pprice:[1 TO 100]");也可以用addFiterQuries设置多过滤条件
//            //query.set("fq","pprice:[1 TO 100]");
//
//            //排序  query.setSort("pprice",ORDER.desc);  addSort
//            //query.set("sort","pprice desc,id asc");
//
//            // 设置查询到的文档返回的域对象
////            query.set("fl","id,pname,pprice");
//
//            //分页
//            query.set("start", currentPage);
//            query.set("rows", pageSize);
//
//            //6.高亮
//            query.set("hl", true);
//            //设置高亮域(设置的域必须在查询条件中存在)
//            query.set("h1.fl", "blog_title", "blog_summary");
//            //前缀
//            query.set("hl.simple.pre", "<span style = 'color:red'>");
//            //后缀
//            query.set("hl.simple.post", "</span>");
//
//            QueryResponse response = solrClient.query(query);
//            SolrDocumentList results = response.getResults();
//            //k是id,内部的map的key是域名,其value是高亮的值集合
//            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
//            for (SolrDocument document : results) {
//                List<String> list = null;
//                if (highlighting.get(document.get("id")) != null) {
//                    list = highlighting.get(document.get("id")).get("blog_title");
//                } else {
//                    System.out.println("无法获取高亮map");
//                }
//                if (list != null && list.size() > 0) {
//                    System.out.println("高亮显示的内容:----" + list.get(0));
//                } else {
//                    System.out.println("高亮显示的内容为空!!!");
//                }
//            }
//            Map<String, Object> map = new HashMap<>();
//            // 返回总记录数
//            map.put(SysConf.TOTAL, results.size());
//            // 返回总页数
//            map.put(SysConf.TOTAL_PAGE, results.size());
//            // 返回当前页大小
//            map.put(SysConf.PAGE_SIZE, pageSize);
//            // 返回当前页
//            map.put(SysConf.CURRENT_PAGE, currentPage);
//            map.put(SysConf.BLOG_LIST, results.size());
//            return map;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return null;
//    }

    private Map<String, Object> searchList(String collection, String keywords, Integer currentPage, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        HighlightQuery query = new SimpleHighlightQuery();
        HighlightOptions highlightOptions = new HighlightOptions().addField("blog_title").addField("blog_summary");
        //高亮前缀
        highlightOptions.setSimplePrefix("<span style = 'color:red'>");
        //高亮后缀
        highlightOptions.setSimplePostfix("</span>");

        query.setHighlightOptions(highlightOptions);
        //添加查询条件
        Criteria criteria = new Criteria("blog_keywords").is(keywords);

        query.addCriteria(criteria);
        //从第几条记录查询
        query.setOffset((long) (currentPage - 1) * pageSize);
        query.setRows(pageSize);

        HighlightPage<SolrIndex> page = solrTemplate.queryForHighlightPage(collection, query, SolrIndex.class);

        //循环高亮入口集合
        for (HighlightEntry<SolrIndex> h : page.getHighlighted()) {
            //获取原实体类
            SolrIndex solrIndex = h.getEntity();
            if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
                //设置高亮结果
                solrIndex.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }

        // 返回总记录数
        map.put(SysConf.TOTAL, page.getTotalElements());
        // 返回总页数
        map.put(SysConf.TOTAL_PAGE, page.getTotalPages());
        // 返回当前页大小
        map.put(SysConf.PAGE_SIZE, pageSize);
        // 返回当前页
        map.put(SysConf.CURRENT_PAGE, currentPage);
        map.put(SysConf.BLOG_LIST, page.getContent());
        return map;
    }
}
