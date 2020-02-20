package com.moxi.mogublog.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.util.WebUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/search")
@Api(value = "SQL搜索RestApi", tags = {"SearchRestApi"})
@Slf4j
public class SearchRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    TagService tagService;

    @Autowired
    BlogSortService blogSortService;

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

    /**
     * 使用SQL语句搜索博客，如需使用Solr或者ElasticSearch 需要启动 mogu-search
     *
     * @param request
     * @param keywords
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "搜索Blog", notes = "搜索Blog")
    @GetMapping("/sqlSearchBlog")
    public String sqlSearchBlog(HttpServletRequest request,
                                @ApiParam(name = "keywords", value = "关键字", required = true) @RequestParam(required = true) String keywords,
                                @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if (StringUtils.isEmpty(keywords) || StringUtils.isEmpty(keywords.trim()) ) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.KEYWORD_IS_NOT_EMPTY);
        }

        final String keyword = keywords.trim();

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(SQLConf.TITLE, keyword).or().like(SQLConf.SUMMARY, keyword);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
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
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureList);

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        Collection<BlogSort> blogSortList = new ArrayList<>();
        if(blogSortUidList.size() > 0) {
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
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureList);
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

    /**
     * 添加高亮
     * @param str
     * @param keyword
     * @return
     */
    private String getHitCode(String str , String keyword) {

        if(StringUtils.isEmpty(keyword) || StringUtils.isEmpty(str)) {
            return str;
        }

        String startStr = "<span style = 'color:red'>";
        String endStr = "</span>";

        // 判断关键字是否直接是搜索的内容，否者直接返回
        if(str.equals(keyword)) {
            return startStr + str + endStr;
        }

        String lowerCaseStr = str.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        String [] lowerCaseArray = lowerCaseStr.split(lowerKeyword);

        Boolean isEndWith = lowerCaseStr.endsWith(lowerKeyword);

        // 计算分割后的字符串位置
        Integer count = 0;
        List<Map<String, Integer>> list = new ArrayList<>();
        List<Map<String, Integer>> keyList = new ArrayList<>();
        for(int a=0; a<lowerCaseArray.length; a++) {

            // 将切割出来的存储map
            Map<String, Integer> map = new HashMap<>();
            Map<String, Integer> keyMap = new HashMap<>();
            map.put("startIndex", count);
            Integer len = lowerCaseArray[a].length();
            count += len;
            map.put("endIndex", count);
            list.add(map);

            if(a < lowerCaseArray.length -1 || isEndWith) {
                // 将keyword存储map
                keyMap.put("startIndex", count);
                count += keyword.length();
                keyMap.put("endIndex", count);
                keyList.add(keyMap);
            }

        }

        // 截取切割对象
        List<String> arrayList = new ArrayList<>();
        for(Map<String, Integer> item : list) {
            Integer start = item.get("startIndex");
            Integer end = item.get("endIndex");
            String itemStr = str.substring(start, end);
            arrayList.add(itemStr);
        }

        // 截取关键字
        List<String> keyArrayList = new ArrayList<>();
        for(Map<String, Integer> item : keyList) {
            Integer start = item.get("startIndex");
            Integer end = item.get("endIndex");
            String itemStr = str.substring(start, end);
            keyArrayList.add(itemStr);
        }

        StringBuffer sb = new StringBuffer();

        for(int a=0; a<arrayList.size(); a++) {

            sb.append(arrayList.get(a));

            if(a < arrayList.size() - 1 || isEndWith) {
                sb.append(startStr);
                sb.append(keyArrayList.get(a));
                sb.append(endStr);
            }
        }

        return sb.toString();
    }
}
