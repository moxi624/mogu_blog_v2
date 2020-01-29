package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.feign.SearchFeignClient;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.BlogSortService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.vo.BlogVO;
import com.moxi.mougblog.base.enums.ELevel;
import com.moxi.mougblog.base.enums.EOriginal;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 博客表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */

@RestController
@RequestMapping("/blog")
@Api(value = "博客RestApi", tags = {"BlogRestApi"})
@Slf4j
public class BlogRestApi {

    @Autowired
    BlogService blogService;

    @Autowired
    WebUtils webUtils;

    @Autowired
    TagService tagService;
    @Autowired
    BlogSortService blogSortService;
    @Autowired
    AdminService adminService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private SearchFeignClient searchFeignClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value(value = "${data.image.url}")
    private String IMG_HOST;
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

    @ApiOperation(value = "获取博客列表", notes = "获取博客列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);
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
            return ResultUtil.result(SysConf.SUCCESS, pageList);
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

        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureList);
        Collection<BlogSort> sortList = blogSortService.listByIds(sortUids);
        Collection<Tag> tagList = tagService.listByIds(tagUids);

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
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加博客")
    @ApiOperation(value = "增加博客", notes = "增加博客", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

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
        Boolean isSave = blogService.save(blog);

        //保存成功后，需要发送消息到solr 和 redis
        updateSolrAndRedis(isSave, blog);

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @OperationLogger(value = "编辑博客")
    @ApiOperation(value = "编辑博客", notes = "编辑博客", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

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
        blog.setStatus(EStatus.ENABLE);

        Boolean isSave = blog.updateById();

        //保存成功后，需要发送消息到solr 和 redis
        updateSolrAndRedis(isSave, blog);

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @OperationLogger(value = "推荐博客排序调整")
    @ApiOperation(value = "推荐博客排序调整", notes = "推荐博客排序调整", response = String.class)
    @PostMapping("/editBatch")
    public String editBatch(HttpServletRequest request, @RequestBody List<BlogVO> blogVOList) {

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

    @OperationLogger(value = "删除博客")
    @ApiOperation(value = "删除博客", notes = "删除博客", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

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

    @OperationLogger(value = "删除选中博客")
    @ApiOperation(value = "删除选中博客", notes = "删除选中博客", response = String.class)
    @PostMapping("/deleteBatch")
    public String deleteBatch(HttpServletRequest request, @RequestBody List<BlogVO> blogVoList) {

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
     * 设置图片
     *
     * @param blog
     */
    private void setPhoto(Blog blog) {
        if (StringUtils.isNotEmpty(blog.getFileUid())) {
            String pictureList = this.pictureFeignClient.getPicture(blog.getFileUid(), SysConf.FILE_SEGMENTATION);

            List<String> picList = webUtils.getPicture(pictureList);
            blog.setPhotoList(picList);
        }
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
}