package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.xo.vo.BlogVO;
import com.moxi.mougblog.base.service.SuperService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 博客表 服务类
 *
 * @author 陌溪
 * @date 2018-09-08
 */
public interface BlogService extends SuperService<Blog> {

    /**
     * 给博客列表设置标签
     *
     * @return
     */
    public List<Blog> setTagByBlogList(List<Blog> list);

    /**
     * 给博客列表设置分类和标签
     *
     * @param list
     * @return
     */
    public List<Blog> setTagAndSortByBlogList(List<Blog> list);

    /**
     * 给博客列表设置分类，标签，图片
     *
     * @param list
     * @return
     */
    public List<Blog> setTagAndSortAndPictureByBlogList(List<Blog> list);

    /**
     * 给博客设置标签
     *
     * @param blog
     * @return
     */
    public Blog setTagByBlog(Blog blog);

    /**
     * 给博客设置分类
     *
     * @param blog
     * @return
     */
    public Blog setSortByBlog(Blog blog);

    /**
     * 通过推荐等级获取博客列表
     *
     * @param level
     * @return
     */
    public List<Blog> getBlogListByLevel(Integer level);

    /**
     * 通过推荐等级获取博客Page，是否排序
     *
     * @param level
     * @return
     */
    public IPage<Blog> getBlogPageByLevel(Page<Blog> page, Integer level, Integer useSort);

    /**
     * 通过状态获取博客数量
     */
    public Integer getBlogCount(Integer status);

    /**
     * 通过标签获取博客数目
     */
    public List<Map<String, Object>> getBlogCountByTag();

    /**
     * 通过标签获取博客数目
     */
    public List<Map<String, Object>> getBlogCountByBlogSort();

    /**
     * 获取一年内的文章贡献数
     *
     * @return
     */
    public Map<String, Object> getBlogContributeCount();

    /**
     * 通过uid获取博客内容
     *
     * @param uid
     * @return
     */
    public Blog getBlogByUid(String uid);

    /**
     * 根据BlogUid获取相关的博客
     *
     * @param blogUid
     * @return
     */
    public List<Blog> getSameBlogByBlogUid(String blogUid);

    /**
     * 获取点击量前top的博客列表
     *
     * @param top
     * @return
     */
    public List<Blog> getBlogListByTop(Integer top);

    /**
     * 获取博客列表
     *
     * @param blogVO
     * @return
     */
    public IPage<Blog> getPageList(BlogVO blogVO);

    /**
     * 新增博客
     *
     * @param blogVO
     */
    public String addBlog(BlogVO blogVO);

    /**
     * 编辑博客
     *
     * @param blogVO
     */
    public String editBlog(BlogVO blogVO);

    /**
     * 推荐博客排序调整
     *
     * @param blogVOList
     * @return
     */
    public String editBatch(List<BlogVO> blogVOList);

    /**
     * 批量删除博客
     *
     * @param blogVO
     */
    public String deleteBlog(BlogVO blogVO);

    /**
     * 批量删除博客
     *
     * @param blogVoList
     * @return
     */
    public String deleteBatchBlog(List<BlogVO> blogVoList);

    /**
     * 本地博客上传
     *
     * @param filedatas
     * @return
     */
    public String uploadLocalBlog(List<MultipartFile> filedatas) throws IOException;

    /**
     * 删除和博客分类有关的Redis缓存
     */
    public void deleteRedisByBlogSort();

    /**
     * 删除和博客标签有关的Redis缓存
     */
    public void deleteRedisByBlogTag();

    /**
     * 删除和博客有关的Redis缓存
     */
    public void deleteRedisByBlog();

    //========================mogu-web使用==========================

    /**
     * 通过推荐等级获取博客Page
     *
     * @param level       推荐级别
     * @param currentPage 当前页
     * @param useSort     是否使用排序字段
     * @return
     */
    public IPage<Blog> getBlogPageByLevel(Integer level, Long currentPage, Integer useSort);

    /**
     * 获取首页排行博客
     *
     * @return
     */
    public IPage<Blog> getHotBlog();


    /**
     * 获取最新的博客
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> getNewBlog(Long currentPage, Long pageSize);

    /**
     * mogu-search调用获取博客的接口[包含内容]
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> getBlogBySearch(Long currentPage, Long pageSize);

    /**
     * 按时间戳获取博客
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> getBlogByTime(Long currentPage, Long pageSize);

    /**
     * 通过博客Uid获取点赞数
     *
     * @param uid
     * @return
     */
    public Integer getBlogPraiseCountByUid(String uid);

    /**
     * 通过UID给博客点赞
     *
     * @param uid
     * @return
     */
    public String praiseBlogByUid(String uid);

    /**
     * 根据标签Uid获取相关的博客
     *
     * @param tagUid
     * @return
     */
    public IPage<Blog> getSameBlogByTagUid(String tagUid);

    /**
     * 通过博客分类UID获取博客列表
     *
     * @param blogSortUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> getListByBlogSortUid(String blogSortUid, Long currentPage, Long pageSize);

    /**
     * 通过关键字搜索博客列表
     *
     * @param keywords
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Map<String, Object> getBlogByKeyword(String keywords, Long currentPage, Long pageSize);

    /**
     * 通过标签搜索博客
     *
     * @param tagUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> searchBlogByTag(String tagUid, Long currentPage, Long pageSize);

    /**
     * 通过博客分类搜索博客
     *
     * @param blogSortUid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> searchBlogByBlogSort(String blogSortUid, Long currentPage, Long pageSize);

    /**
     * 通过作者搜索博客
     *
     * @param author
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<Blog> searchBlogByAuthor(String author, Long currentPage, Long pageSize);


    /**
     * 获取博客的归档日期
     *
     * @return
     */
    public String getBlogTimeSortList();

    /**
     * 通过月份获取日期
     *
     * @param monthDate
     * @return
     */
    public String getArticleByMonth(String monthDate);
}
