package com.moxi.blog.elasticsearch.service;

import com.moxi.blog.elasticsearch.client.BlogClient;
import com.moxi.blog.elasticsearch.pojo.Blog;
import com.moxi.blog.elasticsearch.reposlitory.BlogRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Field;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    BlogClient blogClient;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public Blog buidBlog(com.moxi.mogublog.xo.entity.Blog eblog) {

        blogClient.getBlogByUid(eblog.getUid());
        //搜索字段
        String all = eblog.getTitle() +" "+ eblog.getSummary();

        //构建blog对象
        Blog blog = new Blog();
        blog.setId(eblog.getUid());
        blog.setUid(eblog.getUid());
        blog.setTitle(eblog.getTitle());
        blog.setSummary(eblog.getSummary());
        blog.setAll(all);

        if(eblog.getBlogSort() != null) {
            blog.setBlogSortName(eblog.getBlogSort().getSortName());
            blog.setBlogSortUid(eblog.getBlogSortUid());
        }

        blog.setIsPublish(eblog.getIsPublish());
        blog.setAuthor(eblog.getAuthor());
        blog.setCreateTime(eblog.getCreateTime().toString());
        blog.setPhotoList(eblog.getPhotoList());
        return blog;
    }

    public Map<String, Object> search(String keywords, Integer currentPage, Integer pageSize) {
        currentPage =Math.max(currentPage-1,0);
        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"uid","title","summary","blogSortName","createTime","author","photoList","blogSortUid"},null));
        //分页
        pageSize = 10;
        queryBuilder.withPageable(PageRequest.of(currentPage, pageSize));

        //过滤
        queryBuilder.withQuery(QueryBuilders.termQuery("isPublish","1"));
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", keywords));

//        HighlightBuilder highlightBuilder = new HighlightBuilder ();
//        highlightBuilder.field("title");
//        String preTag = "<span style = 'color:red'>";
//        String postTag = "</span>";
//        HighlightBuilder.Field allHighLight = new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag);
//        queryBuilder.withHighlightFields(allHighLight);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style = 'color:red'>");
        highlightBuilder.postTags("</span>");

        queryBuilder.withHighlightBuilder(highlightBuilder);

        //查询
        Page<Blog> result = blogRepository.search(queryBuilder.build());


        //解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Blog> blogList = result.getContent();
        Map<String, Object> map = new HashMap<>();
        map.put("totle", total);
        map.put("totlePage", totalPage);
        map.put("currentPage", currentPage);
        map.put("blogList", blogList);
        return map;
    }

}
