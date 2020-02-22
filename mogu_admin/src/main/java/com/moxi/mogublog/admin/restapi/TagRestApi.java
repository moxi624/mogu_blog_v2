package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mogublog.xo.vo.TagVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 标签表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
@Api(value = "标签RestApi", tags = {"TagRestApi"})
@RestController
@RequestMapping("/tag")
@Slf4j
public class TagRestApi {

    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;

    @ApiOperation(value = "获取标签列表", notes = "获取标签列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(tagVO.getKeyword()) && !StringUtils.isEmpty(tagVO.getKeyword())) {
            queryWrapper.like(SQLConf.CONTENT, tagVO.getKeyword().trim());
        }

        Page<Tag> page = new Page<>();
        page.setCurrent(tagVO.getCurrentPage());
        page.setSize(tagVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<Tag> pageList = tagService.page(page, queryWrapper);
        log.info("获取标签列表");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加标签")
    @ApiOperation(value = "增加标签", notes = "增加标签", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.CONTENT, tagVO.getContent());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Tag tempTag = tagService.getOne(queryWrapper);
        if (tempTag != null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
        }
        Tag tag = new Tag();
        tag.setContent(tagVO.getContent());
        tag.setClickCount(0);
        tag.setStatus(EStatus.ENABLE);
        tag.setSort(tagVO.getSort());
        tag.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @OperationLogger(value = "编辑标签")
    @ApiOperation(value = "编辑标签", notes = "编辑标签", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Tag tag = tagService.getById(tagVO.getUid());

        if (tag != null && !tag.getContent().equals(tagVO.getContent())) {
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.CONTENT, tagVO.getContent());
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            Tag tempTag = tagService.getOne(queryWrapper);
            if (tempTag != null) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
            }
        }

        tag.setContent(tagVO.getContent());
        tag.setStatus(EStatus.ENABLE);
        tag.setSort(tagVO.getSort());
        tag.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @OperationLogger(value = "批量删除标签")
    @ApiOperation(value = "批量删除标签", notes = "批量删除标签", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(@Validated({Delete.class}) @RequestBody List<TagVO> tagVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        if (tagVoList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        tagVoList.forEach(item -> {
            uids.add(item.getUid());
        });
        Collection<Tag> tagList = tagService.listByIds(uids);

        tagList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
        });

        Boolean save = tagService.updateBatchById(tagList);

        if (save) {
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DELETE_FAIL);
        }
    }

    @OperationLogger(value = "置顶标签")
    @ApiOperation(value = "置顶标签", notes = "置顶标签", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody TagVO tagVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        Tag tag = tagService.getById(tagVO.getUid());

        //查找出最大的那一个
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.SORT);
        Page<Tag> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<Tag> pageList = tagService.page(page, queryWrapper);
        List<Tag> list = pageList.getRecords();
        Tag maxTag = list.get(0);

        if (StringUtils.isEmpty(maxTag.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        if (maxTag.getUid().equals(tag.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }

        Integer sortCount = maxTag.getSort() + 1;

        tag.setSort(sortCount);

        tag.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_FAIL);
    }

    @OperationLogger(value = "通过点击量排序标签")
    @ApiOperation(value = "通过点击量排序标签", notes = "通过点击量排序标签", response = String.class)
    @PostMapping("/tagSortByClickCount")
    public String tagSortByClickCount() {

        QueryWrapper<Tag> queryWrapper = new QueryWrapper();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        // 按点击从高到低排序
        queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);
        List<Tag> tagList = tagService.list(queryWrapper);
        // 设置初始化最大的sort值
        Integer maxSort = tagList.size();
        for (Tag item : tagList) {
            item.setSort(item.getClickCount());
            item.updateById();
        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 通过引用量排序标签
     * 引用量就是所有的文章中，有多少使用了该标签，如果使用的越多，该标签的引用量越大，那么排名越靠前
     *
     * @return
     */
    @OperationLogger(value = "通过引用量排序标签")
    @ApiOperation(value = "通过引用量排序标签", notes = "通过引用量排序标签", response = String.class)
    @PostMapping("/tagSortByCite")
    public String tagSortByCite() {

        // 定义Map   key：tagUid,  value: 引用量
        Map<String, Integer> map = new HashMap<>();

        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<Tag> tagList = tagService.list(tagQueryWrapper);
        // 初始化所有标签的引用量
        tagList.forEach(item -> {
            map.put(item.getUid(), 0);
        });

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        // 过滤content字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        List<Blog> blogList = blogService.list(queryWrapper);

        blogList.forEach(item -> {
            String tagUids = item.getTagUid();
            List<String> tagUidList = StringUtils.changeStringToString(tagUids, SysConf.FILE_SEGMENTATION);
            for (String tagUid : tagUidList) {
                if (map.get(tagUid) != null) {
                    Integer count = map.get(tagUid) + 1;
                    map.put(tagUid, count);
                } else {
                    map.put(tagUid, 0);
                }
            }
        });

        tagList.forEach(item -> {
            item.setSort(map.get(item.getUid()));
            item.updateById();
        });

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}

