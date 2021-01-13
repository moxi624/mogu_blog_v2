package com.moxi.mogublog.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

/**
 * ESBlogIndex
 */
@Data
@Document(indexName = "blog", type = "docs", shards = 1, replicas = 0)
public class ESBlogIndex {
    @Id
    private String id;

    private String uid;

    private Integer oid;

    private String type;

    private String title;

    private String summary;

    private String content;

    private String blogSortName;

    private String blogSortUid;

    private String isPublish;

    private Date createTime;

    private String author;

    private String photoUrl;

    private List<String> tagUidList;

    private List<String> tagNameList;


}
