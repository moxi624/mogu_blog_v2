package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.xo.entity.Blog;

import java.util.List;
import java.util.Map;

/**
 * solr索引维护
 *
 * @author limboy
 * @create 2018-09-29 16:16
 */
public interface BlogSearchService {

    /**
     * 搜索
     *
     * @param keywords    关键字
     * @param currentPage 当前页
     * @param pageSize    页大小
     * @return
     */
    public Map<String, Object> search(String collection, String keywords, Integer currentPage, Integer pageSize);

    /**
     * 初始化索引
     *
     * @param
     * @return
     */
    public void initIndex(String collection, List<Blog> blogList);

    /**
     * 添加索引
     *
     * @param
     * @return
     */
    public void addIndex(String collection, Blog blog);

    /**
     * 更新索引
     *
     * @param
     * @return
     */
    public void updateIndex(String collection, Blog blog);

    /**
     * 删除索引
     *
     * @param id
     * @return
     */
    public void deleteIndex(String collection, String id);

    /**
     * 删除全部索引
     *
     * @param
     * @return
     */
    public void deleteAllIndex(String collection);
}
