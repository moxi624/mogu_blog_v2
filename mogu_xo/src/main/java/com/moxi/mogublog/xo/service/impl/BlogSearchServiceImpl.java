package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.SolrIndex;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogSearchService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.enums.EStatus;
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
public class BlogSearchServiceImpl implements BlogSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogSortService blogSortService;


    //搜索(带高亮)
    @Override
    public Map<String, Object> search(String colleciton, String keywords, Integer currentPage, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.putAll(searchList(colleciton, keywords, currentPage, pageSize));
        return map;
    }

    //初始化索引
    @Override

    public void initIndex(String collection, List<Blog> blogList) {

        this.deleteAllIndex(collection); //清除所有索引

        List<SolrIndex> solrIndexs = new ArrayList<>();

        for (Blog blog : blogList) {
            SolrIndex solrIndex = new SolrIndex();
            solrIndex.setFileUid(blog.getFileUid());
            //将图片存放索引中
            if (blog.getPhotoList() != null) {
                String str = "";
                for (String s : blog.getPhotoList()) {
                    str = str + s + ",";
                }
                solrIndex.setPhotoList(str);
            }
            solrIndex.setId(blog.getUid());
            solrIndex.setTitle(blog.getTitle());
            solrIndex.setSummary(blog.getSummary());
            solrIndex.setTag(getTagByBlog(blog));
            solrIndex.setBlogSort(getBlogSortbyBlog(blog));
            solrIndex.setBlogTagUid(blog.getTagUid());
            solrIndex.setBlogSortUid(blog.getBlogSortUid());
            solrIndex.setAuthor(blog.getAuthor());
            solrIndex.setUpdateTime(blog.getUpdateTime());
            solrIndexs.add(solrIndex);

//	        SolrInputDocument document = new SolrInputDocument();
//	        document.addField("blog_uid", blog.getUid());
//	        document.addField("blog_title", blog.getTitle());
//	        document.addField("blog_summary", blog.getSummary());
//	        document.addField("blog_tag", getTagByBlog(blog));//获取博客标签
//	        document.addField("blog_sort", getBlogSortbyBlog(blog));//获取博客分类
//	        document.addField("blog_author", blog.getAuthor());
//	        document.addField("blog_updateTime", blog.getUpdateTime());
//	        solrTemplate.saveBean(document);
//	        solrTemplate.commit();
        }
        solrTemplate.saveBeans(collection, solrIndexs);
        solrTemplate.commit(collection);
    }

    //添加索引
    @Override
    public void addIndex(String collection, Blog blog) {

        SolrIndex solrIndex = new SolrIndex();
        //将图片存放索引中
        if (blog.getPhotoList() != null) {
            String str = "";
            for (String s : blog.getPhotoList()) {
                str = str + s + ",";
            }
            solrIndex.setPhotoList(str);
        }
        solrIndex.setId(blog.getUid());
        solrIndex.setTitle(blog.getTitle());
        solrIndex.setSummary(blog.getSummary());
        solrIndex.setTag(getTagbyTagUid(blog.getTagUid()));
        solrIndex.setBlogSort(getBlogSort(blog.getBlogSortUid()));
        solrIndex.setAuthor(blog.getAuthor());
        solrIndex.setUpdateTime(new Date());
        solrTemplate.saveBean(collection, solrIndex);
        solrTemplate.commit(collection);

    }

    //更新索引
    @Override
    public void updateIndex(String collection, Blog blog) {

        Optional<SolrIndex> solrIndex = solrTemplate.getById(collection, blog.getUid(), SolrIndex.class);

        //为空表示原来修改发布状态位的时候，删除掉了索引，需要重新添加
        if (solrIndex.isPresent()) {

            addIndex(collection, blog);

        } else {
            //将图片存放索引中
            if (blog.getPhotoList() != null) {
                String str = "";
                for (String s : blog.getPhotoList()) {
                    str = str + s + ",";
                }
                solrIndex.get().setPhotoList(str);
            }
            solrIndex.get().setId(blog.getUid());
            solrIndex.get().setTitle(blog.getTitle());
            solrIndex.get().setSummary(blog.getSummary());
            solrIndex.get().setTag(getTagbyTagUid(blog.getTagUid()));
            solrIndex.get().setBlogSort(getBlogSort(blog.getBlogSortUid()));
            solrIndex.get().setAuthor(blog.getAuthor());
            solrIndex.get().setUpdateTime(new Date());
            solrTemplate.saveBean(collection, solrIndex);
            solrTemplate.commit(collection);
        }

    }

    @Override
    public void deleteIndex(String collection, String uid) {
        solrTemplate.deleteByIds(collection, uid);
        solrTemplate.commit(collection);
    }

    @Override
    public void deleteAllIndex(String collection) {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(collection, query);
        solrTemplate.commit(collection);
    }


    private String getBlogSortbyBlog(Blog blog) {
        String blogSortUid = blog.getBlogSortUid();
        String blogSortContent = getBlogSort(blogSortUid);
        return blogSortContent;
    }


    private String getBlogSort(String blogSortUid) {
        BlogSort blogSort = blogSortService.getById(blogSortUid);
        String blogSortName = blogSort.getSortName();
        return blogSortName;
    }

    private String getTagByBlog(Blog blog) {
        String tagUid = blog.getTagUid();
        String tagContents = getTagbyTagUid(tagUid);
        return tagContents;
    }

    private String getTagbyTagUid(String tagUid) {
        String uids[] = tagUid.split(",");
        List<String> tagContentList = new ArrayList<>();
        for (String uid : uids) {
            Tag tag = tagService.getById(uid);
            if (tag != null && tag.getStatus() != EStatus.DISABLED) {
                tagContentList.add(tag.getContent());
            }
        }
        String tagContents = StringUtils.listTranformString(tagContentList, ",");
        return tagContents;
    }


    private Map<String, Object> searchList(String collection, String keywords, Integer currentPage, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();

        HighlightQuery query = new SimpleHighlightQuery();
        HighlightOptions highlightOptions = new HighlightOptions().addField("blog_title");
        highlightOptions.setSimplePrefix("<span style = 'color:red'>");//高亮前缀
        highlightOptions.setSimplePostfix("</span>");//高亮后缀
        query.setHighlightOptions(highlightOptions);

        //添加查询条件
        Criteria criteria = new Criteria("blog_keywords").is(keywords);
        query.addCriteria(criteria);


        query.setOffset((long) (currentPage - 1) * pageSize);//从第几条记录查询
        query.setRows(pageSize);

        HighlightPage<SolrIndex> page = solrTemplate.queryForHighlightPage(collection, query, SolrIndex.class);

        for (HighlightEntry<SolrIndex> h : page.getHighlighted()) {//循环高亮入口集合
            SolrIndex solrIndex = h.getEntity();//获取原实体类
            if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
                solrIndex.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮结果
            }
        }

        // 返回总记录数
        map.put("total", page.getTotalElements());
        // 返回总页数
        map.put("totalPages", page.getTotalPages());
        // 返回当前页大小
        map.put("pageSize", pageSize);
        // 返回当前页
        map.put("currentPage", currentPage);

        map.put("rows", page.getContent());

        return map;
    }

}
