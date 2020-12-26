package com.moxi.mogublog.search.service;

import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.commons.entity.Tag;
import com.moxi.mogublog.search.global.SysConf;
import com.moxi.mogublog.search.mapper.HighlightResultHelper;
import com.moxi.mogublog.search.pojo.ESBlogIndex;
import com.moxi.mogublog.search.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch实现类
 *
 * @author 陌溪
 * @date 2020/9/15 15:19
 */
@Slf4j
@Service
public class ElasticSearchService {


    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    HighlightResultHelper highlightResultHelper;

    @Autowired
    BlogRepository blogRepository;

    public ESBlogIndex buidBlog(Blog eblog) {

        //构建blog对象
        ESBlogIndex blog = new ESBlogIndex();
        blog.setId(eblog.getUid());
        blog.setOid(eblog.getOid());
        blog.setUid(eblog.getUid());
        blog.setTitle(eblog.getTitle());
        blog.setType(eblog.getType());
        blog.setSummary(eblog.getSummary());
        blog.setContent(eblog.getContent());

        if (eblog.getBlogSort() != null) {
            blog.setBlogSortName(eblog.getBlogSort().getSortName());
            blog.setBlogSortUid(eblog.getBlogSortUid());
        }

        if (eblog.getTagList() != null) {
            List<Tag> tagList = eblog.getTagList();
            List<String> tagUidList = new ArrayList<>();
            List<String> tagNameList = new ArrayList<>();
            tagList.forEach(item -> {
                if (item != null) {
                    tagUidList.add(item.getUid());
                    tagNameList.add(item.getContent());
                }
            });
            blog.setTagNameList(tagNameList);
            blog.setTagUidList(tagUidList);
        }

        blog.setIsPublish(eblog.getIsPublish());
        blog.setAuthor(eblog.getAuthor());
        blog.setCreateTime(eblog.getCreateTime());
        if (eblog.getPhotoList() != null && eblog.getPhotoList().size() > 0) {
            blog.setPhotoUrl(eblog.getPhotoList().get(0));
        } else {
            blog.setPhotoUrl("");
        }
        return blog;
    }

    public Map<String, Object> search(String keywords, Integer currentPage, Integer pageSize) {
        currentPage = Math.max(currentPage - 1, 0);

        List<HighlightBuilder.Field> highlightFields = new ArrayList<>();

        HighlightBuilder.Field titleField = new HighlightBuilder.Field(SysConf.TITLE).preTags("<span style='color:red'>").postTags("</span>");
        HighlightBuilder.Field summaryField = new HighlightBuilder.Field(SysConf.SUMMARY).preTags("<span style='color:red'>").postTags("</span>");
        highlightFields.add(titleField);
        highlightFields.add(summaryField);

        HighlightBuilder.Field[] highlightFieldsAry = highlightFields.toArray(new HighlightBuilder
                .Field[highlightFields.size()]);
        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();


        queryBuilder.withPageable(PageRequest.of(currentPage, pageSize));

        //过滤
        QueryStringQueryBuilder queryStrBuilder = new QueryStringQueryBuilder(keywords);
        queryStrBuilder.field("title", 0.75F).field("summary", 0.75F).field("content", 0.1F);


        queryBuilder.withQuery(queryStrBuilder);

        queryBuilder.withHighlightFields(highlightFieldsAry);

        log.error("查询语句：{}", queryBuilder.build().getQuery().toString());

        //查询
        AggregatedPage<ESBlogIndex> result = elasticsearchTemplate.queryForPage(queryBuilder.build(), ESBlogIndex
                .class, highlightResultHelper);

        //解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<ESBlogIndex> blogList = result.getContent();
        Map<String, Object> map = new HashMap<>();
        map.put(SysConf.TOTAL, total);
        map.put(SysConf.TOTAL_PAGE, totalPage);
        map.put(SysConf.PAGE_SIZE, pageSize);
        map.put(SysConf.CURRENT_PAGE, currentPage + 1);
        map.put(SysConf.BLOG_LIST, blogList);
        return map;
    }

}
