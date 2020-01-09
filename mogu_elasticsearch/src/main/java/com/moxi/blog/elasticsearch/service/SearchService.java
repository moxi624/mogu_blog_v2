package com.moxi.blog.elasticsearch.service;

import com.moxi.blog.elasticsearch.client.BlogClient;
import com.moxi.blog.elasticsearch.pojo.Blog;
import com.moxi.blog.elasticsearch.reposlitory.BlogRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    BlogClient blogClient;

    @Autowired
    BlogRepository blogRepository;


    public Blog buidBlog(com.moxi.mogublog.xo.entity.Blog eblog) {

        blogClient.getBlogByUid(eblog.getUid());
        //搜索字段
        String all = eblog.getTitle() + eblog.getSummary();
        //构建blog对象
        Blog blog = new Blog();
        blog.setUid(eblog.getUid());
        blog.setTitle(eblog.getTitle());
        blog.setSummary(eblog.getSummary());
        blog.setAll(all);
        //sortName
        blog.setBlogSortName(eblog.getBlogSortUid());
        blog.setIsPublish(eblog.getIsPublish());
        blog.setAuthor(eblog.getAuthor());
        blog.setCreateTime(eblog.getCreateTime().toString());
        blog.setPhoto(eblog.getPhotoList().toString());
        return blog;
    }

    public Map<String, Object> search(String keywords, Integer currentPage, Integer pageSize) {
        currentPage = currentPage - 1;
        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"title","summary","blogSortName","createTime","author","photo"},null));
        //分页
        queryBuilder.withPageable(PageRequest.of(currentPage, pageSize));
        //过滤
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", keywords));
        queryBuilder.withQuery(QueryBuilders.termQuery("isPublish","1"));

        //查询
        Page<Blog> result = blogRepository.search(queryBuilder.build());

        //解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Blog> blogList = result.getContent();
        Map<String, Object> map = new HashMap<>();
        map.put("totle", total);
        map.put("tottalPage", totalPage);
        map.put("blogList", blogList);
        return map;
    }
}
