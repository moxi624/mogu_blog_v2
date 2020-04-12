package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.internal.LinkedTreeMap;
import com.moxi.mogublog.commons.entity.*;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.BlogMapper;
import com.moxi.mogublog.xo.mapper.BlogSortMapper;
import com.moxi.mogublog.xo.mapper.TagMapper;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.BlogVO;
import com.moxi.mougblog.base.enums.*;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
@Slf4j
public class BlogServiceImpl extends SuperServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    WebUtil webUtil;
    @Autowired
    CommentService commentService;
    @Autowired
    WebVisitService webVisitService;
    @Autowired
    TagService tagService;
    @Autowired
    PictureService pictureService;
    @Autowired
    BlogSortService blogSortService;
    @Autowired
    LinkService linkService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Autowired
    RedisUtil redisUtil;
    @Resource
    TagMapper tagMapper;
    @Resource
    BlogSortMapper blogSortMapper;
    @Resource
    BlogMapper blogMapper;
    @Autowired
    AdminService adminService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    SystemConfigService systemConfigService;

    @Value(value = "${PROJECT_NAME}")
    private String PROJECT_NAME;

    @Value(value = "${BLOG.FIRST_COUNT}")
    private Integer BLOG_FIRST_COUNT;

    @Value(value = "${BLOG.SECOND_COUNT}")
    private Integer BLOG_SECOND_COUNT;

    @Value(value = "${BLOG.THIRD_COUNT}")
    private Integer BLOG_THIRD_COUNT;

    @Value(value = "${BLOG.FOURTH_COUNT}")
    private Integer BLOG_FOURTH_COUNT;

    @Value(value = "${BLOG.HOT_COUNT}")
    private Integer BLOG_HOT_COUNT;

    @Value(value = "${BLOG.NEW_COUNT}")
    private Integer BLOG_NEW_COUNT;

    @Override
    public List<Blog> setTagByBlogList(List<Blog> list) {
        for (Blog item : list) {
            if (item != null) {
                setTagByBlog(item);
            }
        }
        return list;
    }

    @Override
    public List<Blog> setTagAndSortByBlogList(List<Blog> list) {
        List<String> sortUids = new ArrayList<>();
        List<String> tagUids = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                sortUids.add(item.getBlogSortUid());
            }
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), BaseSysConf.FILE_SEGMENTATION);
                for (String itemTagUid : tagUidsTemp) {
                    tagUids.add(itemTagUid);
                }
            }
        });

        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();

        if (sortUids.size() > 0) {
            sortList = blogSortMapper.selectBatchIds(sortUids);
        }
        if (tagList.size() > 0) {
            tagList = tagMapper.selectBatchIds(tagUids);
        }

        Map<String, BlogSort> sortMap = new HashMap<>();
        Map<String, Tag> tagMap = new HashMap<>();

        sortList.forEach(item -> {
            sortMap.put(item.getUid(), item);
        });

        tagList.forEach(item -> {
            tagMap.put(item.getUid(), item);
        });

        for (Blog item : list) {

            //设置分类
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                item.setBlogSort(sortMap.get(item.getBlogSortUid()));
            }

            //获取标签
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), BaseSysConf.FILE_SEGMENTATION);
                List<Tag> tagListTemp = new ArrayList<Tag>();

                tagUidsTemp.forEach(tag -> {
                    tagListTemp.add(tagMap.get(tag));
                });
                item.setTagList(tagListTemp);
            }
        }

        return list;
    }

    @Override
    public List<Blog> setTagAndSortAndPictureByBlogList(List<Blog> list) {
        final StringBuffer fileUids = new StringBuffer();
        List<String> sortUids = new ArrayList<>();
        List<String> tagUids = new ArrayList<>();

        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + ",");
            }
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                sortUids.add(item.getBlogSortUid());
            }
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                tagUids.add(item.getTagUid());
            }
        });
        String pictureList = null;

        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), ",");
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);
        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();
        if (sortUids.size() > 0) {
            sortList = blogSortService.listByIds(sortUids);
        }
        if (tagUids.size() > 0) {
            tagList = tagService.listByIds(tagUids);
        }


        Map<String, BlogSort> sortMap = new HashMap<>();
        Map<String, Tag> tagMap = new HashMap<>();
        Map<String, String> pictureMap = new HashMap<>();

        sortList.forEach(item -> {
            sortMap.put(item.getUid(), item);
        });

        tagList.forEach(item -> {
            tagMap.put(item.getUid(), item);
        });

        picList.forEach(item -> {
            pictureMap.put(item.get("uid").toString(), item.get("url").toString());
        });


        for (Blog item : list) {

            //设置分类
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {

                item.setBlogSort(sortMap.get(item.getBlogSortUid()));
                if (sortMap.get(item.getBlogSortUid()) != null) {
                    item.setBlogSortName(sortMap.get(item.getBlogSortUid()).getSortName());
                }
            }

            //获取标签
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), ",");
                List<Tag> tagListTemp = new ArrayList<Tag>();

                tagUidsTemp.forEach(tag -> {
                    tagListTemp.add(tagMap.get(tag));
                });
                item.setTagList(tagListTemp);
            }

            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), ",");
                List<String> pictureListTemp = new ArrayList<String>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });

                item.setPhotoList(pictureListTemp);

                // 只设置一张标题图
                if (pictureListTemp.size() > 0) {
                    item.setPhotoUrl(pictureListTemp.get(0));
                } else {
                    item.setPhotoUrl("");
                }

            }
        }
        return list;
    }

    @Override
    public Blog setTagByBlog(Blog blog) {
        String tagUid = blog.getTagUid();
        if (!StringUtils.isEmpty(tagUid)) {
            String uids[] = tagUid.split(SysConf.FILE_SEGMENTATION);
            List<Tag> tagList = new ArrayList<Tag>();
            for (String uid : uids) {
                Tag tag = tagMapper.selectById(uid);
                if (tag != null && tag.getStatus() != EStatus.DISABLED) {
                    tagList.add(tag);
                }
            }
            blog.setTagList(tagList);
        }
        return blog;
    }

    @Override
    public Blog setSortByBlog(Blog blog) {

        if (blog != null && !StringUtils.isEmpty(blog.getBlogSortUid())) {
            BlogSort blogSort = blogSortMapper.selectById(blog.getBlogSortUid());
            blog.setBlogSort(blogSort);
        }
        return blog;
    }

    @Override
    public List<Blog> getBlogListByLevel(Integer level) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.LEVEL, level);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);

        List<Blog> list = blogMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public IPage<Blog> getBlogPageByLevel(Page<Blog> page, Integer level, Integer useSort) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.LEVEL, level);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);

        if (useSort == 0) {
            queryWrapper.orderByDesc(BaseSQLConf.CREATE_TIME);
        } else {
            queryWrapper.orderByDesc(BaseSQLConf.SORT);
        }

        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SysConf.CONTENT));

        return blogMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Integer getBlogCount(Integer status) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);

        return blogMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getBlogCountByTag() {

        List<Map<String, Object>> blogCoutByTagMap = blogMapper.getBlogCountByTag();

        Map<String, Integer> tagMap = new HashMap<>();

        for (Map<String, Object> item : blogCoutByTagMap) {
            String tagUid = String.valueOf(item.get("tag_uid"));
            // java.lang.Number是Integer,Long的父类
            Number num = (Number) item.get("count");
            Integer count = num.intValue();

            //如果只有一个UID的情况
            if (tagUid.length() == 32) {

                //如果没有这个内容的话，就设置
                if (tagMap.get(tagUid) == null) {
                    tagMap.put(tagUid, count);
                } else {
                    Integer tempCount = tagMap.get(tagUid) + count;
                    tagMap.put(tagUid, tempCount);
                }

            } else {
                //如果长度大于32，说明含有多个UID
                if (StringUtils.isNotEmpty(tagUid)) {
                    List<String> strList = StringUtils.changeStringToString(tagUid, ",");
                    for (String strItem : strList) {
                        if (tagMap.get(strItem) == null) {
                            tagMap.put(strItem, count);
                        } else {
                            Integer tempCount = tagMap.get(strItem) + count;
                            tagMap.put(strItem, tempCount);
                        }
                    }
                }
            }
        }

        //把查询到的Tag放到Map中
        Set<String> tagUids = tagMap.keySet();
        Collection<Tag> tagCollection = new ArrayList<>();

        if (tagUids.size() > 0) {
            tagCollection = tagMapper.selectBatchIds(tagUids);
        }

        Map<String, String> tagEntityMap = new HashMap<>();
        for (Tag tag : tagCollection) {
            if (StringUtils.isNotEmpty(tag.getContent())) {
                tagEntityMap.put(tag.getUid(), tag.getContent());
            }
        }

        List<Map<String, Object>> resultMap = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : tagMap.entrySet()) {

            String tagUid = entry.getKey();

            if (tagEntityMap.get(tagUid) != null) {
                String tagName = tagEntityMap.get(tagUid);
                Integer count = entry.getValue();

                Map<String, Object> itemResultMap = new HashMap<>();
                itemResultMap.put("tagUid", tagUid);
                itemResultMap.put("name", tagName);
                itemResultMap.put("value", count);
                resultMap.add(itemResultMap);
            }
        }

        return resultMap;

    }

    @Override
    public List<Map<String, Object>> getBlogCountByBlogSort() {

        List<Map<String, Object>> blogCoutByBlogSortMap = blogMapper.getBlogCountByBlogSort();

        Map<String, Integer> blogSortMap = new HashMap<>();

        for (Map<String, Object> item : blogCoutByBlogSortMap) {

            String blogSortUid = String.valueOf(item.get("blog_sort_uid"));
            // java.lang.Number是Integer,Long的父类
            Number num = (Number) item.get("count");
            Integer count = 0;
            if (num != null) {
                count = num.intValue();
            }

            blogSortMap.put(blogSortUid, count);
        }

        //把查询到的BlogSort放到Map中
        Set<String> blogSortUids = blogSortMap.keySet();
        Collection<BlogSort> blogSortCollection = new ArrayList<>();

        if (blogSortUids.size() > 0) {
            blogSortCollection = blogSortMapper.selectBatchIds(blogSortUids);
        }

        Map<String, String> blogSortEntityMap = new HashMap<>();
        for (BlogSort blogSort : blogSortCollection) {
            if (StringUtils.isNotEmpty(blogSort.getSortName())) {
                blogSortEntityMap.put(blogSort.getUid(), blogSort.getSortName());
            }
        }

        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, Integer> entry : blogSortMap.entrySet()) {

            String blogSortUid = entry.getKey();

            if (blogSortEntityMap.get(blogSortUid) != null) {
                String blogSortName = blogSortEntityMap.get(blogSortUid);
                Integer count = entry.getValue();

                Map<String, Object> itemResultMap = new HashMap<>();
                itemResultMap.put("blogSortUid", blogSortUid);
                itemResultMap.put("name", blogSortName);
                itemResultMap.put("value", count);
                resultMap.add(itemResultMap);
            }
        }

        return resultMap;

    }

    @Override
    public Map<String, Object> getBlogContributeCount() {

        // 获取今天结束时间
        String endTime = DateUtils.getNowTime();

        // 获取365天前的日期
        Date temp = DateUtils.getDate(endTime, -365);

        String startTime = DateUtils.dateTimeToStr(temp);

        List<Map<String, Object>> blogContributeMap = blogMapper.getBlogContributeCount(startTime, endTime);

        List<String> dateList = DateUtils.getDayBetweenDates(startTime, endTime);

        Map<String, Object> dateMap = new HashMap<>();

        for (Map<String, Object> itemMap : blogContributeMap) {

            dateMap.put(itemMap.get("DATE").toString(), itemMap.get("COUNT"));
        }

        List<List<Object>> resultList = new ArrayList<>();
        for (String item : dateList) {
            Integer count = 0;
            if (dateMap.get(item) != null) {
                count = Integer.valueOf(dateMap.get(item).toString());
            }
            List<Object> objectList = new ArrayList<>();
            objectList.add(item);
            objectList.add(count);
            resultList.add(objectList);
        }

        Map<String, Object> resultMap = new HashMap<>();
        List<String> contributeDateList = new ArrayList<>();
        contributeDateList.add(startTime);
        contributeDateList.add(endTime);
        resultMap.put("contributeDate", contributeDateList);
        resultMap.put("blogContributeCount", resultList);

        return resultMap;
    }

    @Override
    public Blog getBlogByUid(String uid) {
        Blog blog = blogMapper.selectById(uid);
        if (blog != null && blog.getStatus() != EStatus.DISABLED) {
            blog = setTagByBlog(blog);
            blog = setSortByBlog(blog);
            return blog;
        }


        return null;
    }

    @Override
    public List<Blog> getSameBlogByBlogUid(String blogUid) {
        Blog blog = blogService.getById(blogUid);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Page<Blog> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);
        // 通过分类来获取相关博客
        String blogSortUid = blog.getBlogSortUid();
        queryWrapper.eq(SQLConf.BLOG_SORT_UID, blogSortUid);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();

        list = blogService.setTagAndSortByBlogList(list);

        //过滤掉当前的博客
        List<Blog> newList = new ArrayList<>();
        for (Blog item : list) {
            if (item.getUid().equals(blogUid)) {
                continue;
            }
            newList.add(item);
        }
        return newList;
    }

    @Override
    public IPage<Blog> getPageList(BlogVO blogVO) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(blogVO.getKeyword()) && !StringUtils.isEmpty(blogVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.TITLE, blogVO.getKeyword().trim());
        }
        if (!StringUtils.isEmpty(blogVO.getTagUid())) {
            queryWrapper.like(SQLConf.TAG_UID, blogVO.getTagUid());
        }
        if (!StringUtils.isEmpty(blogVO.getBlogSortUid())) {
            queryWrapper.like(SQLConf.BLOG_SORT_UID, blogVO.getBlogSortUid());
        }
        if (!StringUtils.isEmpty(blogVO.getLevelKeyword())) {
            queryWrapper.eq(SQLConf.LEVEL, blogVO.getLevelKeyword());
        }
        if (!StringUtils.isEmpty(blogVO.getIsPublish())) {
            queryWrapper.eq(SQLConf.IS_PUBLISH, blogVO.getIsPublish());
        }
        if (!StringUtils.isEmpty(blogVO.getIsOriginal())) {
            queryWrapper.eq(SQLConf.IS_ORIGINAL, blogVO.getIsOriginal());
        }

        //分页
        Page<Blog> page = new Page<>();
        page.setCurrent(blogVO.getCurrentPage());
        page.setSize(blogVO.getPageSize());

        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);

        // 是否启动排序字段
        if (blogVO.getUseSort() == 0) {
            // 未使用，默认按时间倒序
            queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        } else {
            // 使用，默认按sort值大小倒序
            queryWrapper.orderByDesc(SQLConf.SORT);
        }

        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();

        if (list.size() == 0) {
            return pageList;
        }

        final StringBuffer fileUids = new StringBuffer();
        List<String> sortUids = new ArrayList<>();
        List<String> tagUids = new ArrayList<>();

        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                sortUids.add(item.getBlogSortUid());
            }
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), SysConf.FILE_SEGMENTATION);
                for (String itemTagUid : tagUidsTemp) {
                    tagUids.add(itemTagUid);
                }
            }
        });
        String pictureList = null;

        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }

        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);
        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();

        if (sortUids.size() > 0) {
            sortList = blogSortService.listByIds(sortUids);
        }
        if (tagUids.size() > 0) {
            tagList = tagService.listByIds(tagUids);
        }


        Map<String, BlogSort> sortMap = new HashMap<>();
        Map<String, Tag> tagMap = new HashMap<>();
        Map<String, String> pictureMap = new HashMap<>();

        sortList.forEach(item -> {
            sortMap.put(item.getUid(), item);
        });

        tagList.forEach(item -> {
            tagMap.put(item.getUid(), item);
        });

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });


        for (Blog item : list) {

            //设置分类
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                item.setBlogSort(sortMap.get(item.getBlogSortUid()));
            }

            //获取标签
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), SysConf.FILE_SEGMENTATION);
                List<Tag> tagListTemp = new ArrayList<Tag>();

                tagUidsTemp.forEach(tag -> {
                    tagListTemp.add(tagMap.get(tag));
                });
                item.setTagList(tagListTemp);
            }

            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public String addBlog(BlogVO blogVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.LEVEL, blogVO.getLevel());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Integer count = blogService.count(queryWrapper);
        String addVerdictResult = addVerdict(count, blogVO.getLevel());
        // 判断是否能够添加推荐
        if (StringUtils.isNotBlank(addVerdictResult)) {
            return addVerdictResult;
        }
        Blog blog = new Blog();
        //如果是原创，作者为用户的昵称
        if (EOriginal.ORIGINAL.equals(blogVO.getIsOriginal())) {
            Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
            if (admin != null) {
                blog.setAuthor(admin.getNickName());
                blog.setAdminUid(admin.getUid());
            }
            blog.setArticlesPart(PROJECT_NAME);
        } else {
            blog.setAuthor(blogVO.getAuthor());
            blog.setArticlesPart(blogVO.getArticlesPart());
        }
        blog.setTitle(blogVO.getTitle());
        blog.setSummary(blogVO.getSummary());
        blog.setContent(blogVO.getContent());
        blog.setTagUid(blogVO.getTagUid());
        blog.setBlogSortUid(blogVO.getBlogSortUid());
        blog.setFileUid(blogVO.getFileUid());
        blog.setLevel(blogVO.getLevel());
        blog.setIsOriginal(blogVO.getIsOriginal());
        blog.setIsPublish(blogVO.getIsPublish());
        blog.setStatus(EStatus.ENABLE);
        blog.setStartComment(blogVO.getStartComment());
        Boolean isSave = blogService.save(blog);

        //保存成功后，需要发送消息到solr 和 redis
        updateSolrAndRedis(isSave, blog);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editBlog(BlogVO blogVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        Blog blog = blogService.getById(blogVO.getUid());
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.LEVEL, blogVO.getLevel());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Integer count = blogService.count(queryWrapper);
        if (blog != null) {
            //传递过来的和数据库中的不同，代表用户已经修改过等级了，那么需要将count数加1
            if (!blog.getLevel().equals(blogVO.getLevel())) {
                count += 1;
            }
        }
        String addVerdictResult = addVerdict(count, blogVO.getLevel());
        //添加的时候进行判断
        if (StringUtils.isNotBlank(addVerdictResult)) {
            return addVerdictResult;
        }

        //如果是原创，作者为用户的昵称
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        blog.setAdminUid(admin.getUid());
        if (EOriginal.ORIGINAL.equals(blogVO.getIsOriginal())) {
            blog.setAuthor(admin.getNickName());
            blog.setArticlesPart(PROJECT_NAME);
        } else {
            blog.setAuthor(blogVO.getAuthor());
            blog.setArticlesPart(blogVO.getArticlesPart());
        }

        blog.setTitle(blogVO.getTitle());
        blog.setSummary(blogVO.getSummary());
        blog.setContent(blogVO.getContent());
        blog.setTagUid(blogVO.getTagUid());
        blog.setBlogSortUid(blogVO.getBlogSortUid());
        blog.setFileUid(blogVO.getFileUid());
        blog.setLevel(blogVO.getLevel());
        blog.setIsOriginal(blogVO.getIsOriginal());
        blog.setIsPublish(blogVO.getIsPublish());
        blog.setStartComment(blogVO.getStartComment());
        blog.setUpdateTime(new Date());
        blog.setStatus(EStatus.ENABLE);

        Boolean isSave = blog.updateById();

        //保存成功后，需要发送消息到solr 和 redis
        updateSolrAndRedis(isSave, blog);

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String editBatch(List<BlogVO> blogVOList) {
        if (blogVOList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> blogUidList = new ArrayList<>();
        Map<String, BlogVO> blogVOMap = new HashMap<>();
        blogVOList.forEach(item -> {
            blogUidList.add(item.getUid());
            blogVOMap.put(item.getUid(), item);
        });

        Collection<Blog> blogList = blogService.listByIds(blogUidList);
        blogList.forEach(blog -> {
            BlogVO blogVO = blogVOMap.get(blog.getUid());
            if (blogVO != null) {
                blog.setAuthor(blogVO.getAuthor());
                blog.setArticlesPart(blogVO.getArticlesPart());
                blog.setTitle(blogVO.getTitle());
                blog.setSummary(blogVO.getSummary());
                blog.setContent(blogVO.getContent());
                blog.setTagUid(blogVO.getTagUid());
                blog.setBlogSortUid(blogVO.getBlogSortUid());
                blog.setFileUid(blogVO.getFileUid());
                blog.setLevel(blogVO.getLevel());
                blog.setIsOriginal(blogVO.getIsOriginal());
                blog.setIsPublish(blogVO.getIsPublish());
                blog.setSort(blogVO.getSort());
                blog.setStatus(EStatus.ENABLE);
            }
        });
        Boolean save = blogService.updateBatchById(blogList);

        //保存成功后，需要发送消息到solr 和 redis
        if (save) {

            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.EDIT_BATCH);

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);

        }

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBlog(BlogVO blogVO) {
        Blog blog = blogService.getById(blogVO.getUid());
        blog.setStatus(EStatus.DISABLED);
        Boolean save = blog.updateById();

        //保存成功后，需要发送消息到solr 和 redis
        if (save) {
            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.DELETE);
            map.put(SysConf.BLOG_UID, blog.getUid());
            map.put(SysConf.LEVEL, blog.getLevel());
            map.put(SysConf.CREATE_TIME, blog.getCreateTime());

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);

        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String deleteBatchBlog(List<BlogVO> blogVoList) {
        if (blogVoList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        StringBuffer uidSbf = new StringBuffer();
        blogVoList.forEach(item -> {
            uids.add(item.getUid());
            uidSbf.append(item.getUid() + SysConf.FILE_SEGMENTATION);
        });
        Collection<Blog> blogList = blogService.listByIds(uids);

        blogList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
        });

        Boolean save = blogService.updateBatchById(blogList);

        //保存成功后，需要发送消息到solr 和 redis
        if (save) {

            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.DELETE_BATCH);
            map.put(SysConf.UID, uidSbf);

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);

        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String uploadLocalBlog(List<MultipartFile> filedatas) throws IOException {

        SystemConfig systemConfig = systemConfigService.getConfig();
        if(systemConfig == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        } else {
            if(EOpenStatus.OPEN.equals(systemConfig.getUploadQiNiu()) && (StringUtils.isEmpty(systemConfig.getQiNiuPictureBaseUrl()) || StringUtils.isEmpty(systemConfig.getQiNiuAccessKey())
                    || StringUtils.isEmpty(systemConfig.getQiNiuSecretKey()) || StringUtils.isEmpty(systemConfig.getQiNiuBucket()) || StringUtils.isEmpty(systemConfig.getQiNiuArea()))) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.PLEASE_SET_QI_NIU);
            }

            if(EOpenStatus.OPEN.equals(systemConfig.getUploadLocal()) && StringUtils.isEmpty(systemConfig.getLocalPictureBaseUrl())) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.PLEASE_SET_LOCAL);
            }
        }


        List<MultipartFile> fileList = new ArrayList<>();
        for(MultipartFile file : filedatas) {
            String fileName = file.getOriginalFilename();
            if(FileUtils.isMarkdown(fileName)) {
                fileList.add(file);
            } else {
                return ResultUtil.result(SysConf.ERROR, "目前仅支持Mrkdown文件");
            }
        }

        if(fileList.size() == 0) {
            return ResultUtil.result(SysConf.ERROR, "请选中需要上传的文件");
        }

        // 文档解析
        List<String> fileContentList = new ArrayList<>();
        for (MultipartFile multipartFile : fileList) {
            try {
                Reader reader = new InputStreamReader(multipartFile.getInputStream(), "utf-8");
                BufferedReader br = new BufferedReader(reader);
                String line;
                String content = "";
                while ((line = br.readLine()) != null) {
                    content += line + "\n";
                }
                // 将Markdown转换成html
                String blogContent = FileUtils.markdownToHtml(content);
                fileContentList.add(blogContent);
            } catch (Exception e) {
                log.error("文件解析出错");
                log.error(e.getMessage());
            }
        }

        HttpServletRequest request = RequestHolder.getRequest();
        String pictureList = request.getParameter(SysConf.PICTURE_LIST);
        List<LinkedTreeMap<String, String>> list = (List<LinkedTreeMap<String, String>>) JsonUtils.jsonArrayToArrayList(pictureList);
        Map<String, String> pictureMap = new HashMap<>();
        for (LinkedTreeMap<String, String> item : list) {

            if(EOpenStatus.OPEN.equals(systemConfig.getPicturePriority())) {
                // 获取七牛云上的图片
                pictureMap.put(item.get(SysConf.FILE_OLD_NAME), item.get(SysConf.QI_NIU_URL));
            } else {
                // 获取本地的图片
                pictureMap.put(item.get(SysConf.FILE_OLD_NAME), item.get(SysConf.PIC_URL));
            }
        }
        // 需要替换的图片Map
        Map<String, String> matchUrlMap = new HashMap<>();
        for (String blogContent : fileContentList) {
        List<String> matchList = RegexUtils.match(blogContent, "<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)>");
        for (String matchStr : matchList) {
            String [] splitList = matchStr.split("\"");
            // 取出中间的图片
            if(splitList.length >= 5) {
                // alt 和 src的先后顺序
                // 得到具体的图片路径
                String pictureUrl = "";
                if(matchStr.indexOf("alt") > matchStr.indexOf("src")) {
                    pictureUrl = splitList[1];
                } else {
                    pictureUrl = splitList[3];
                }

                // 判断是网络图片还是本地图片
                if(!pictureUrl.startsWith(SysConf.HTTP)) {
                    // 那么就需要遍历全部的map和他匹配
                    for(Map.Entry<String, String> map : pictureMap.entrySet()){
                        // 查看Map中的图片是否在需要替换的key中
                        if(pictureUrl.indexOf(map.getKey()) > -1) {
                            if(EOpenStatus.OPEN.equals(systemConfig.getPicturePriority())) {
                                matchUrlMap.put(pictureUrl, systemConfig.getQiNiuPictureBaseUrl() + map.getValue());
                            } else {
                                matchUrlMap.put(pictureUrl, systemConfig.getLocalPictureBaseUrl() + map.getValue());
                            }
                            break;
                        }
                    }
                }
            }
        }

            // 获取一个排序最高的博客分类和标签
            BlogSort blogSort = blogSortService.getTopOne();
            Tag tag = tagService.getTopTag();
            // 获取任意博客封面
            Picture picture = pictureService.getTopOne();
            if(blogSort == null || tag == null || picture == null) {
                return ResultUtil.result(SysConf.ERROR, "使用本地上传，请先确保博客分类，博客标签，博客图片中含有数据");
            }

            // 获取当前管理员
            Admin admin = adminService.getMe();

            // 存储需要上传的博客
            List<Blog> blogList = new ArrayList<>();

            // 开始进行图片替换操作
            for (String content : fileContentList) {
                // 循环替换里面的图片
                for(Map.Entry<String, String> map : matchUrlMap.entrySet()){
                    content = content.replace(map.getKey(), map.getValue());
                }
                Blog blog = new Blog();
                blog.setBlogSortUid(blogSort.getUid());
                blog.setTagUid(tag.getUid());
                blog.setAdminUid(admin.getUid());
                blog.setAuthor(admin.getNickName());
                blog.setArticlesPart(PROJECT_NAME);
                blog.setLevel(ELevel.NORMAL);
                blog.setTitle("默认标题");
                blog.setSummary("默认简介");
                blog.setContent(content);
                blog.setFileUid(picture.getFileUid());
                blog.setIsOriginal(EOriginal.ORIGINAL);
                blog.setIsPublish(EPublish.NO_PUBLISH);
                blog.setStartComment(EOpenStatus.OPEN);
                blogList.add(blog);
            }
            // 批量添加博客
            blogService.saveBatch(blogList);
        }

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }


    @Override
    public IPage<Blog> getBlogPageByLevel(Integer level, Long currentPage, Integer useSort) {

        //从Redis中获取内容
        String jsonResult = redisUtil.get(RedisConf.BLOG_LEVEL + RedisConf.SEGMENTATION + level);

        //判断redis中是否有文章
        if (StringUtils.isNotEmpty(jsonResult)) {
            List list = JsonUtils.jsonArrayToArrayList(jsonResult);
            IPage pageList = new Page();
            pageList.setRecords(list);
            return pageList;
        }
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        switch (level) {
            case ELevel.NORMAL: {
                page.setSize(BLOG_NEW_COUNT);
            }
            break;
            case ELevel.FIRST: {
                page.setSize(BLOG_FIRST_COUNT);
            }
            break;
            case ELevel.SECOND: {
                page.setSize(BLOG_SECOND_COUNT);
            }
            break;
            case ELevel.THIRD: {
                page.setSize(BLOG_THIRD_COUNT);
            }
            break;
            case ELevel.FOURTH: {
                page.setSize(BLOG_FOURTH_COUNT);
            }
            break;
        }
        IPage<Blog> pageList = blogService.getBlogPageByLevel(page, level, useSort);
        List<Blog> list = pageList.getRecords();

        // 一级推荐或者二级推荐没有内容时，自动把top5填充至一级推荐和二级推荐中
        if ((level == SysConf.ONE || level == SysConf.TWO) && list.size() == 0) {
            QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
            Page<Blog> hotPage = new Page<>();
            hotPage.setCurrent(1);
            hotPage.setSize(BLOG_HOT_COUNT);
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
            queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
            queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
            IPage<Blog> hotPageList = blogService.page(hotPage, queryWrapper);
            List<Blog> hotBlogList = hotPageList.getRecords();
            List<Blog> secondBlogList = new ArrayList<>();
            List<Blog> firstBlogList = new ArrayList<>();
            for (int a = 0; a < hotBlogList.size(); a++) {
                // 当推荐大于两个的时候
                if ((hotBlogList.size() - firstBlogList.size()) > BLOG_SECOND_COUNT) {
                    firstBlogList.add(hotBlogList.get(a));
                } else {
                    secondBlogList.add(hotBlogList.get(a));
                }
            }

            firstBlogList = setBlog(firstBlogList);
            secondBlogList = setBlog(secondBlogList);

            //将从数据库查询的数据缓存到redis中，设置1小时后过期
            redisUtil.setEx(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.ONE, JsonUtils.objectToJson(firstBlogList).toString(), 1, TimeUnit.HOURS);
            redisUtil.setEx(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + SysConf.TWO, JsonUtils.objectToJson(secondBlogList).toString(), 1, TimeUnit.HOURS);

            switch (level) {
                case SysConf.ONE: {
                    pageList.setRecords(firstBlogList);
                }
                ;
                break;
                case SysConf.TWO: {
                    pageList.setRecords(secondBlogList);
                }
                ;
                break;
            }
            return pageList;
        }

        list = setBlog(list);

        pageList.setRecords(list);

        //将从数据库查询的数据缓存到redis中
        redisUtil.setEx(SysConf.BLOG_LEVEL + SysConf.REDIS_SEGMENTATION + level, JsonUtils.objectToJson(list).toString(), 1, TimeUnit.HOURS);
        return pageList;
    }

    @Override
    public IPage<Blog> getHotBlog() {
        //从Redis中获取内容
        String jsonResult = redisUtil.get(SysConf.HOT_BLOG);
        //判断redis中是否有文章
        if (StringUtils.isNotEmpty(jsonResult)) {
            List list = JsonUtils.jsonArrayToArrayList(jsonResult);
            IPage pageList = new Page();
            pageList.setRecords(list);
            return pageList;
        }
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
        page.setCurrent(0);
        page.setSize(BLOG_HOT_COUNT);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = setBlog(list);
        //将从热门博客缓存到redis中
        redisUtil.setEx(SysConf.HOT_BLOG, JsonUtils.objectToJson(list).toString(), 1, TimeUnit.HOURS);
        pageList.setRecords(list);
        return null;
    }

    @Override
    public IPage<Blog> getNewBlog(Long currentPage, Long pageSize) {
        // 只缓存第一页的内容
        if (currentPage == 1L) {
            //从Redis中获取内容
            String jsonResult = redisUtil.get(SysConf.NEW_BLOG);

            //判断redis中是否有文章
            if (StringUtils.isNotEmpty(jsonResult)) {
                List list = JsonUtils.jsonArrayToArrayList(jsonResult);
                IPage pageList = new Page();
                pageList.setRecords(list);
                return pageList;
            }
        }
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(BLOG_NEW_COUNT);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));

        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();

        if (list.size() <= 0) {
            return pageList;
        }

        list = setBlog(list);

        //将从最新博客缓存到redis中
        if (currentPage == 1L) {
            redisUtil.setEx(SysConf.NEW_BLOG, JsonUtils.objectToJson(list).toString(), 1, TimeUnit.HOURS);
        }
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public IPage<Blog> getBlogByTime(Long currentPage, Long pageSize) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));

        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();

        list = setBlog(list);
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public Integer getBlogPraiseCountByUid(String uid) {
        Integer pariseCount = 0;
        if (StringUtils.isEmpty(uid)) {
            log.error("传入的UID为空");
            return pariseCount;
        }
        //从Redis取出用户点赞数据
        String pariseJsonResult = redisUtil.get(RedisConf.BLOG_PRAISE + RedisConf.SEGMENTATION + uid);
        if (!StringUtils.isEmpty(pariseJsonResult)) {
            pariseCount = Integer.parseInt(pariseJsonResult);
        }
        return pariseCount;
    }

    @Override
    public String praiseBlogByUid(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }

        HttpServletRequest request = RequestHolder.getRequest();
        String ip = IpUtils.getIpAddr(request);

        // 如果用户登录了
        if (request.getAttribute(SysConf.USER_UID) != null) {
            String userUid = request.getAttribute(SysConf.USER_UID).toString();
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.USER_UID, userUid);
            queryWrapper.eq(SQLConf.BLOG_UID, uid);
            queryWrapper.eq(SQLConf.TYPE, ECommentType.PRAISE);
            queryWrapper.last("LIMIT 1");
            Comment praise = commentService.getOne(queryWrapper);
            if (praise != null) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.YOU_HAVE_BEEN_PRISE);
            }
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PLEASE_LOGIN_TO_PRISE);
        }

        Blog blog = blogService.getById(uid);

        String pariseJsonResult = redisUtil.get(RedisConf.BLOG_PRAISE + RedisConf.SEGMENTATION + uid);

        if (StringUtils.isEmpty(pariseJsonResult)) {

            //给该博客点赞数
            redisUtil.set(RedisConf.BLOG_PRAISE + RedisConf.SEGMENTATION + uid, "1");

            blog.setCollectCount(1);
            blog.updateById();

        } else {

            Integer count = blog.getCollectCount() + 1;

            //给该博客点赞 +1
            redisUtil.set(RedisConf.BLOG_PRAISE + RedisConf.SEGMENTATION + uid, count.toString());

            blog.setCollectCount(count);
            blog.updateById();
        }

        // 已登录用户，向评论表添加点赞数据
        if (request.getAttribute(SysConf.USER_UID) != null) {
            String userUid = request.getAttribute(SysConf.USER_UID).toString();
            Comment comment = new Comment();
            comment.setUserUid(userUid);
            comment.setBlogUid(uid);
            comment.setSource(ECommentSource.BLOG_INFO.getCode());
            comment.setType(ECommentType.PRAISE);
            comment.insert();
        }
        return ResultUtil.result(SysConf.SUCCESS, blog.getCollectCount());
    }

    @Override
    public IPage<Blog> getSameBlogByTagUid(String tagUid) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);
        queryWrapper.like(SQLConf.TAG_UID, tagUid);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = blogService.setTagAndSortByBlogList(list);
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public IPage<Blog> getListByBlogSortUid(String blogSortUid, Long currentPage, Long pageSize) {
        //分页
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.eq(SQLConf.BLOG_SORT_UID, blogSortUid);

        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        IPage<Blog> pageList = blogService.page(page, queryWrapper);

        //给博客增加标签和分类
        List<Blog> list = blogService.setTagAndSortByBlogList(pageList.getRecords());
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public Map<String, Object> getBlogByKeyword(String keywords, Long currentPage, Long pageSize) {
        final String keyword = keywords.trim();
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.like(SQLConf.TITLE, keyword).or().like(SQLConf.SUMMARY, keyword));
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        IPage<Blog> iPage = blogService.page(page, queryWrapper);

        List<Blog> blogList = iPage.getRecords();

        List<String> blogSortUidList = new ArrayList<>();
        Map<String, String> pictureMap = new HashMap<>();
        final StringBuffer fileUids = new StringBuffer();

        blogList.forEach(item -> {

            // 获取图片uid
            blogSortUidList.add(item.getBlogSortUid());
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }

            // 给标题和简介设置高亮
            item.setTitle(getHitCode(item.getTitle(), keyword));
            item.setSummary(getHitCode(item.getTitle(), keyword));

        });

        // 调用图片接口，获取图片
        String pictureList = null;
        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        Collection<BlogSort> blogSortList = new ArrayList<>();
        if (blogSortUidList.size() > 0) {
            blogSortList = blogSortService.listByIds(blogSortUidList);
        }

        Map<String, String> blogSortMap = new HashMap<>();
        blogSortList.forEach(item -> {
            blogSortMap.put(item.getUid(), item.getSortName());
        });

        // 设置分类名 和 图片
        blogList.forEach(item -> {
            if (blogSortMap.get(item.getBlogSortUid()) != null) {
                item.setBlogSortName(blogSortMap.get(item.getBlogSortUid()));
            }

            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                // 只设置一张标题图
                if (pictureListTemp.size() > 0) {
                    item.setPhotoUrl(pictureListTemp.get(0));
                } else {
                    item.setPhotoUrl("");
                }
            }
        });


        Map<String, Object> map = new HashMap<>();

        // 返回总记录数
        map.put(SysConf.TOTAL, iPage.getTotal());

        // 返回总页数
        map.put(SysConf.TOTAL_PAGE, iPage.getPages());

        // 返回当前页大小
        map.put(SysConf.PAGE_SIZE, pageSize);

        // 返回当前页
        map.put(SysConf.CURRENT_PAGE, iPage.getCurrent());

        // 返回数据
        map.put(SysConf.BLOG_LIST, blogList);

        return map;
    }

    @Override
    public IPage<Blog> searchBlogByTag(String tagUid, Long currentPage, Long pageSize) {
        Tag tag = tagService.getById(tagUid);
        if (tag != null) {
            HttpServletRequest request = RequestHolder.getRequest();
            String ip = IpUtils.getIpAddr(request);
            //从Redis取出数据，判断该用户24小时内，是否点击过该标签
            String jsonResult = redisUtil.get(RedisConf.TAG_CLICK + RedisConf.SEGMENTATION + ip + "#" + tagUid);

            if (StringUtils.isEmpty(jsonResult)) {

                //给标签点击数增加
                int clickCount = tag.getClickCount() + 1;
                tag.setClickCount(clickCount);
                tag.updateById();
                //将该用户点击记录存储到redis中, 24小时后过期
                redisUtil.setEx(RedisConf.TAG_CLICK + RedisConf.SEGMENTATION + ip + RedisConf.WELL_NUMBER + tagUid, clickCount + "",
                        24, TimeUnit.HOURS);
            }

        }
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        queryWrapper.like(SQLConf.TagUid, tagUid);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SysConf.CONTENT));
        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = blogService.setTagAndSortAndPictureByBlogList(list);
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public IPage<Blog> searchBlogByBlogSort(String blogSortUid, Long currentPage, Long pageSize) {
        BlogSort blogSort = blogSortService.getById(blogSortUid);
        if (blogSort != null) {
            HttpServletRequest request = RequestHolder.getRequest();
            String ip = IpUtils.getIpAddr(request);

            //从Redis取出数据，判断该用户24小时内，是否点击过该分类
            String jsonResult = redisUtil.get(RedisConf.TAG_CLICK + RedisConf.SEGMENTATION + ip + RedisConf.WELL_NUMBER + blogSortUid);

            if (StringUtils.isEmpty(jsonResult)) {

                //给标签点击数增加
                int clickCount = blogSort.getClickCount() + 1;
                blogSort.setClickCount(clickCount);
                blogSort.updateById();

                //将该用户点击记录存储到redis中, 24小时后过期
                redisUtil.setEx(RedisConf.TAG_CLICK + RedisConf.SEGMENTATION + ip + RedisConf.WELL_NUMBER + blogSortUid, clickCount + "",
                        24, TimeUnit.HOURS);
            }

        }

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        queryWrapper.eq(SQLConf.BLOG_SORT_UID, blogSortUid);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);

        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SysConf.CONTENT));
        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = blogService.setTagAndSortAndPictureByBlogList(list);
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public IPage<Blog> searchBlogByAuthor(String author, Long currentPage, Long pageSize) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        queryWrapper.eq(SQLConf.AUTHOR, author);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SysConf.CONTENT));

        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = blogService.setTagAndSortAndPictureByBlogList(list);
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public String getBlogTimeSortList() {
        //从Redis中获取内容
        String monthResult = redisUtil.get(SysConf.MONTH_SET);

        //判断redis中时候包含归档的内容
        if (StringUtils.isNotEmpty(monthResult)) {
            List list = JsonUtils.jsonArrayToArrayList(monthResult);
            return ResultUtil.result(SysConf.SUCCESS, list);
        }

        // 第一次启动的时候归档
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);

        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        List<Blog> list = blogService.list(queryWrapper);

        //给博客增加标签和分类
        list = blogService.setTagAndSortByBlogList(list);

        Map<String, List<Blog>> map = new HashMap<>();
        Iterator iterable = list.iterator();
        Set<String> monthSet = new TreeSet<>();
        while (iterable.hasNext()) {
            Blog blog = (Blog) iterable.next();
            Date createTime = blog.getCreateTime();

            String month = new SimpleDateFormat("yyyy年MM月").format(createTime).toString();

            monthSet.add(month);

            if (map.get(month) == null) {
                List<Blog> blogList = new ArrayList<>();
                blogList.add(blog);
                map.put(month, blogList);
            } else {
                List<Blog> blogList = map.get(month);
                blogList.add(blog);
                map.put(month, blogList);
            }
        }

        // 缓存该月份下的所有文章  key: 月份   value：月份下的所有文章
        map.forEach((key, value) -> {
            redisUtil.set(SysConf.BLOG_SORT_BY_MONTH + SysConf.REDIS_SEGMENTATION + key, JsonUtils.objectToJson(value).toString());
        });

        //将从数据库查询的数据缓存到redis中
        redisUtil.set(SysConf.MONTH_SET, JsonUtils.objectToJson(monthSet).toString());
        return ResultUtil.result(SysConf.SUCCESS, monthSet);
    }

    @Override
    public String getArticleByMonth(String monthDate) {
        if (StringUtils.isEmpty(monthDate)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        //从Redis中获取内容
        String contentResult = redisUtil.get(SysConf.BLOG_SORT_BY_MONTH + SysConf.REDIS_SEGMENTATION + monthDate);

        //判断redis中时候包含该日期下的文章
        if (StringUtils.isNotEmpty(contentResult)) {
            List list = JsonUtils.jsonArrayToArrayList(contentResult);
            return ResultUtil.result(SysConf.SUCCESS, list);
        }

        // 第一次启动的时候归档
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        //因为首页并不需要显示内容，所以需要排除掉内容字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        List<Blog> list = blogService.list(queryWrapper);

        //给博客增加标签和分类
        list = blogService.setTagAndSortByBlogList(list);

        Map<String, List<Blog>> map = new HashMap<>();
        Iterator iterable = list.iterator();
        Set<String> monthSet = new TreeSet<>();
        while (iterable.hasNext()) {
            Blog blog = (Blog) iterable.next();
            Date createTime = blog.getCreateTime();

            String month = new SimpleDateFormat("yyyy年MM月").format(createTime).toString();

            monthSet.add(month);

            if (map.get(month) == null) {
                List<Blog> blogList = new ArrayList<>();
                blogList.add(blog);
                map.put(month, blogList);
            } else {
                List<Blog> blogList = map.get(month);
                blogList.add(blog);
                map.put(month, blogList);
            }
        }

        // 缓存该月份下的所有文章  key: 月份   value：月份下的所有文章
        map.forEach((key, value) -> {
            redisUtil.set(SysConf.BLOG_SORT_BY_MONTH + SysConf.REDIS_SEGMENTATION + key, JsonUtils.objectToJson(value).toString());
        });
        //将从数据库查询的数据缓存到redis中
        redisUtil.set(SysConf.MONTH_SET, JsonUtils.objectToJson(monthSet).toString());
        return ResultUtil.result(SysConf.SUCCESS, map.get(monthDate));
    }

    /**
     * 添加时校验
     *
     * @param count
     * @param level
     * @return
     */
    private String addVerdict(Integer count, Integer level) {
        //添加的时候进行判断
        switch (level) {
            case ELevel.FIRST: {
                if (count > BLOG_FIRST_COUNT) {
                    return ResultUtil.result(SysConf.ERROR, "一级推荐不能超过" + BLOG_FIRST_COUNT + "个");
                }
            }
            break;

            case ELevel.SECOND: {
                if (count > BLOG_SECOND_COUNT) {
                    return ResultUtil.result(SysConf.ERROR, "二级推荐不能超过" + BLOG_SECOND_COUNT + "个");
                }
            }
            break;

            case ELevel.THIRD: {
                if (count > BLOG_THIRD_COUNT) {
                    return ResultUtil.result(SysConf.ERROR, "三级推荐不能超过" + BLOG_THIRD_COUNT + "个");
                }
            }
            break;

            case ELevel.FOURTH: {
                if (count > BLOG_FOURTH_COUNT) {
                    return ResultUtil.result(SysConf.ERROR, "四级推荐不能超过" + BLOG_FOURTH_COUNT + "个");
                }
            }
            break;
            default: {

            }
        }
        return null;
    }

    /**
     * 保存成功后，需要发送消息到solr 和 redis
     *
     * @param isSave
     * @param blog
     */
    private void updateSolrAndRedis(Boolean isSave, Blog blog) {
        // 保存操作，并且文章已设置发布
        if (isSave && EPublish.PUBLISH.equals(blog.getIsPublish())) {
            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.ADD);
            map.put(SysConf.BLOG_UID, blog.getUid());
            map.put(SysConf.LEVEL, blog.getLevel());
            map.put(SysConf.CREATE_TIME, blog.getCreateTime());

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);

        } else if (EPublish.NO_PUBLISH.equals(blog.getIsPublish())) {

            //这是需要做的是，是删除redis中的该条博客数据
            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.EDIT);
            map.put(SysConf.BLOG_UID, blog.getUid());
            map.put(SysConf.LEVEL, blog.getLevel());
            map.put(SysConf.CREATE_TIME, blog.getCreateTime());

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);
        }
    }

    /**
     * 设置博客的分类标签和内容
     *
     * @param list
     * @return
     */
    private List<Blog> setBlog(List<Blog> list) {
        final StringBuffer fileUids = new StringBuffer();
        List<String> sortUids = new ArrayList<>();
        List<String> tagUids = new ArrayList<>();

        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                sortUids.add(item.getBlogSortUid());
            }
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                tagUids.add(item.getTagUid());
            }
        });
        String pictureList = null;

        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);
        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();
        if (sortUids.size() > 0) {
            sortList = blogSortService.listByIds(sortUids);
        }
        if (tagUids.size() > 0) {
            tagList = tagService.listByIds(tagUids);
        }


        Map<String, BlogSort> sortMap = new HashMap<>();
        Map<String, Tag> tagMap = new HashMap<>();
        Map<String, String> pictureMap = new HashMap<>();

        sortList.forEach(item -> {
            sortMap.put(item.getUid(), item);
        });

        tagList.forEach(item -> {
            tagMap.put(item.getUid(), item);
        });

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });


        for (Blog item : list) {

            //设置分类
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                item.setBlogSort(sortMap.get(item.getBlogSortUid()));
            }

            //获取标签
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), SysConf.FILE_SEGMENTATION);
                List<Tag> tagListTemp = new ArrayList<Tag>();

                tagUidsTemp.forEach(tag -> {
                    tagListTemp.add(tagMap.get(tag));
                });
                item.setTagList(tagListTemp);
            }

            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        return list;
    }

    /**
     * 添加高亮
     *
     * @param str
     * @param keyword
     * @return
     */
    private String getHitCode(String str, String keyword) {

        if (StringUtils.isEmpty(keyword) || StringUtils.isEmpty(str)) {
            return str;
        }

        String startStr = "<span style = 'color:red'>";
        String endStr = "</span>";

        // 判断关键字是否直接是搜索的内容，否者直接返回
        if (str.equals(keyword)) {
            return startStr + str + endStr;
        }

        String lowerCaseStr = str.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        String[] lowerCaseArray = lowerCaseStr.split(lowerKeyword);

        Boolean isEndWith = lowerCaseStr.endsWith(lowerKeyword);

        // 计算分割后的字符串位置
        Integer count = 0;
        List<Map<String, Integer>> list = new ArrayList<>();
        List<Map<String, Integer>> keyList = new ArrayList<>();
        for (int a = 0; a < lowerCaseArray.length; a++) {

            // 将切割出来的存储map
            Map<String, Integer> map = new HashMap<>();
            Map<String, Integer> keyMap = new HashMap<>();
            map.put("startIndex", count);
            Integer len = lowerCaseArray[a].length();
            count += len;
            map.put("endIndex", count);
            list.add(map);

            if (a < lowerCaseArray.length - 1 || isEndWith) {
                // 将keyword存储map
                keyMap.put("startIndex", count);
                count += keyword.length();
                keyMap.put("endIndex", count);
                keyList.add(keyMap);
            }

        }

        // 截取切割对象
        List<String> arrayList = new ArrayList<>();
        for (Map<String, Integer> item : list) {
            Integer start = item.get("startIndex");
            Integer end = item.get("endIndex");
            String itemStr = str.substring(start, end);
            arrayList.add(itemStr);
        }

        // 截取关键字
        List<String> keyArrayList = new ArrayList<>();
        for (Map<String, Integer> item : keyList) {
            Integer start = item.get("startIndex");
            Integer end = item.get("endIndex");
            String itemStr = str.substring(start, end);
            keyArrayList.add(itemStr);
        }

        StringBuffer sb = new StringBuffer();

        for (int a = 0; a < arrayList.size(); a++) {

            sb.append(arrayList.get(a));

            if (a < arrayList.size() - 1 || isEndWith) {
                sb.append(startStr);
                sb.append(keyArrayList.get(a));
                sb.append(endStr);
            }
        }
        return sb.toString();
    }
}
