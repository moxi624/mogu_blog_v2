package com.moxi.mogublog.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/search")
@Api(value = "搜索RestApi", tags = {"SearchRestApi"})
public class SearchRestApi {

    private static Logger log = LogManager.getLogger(SearchRestApi.class);
    @Autowired
    TagService tagService;
    @Autowired
    BlogSortService blogSortService;
    @Autowired
    private BlogSearchService blogSearchService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private WebVisitService webVisitService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value(value = "${spring.data.solr.core}")
    private String collection;

    @ApiOperation(value = "搜索Blog", notes = "搜索Blog")
    @GetMapping("/searchBlog")
    public String searchBlog(HttpServletRequest request,
                             @ApiParam(name = "keywords", value = "关键字", required = true) @RequestParam(required = true) String keywords,
                             @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                             @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if (StringUtils.isEmpty(keywords)) {
            return ResultUtil.result(SysConf.ERROR, "关键字不能为空");
        }

        Map<String, Object> map = blogSearchService.search(collection, keywords, currentPage, pageSize);

        //增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, request, EBehavior.BLOG_SEARCH.getBehavior(), null, keywords);

        return ResultUtil.result(SysConf.SUCCESS, map);

    }

    @ApiOperation(value = "根据标签获取相关的博客", notes = "根据标签获取相关的博客")
    @GetMapping("/searchBlogByTag")
    public String searchBlogByTag(HttpServletRequest request,
                                  @ApiParam(name = "tagUid", value = "博客标签UID", required = true) @RequestParam(name = "tagUid", required = true) String tagUid,
                                  @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                  @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
        if (StringUtils.isEmpty(tagUid)) {
            return ResultUtil.result(SysConf.ERROR, "标签不能为空");
        }

        Tag tag = tagService.getById(tagUid);
        if (tag != null) {

            String ip = IpUtils.getIpAddr(request);

            //从Redis取出数据，判断该用户24小时内，是否点击过该标签
            String jsonResult = stringRedisTemplate.opsForValue().get("TAG_CLICK:" + ip + "#" + tagUid);

            if (StringUtils.isEmpty(jsonResult)) {

                //给标签点击数增加
                log.info("点击数加1");
                int clickCount = tag.getClickCount() + 1;
                tag.setClickCount(clickCount);
                tag.updateById();

                //将该用户点击记录存储到redis中, 24小时后过期
                stringRedisTemplate.opsForValue().set("TAG_CLICK:" + ip + "#" + tagUid, clickCount + "",
                        24, TimeUnit.HOURS);
            }

        } else {
            return ResultUtil.result(SysConf.ERROR, "标签不能为空");
        }

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        queryWrapper.like(SQLConf.TagUid, tagUid);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals("content"));
        ;

        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();

        list = setBlog(list);

        //增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, request, EBehavior.BLOG_TAG.getBehavior(), tagUid, null);

        pageList.setRecords(list);

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "根据分类获取相关的博客", notes = "根据标签获取相关的博客")
    @GetMapping("/searchBlogBySort")
    public String searchBlogBySort(HttpServletRequest request,
                                   @ApiParam(name = "blogSortUid", value = "博客分类UID", required = true) @RequestParam(name = "blogSortUid", required = true) String blogSortUid,
                                   @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                   @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
        if (StringUtils.isEmpty(blogSortUid)) {
            return ResultUtil.result(SysConf.ERROR, "uid不能为空");
        }
        BlogSort blogSort = blogSortService.getById(blogSortUid);
        if (blogSort != null) {

            String ip = IpUtils.getIpAddr(request);

            //从Redis取出数据，判断该用户24小时内，是否点击过该分类
            String jsonResult = stringRedisTemplate.opsForValue().get("BLOG_SORT_CLICK:" + ip + "#" + blogSortUid);

            if (StringUtils.isEmpty(jsonResult)) {

                //给标签点击数增加
                log.info("点击数加1");
                int clickCount = blogSort.getClickCount() + 1;
                blogSort.setClickCount(clickCount);
                blogSort.updateById();

                //将该用户点击记录存储到redis中, 24小时后过期
                stringRedisTemplate.opsForValue().set("BLOG_SORT_CLICK:" + ip + "#" + blogSortUid, clickCount + "",
                        24, TimeUnit.HOURS);
            }

        } else {
            return ResultUtil.result(SysConf.ERROR, "标签不能为空");
        }

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        queryWrapper.eq(SQLConf.BLOG_SORT_UID, blogSortUid);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);

        queryWrapper.select(Blog.class, i -> !i.getProperty().equals("content"));
        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = setBlog(list);

        //增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, request, EBehavior.BLOG_SORT.getBehavior(), blogSortUid, null);

        pageList.setRecords(list);

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "根据作者获取相关的博客", notes = "根据作者获取相关的博客")
    @GetMapping("/searchBlogByAuthor")
    public String searchBlogByAuthor(HttpServletRequest request,
                                     @ApiParam(name = "author", value = "作者名称", required = true) @RequestParam(name = "author", required = true) String author,
                                     @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                                     @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
        if (StringUtils.isEmpty(author)) {
            return ResultUtil.result(SysConf.ERROR, "作者不能为空");
        }
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        Page<Blog> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        queryWrapper.eq(SQLConf.AUTHOR, author);
        queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        queryWrapper.select(Blog.class, i -> !i.getProperty().equals("content"));

        IPage<Blog> pageList = blogService.page(page, queryWrapper);
        List<Blog> list = pageList.getRecords();
        list = setBlog(list);

        //增加记录（可以考虑使用AOP）
        webVisitService.addWebVisit(null, request, EBehavior.BLOG_AUTHOR.getBehavior(), null, author);
        pageList.setRecords(list);

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }


    /**
     * 设置博客的分类标签和内容
     *
     * @param list
     * @return
     */
    private List<Blog> setBlog(List<Blog> list) {
        final StringBuffer fileUids = new StringBuffer();
        List<String> sortUids = new ArrayList<String>();
        List<String> tagUids = new ArrayList<String>();

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
        List<Map<String, Object>> picList = WebUtils.getPictureMap(pictureList);
        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();
        if (sortUids.size() > 0) {
            sortList = blogSortService.listByIds(sortUids);
        }
        if (tagUids.size() > 0) {
            tagList = tagService.listByIds(tagUids);
        }


        Map<String, BlogSort> sortMap = new HashMap<String, BlogSort>();
        Map<String, Tag> tagMap = new HashMap<String, Tag>();
        Map<String, String> pictureMap = new HashMap<String, String>();

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
            }
        }
        return list;
    }


}
