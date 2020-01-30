package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.ResourceSort;
import com.moxi.mogublog.xo.service.ResourceSortService;
import com.moxi.mogublog.xo.vo.ResourceSortVO;
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

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 资源分类表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2020年1月9日19:23:28
 */
@Api(value = "资源分类RestApi", tags = {"ResourceSortRestApi"})
@RestController
@RequestMapping("/resourceSort")
@Slf4j
public class ResourceSortRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    ResourceSortService resourceSortService;
    @Autowired
    PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取资源分类列表", notes = "获取资源分类列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        QueryWrapper<ResourceSort> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(resourceSortVO.getKeyword()) && !StringUtils.isEmpty(resourceSortVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.SORT_NAME, resourceSortVO.getKeyword().trim());
        }

        Page<ResourceSort> page = new Page<>();
        page.setCurrent(resourceSortVO.getCurrentPage());
        page.setSize(resourceSortVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<ResourceSort> pageList = resourceSortService.page(page, queryWrapper);
        List<ResourceSort> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
        });
        String pictureResult = null;
        Map<String, String> pictureMap = new HashMap<>();

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get("uid").toString(), item.get("url").toString());
        });

        for (ResourceSort item : list) {
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
        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加资源分类")
    @ApiOperation(value = "增加资源分类", notes = "增加资源分类", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        /**
         * 判断需要增加的分类是否存在
         */
        QueryWrapper<ResourceSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, resourceSortVO.getSortName());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        ResourceSort tempSort = resourceSortService.getOne(queryWrapper);
        if (tempSort != null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
        }

        ResourceSort resourceSort = new ResourceSort();
        resourceSort.setSortName(resourceSortVO.getSortName());
        resourceSort.setContent(resourceSortVO.getContent());
        resourceSort.setFileUid(resourceSortVO.getFileUid());
        resourceSort.setStatus(EStatus.ENABLE);
        resourceSort.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @OperationLogger(value = "编辑资源分类")
    @ApiOperation(value = "编辑资源分类", notes = "编辑资源分类", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        ResourceSort resourceSort = resourceSortService.getById(resourceSortVO.getUid());

        /**
         * 判断需要编辑的分类是否存在
         */
        if (!resourceSort.getSortName().equals(resourceSortVO.getSortName())) {
            QueryWrapper<ResourceSort> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.SORT_NAME, resourceSortVO.getSortName());
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            ResourceSort tempSort = resourceSortService.getOne(queryWrapper);
            if (tempSort != null) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
            }
        }

        resourceSort.setSortName(resourceSortVO.getSortName());
        resourceSort.setContent(resourceSortVO.getContent());
        resourceSort.setFileUid(resourceSortVO.getFileUid());
        resourceSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @OperationLogger(value = "批量删除资源分类")
    @ApiOperation(value = "批量删除资源分类", notes = "批量删除资源分类", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(@Validated({Delete.class}) @RequestBody List<ResourceSortVO> resourceSortVOList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        if (resourceSortVOList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        resourceSortVOList.forEach(item -> {
            uids.add(item.getUid());
        });
        Collection<ResourceSort> tagList = resourceSortService.listByIds(uids);

        tagList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
        });

        Boolean save = resourceSortService.updateBatchById(tagList);

        if (save) {
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DELETE_FAIL);
        }
    }

    @OperationLogger(value = "置顶资源分类")
    @ApiOperation(value = "置顶分类", notes = "置顶分类", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody ResourceSortVO resourceSortVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        ResourceSort resourceSort = resourceSortService.getById(resourceSortVO.getUid());

        //查找出最大的那一个
        QueryWrapper<ResourceSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.SORT);
        Page<ResourceSort> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<ResourceSort> pageList = resourceSortService.page(page, queryWrapper);
        List<ResourceSort> list = pageList.getRecords();
        ResourceSort maxSort = list.get(0);

        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        if (maxSort.getUid().equals(resourceSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }

        Integer sortCount = maxSort.getSort() + 1;

        resourceSort.setSort(sortCount);

        resourceSort.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}

