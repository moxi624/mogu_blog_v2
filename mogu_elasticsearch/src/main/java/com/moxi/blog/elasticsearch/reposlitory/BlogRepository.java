package com.moxi.blog.elasticsearch.reposlitory;

import com.moxi.blog.elasticsearch.pojo.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogRepository extends ElasticsearchRepository<Blog, Long> {
}
