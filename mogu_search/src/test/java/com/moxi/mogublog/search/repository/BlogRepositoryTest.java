//package com.moxi.blog.elasticsearch.reposlitory;
//
//import com.moxi.blog.elasticsearch.client.BlogClient;
//import com.moxi.blog.elasticsearch.pojo.Blog;
//import com.moxi.blog.elasticsearch.service.SearchService;
//import com.moxi.mogublog.utils.JsonUtils;
//import com.moxi.mogublog.xo.entity.BlogSort;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BlogRepositoryTest {
//
//    @Autowired
//    private BlogRepository blogRepository;
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Autowired
//    private BlogClient blogClient;
//
//    @Autowired
//    private SearchService searchService;
//
//    @Test
//    public void testCreateIndex() {
//        elasticsearchTemplate.deleteIndex(Blog.class);
//        elasticsearchTemplate.createIndex(Blog.class);
//        elasticsearchTemplate.putMapping(Blog.class);
//    }
//
//    @Test
//    public void loadData() throws ParseException {
//
//        long page = 1;
//        long row = 10;
//        int size = 0;
//        do {
//            // 查询blog信息
//            String result = blogClient.getNewBlog(page, row);
//            //构建blog
//
//            Map<String, Object> blogMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
//            if ("success".equals(blogMap.get("code"))) {
//                Map<String, Object> blogData = (Map<String, Object>) blogMap.get("data");
//                List<Map<String, Object>> blogRecords = (List<Map<String, Object>>) blogData.get("records");
//                size = blogData.size();
//                for (int i = 0; i < blogData.size(); i++) {
//
//                    if (org.springframework.util.StringUtils.isEmpty(blogRecords.get(i).get("uid"))) {
//                        continue;
//                    }
//                    List<com.moxi.mogublog.xo.entity.Blog> EBlogList = new ArrayList<>();
//
//                    List<Map<String, Object>> tagList = (List<Map<String, Object>>) blogRecords.get(i).get("tagList");
//                    Map<String, Object> MapBlogSort = (Map<String, Object>) blogRecords.get(i).get("blogSort");
//                    List photoList = (List) blogRecords.get(i).get("photoList");
//                    com.moxi.mogublog.xo.entity.Blog EBlog = new com.moxi.mogublog.xo.entity.Blog();
//                    BlogSort blogSort = new BlogSort();
//                    blogSort.setSortName(MapBlogSort.get("sortName").toString());
//                    blogSort.setContent(MapBlogSort.get("content").toString());
//                    blogSort.setUid(MapBlogSort.get("uid").toString());
//                    EBlog.setUid((String) blogRecords.get(i).get("uid"));
//                    EBlog.setTitle((String) blogRecords.get(i).get("title"));
//                    EBlog.setSummary((String) blogRecords.get(i).get("summary"));
//
//                    EBlog.setBlogSortUid(blogSort.getUid());
//                    EBlog.setBlogSort(blogSort);
//                    EBlog.setIsPublish((String) blogRecords.get(i).get("isPublish"));
//                    EBlog.setAuthor((String) blogRecords.get(i).get("author"));
//                    Date createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(blogRecords.get(i).get
//                    ("createTime").toString());
//                    EBlog.setCreateTime(createTime);
//                    EBlog.setPhotoList(photoList);
//                    EBlogList.add(EBlog);
//
//                    List<Blog> blogList = EBlogList.stream()
//                            .map(searchService::buidBlog).collect(Collectors.toList());
//                    //存入索引库
//                    blogRepository.saveAll(blogList);
//                    //翻页
//                    page++;
//                }
//            }
//        } while (size == 10);
//
//    }
//
//}