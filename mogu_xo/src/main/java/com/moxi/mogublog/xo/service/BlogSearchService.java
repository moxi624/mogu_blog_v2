package com.moxi.mogublog.xo.service;

import java.util.List;
import java.util.Map;

import com.moxi.mogublog.xo.entity.Blog;

/**
 * solr索引维护
 * @author limboy
 * @create 2018-09-29 16:16
 */
public interface BlogSearchService {
    
	/**
	 * 搜索
	 * @param keywords 关键字
	 * @param currentPage 当前页
	 * @param pageSize  页大小
	 * @return
	 */
    public Map<String, Object> search(String keywords, Integer currentPage, Integer pageSize);

    /**
     * 初始化索引
     * @param
     * @return
     */
    public void initIndex(List<Blog> blogList);

    /**
     * 添加索引
     * @param
     * @return
     */
    public void addIndex(Blog blog);

    /**
     * 更新索引
     * @param
     * @return
     */
    public void updateIndex(Blog blog);

    /**
     * 删除索引
     * @param id
     * @return
     */
    public void deleteIndex(String id);

    /**
     * 删除全部索引
     * @param
     * @return
     */
    public void deleteAllIndex();
}
