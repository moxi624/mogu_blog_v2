package com.moxi.blog.elasticsearch.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "blog", type = "docs", shards = 1, replicas = 0)
public class Blog {
    @Id
    private String Uid;

    private String title;

    private String summary;

    private String blogSortName;

    private String isPublish;

    private String createTime;

    private String author;

    private String photo;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; //所以可搜索信息

}
