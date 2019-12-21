package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.ResourceSort;
import com.moxi.mogublog.xo.service.ResourceSortService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源分类表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年10月19日21:36:02
 */
//@PreAuthorize("hasRole('Administrator')")
@Api(value = "资源分类RestApi", tags = {"ResourceSortRestApi"})
@RestController
@RequestMapping("/resourceSort")
public class ResourceSortRestApi {

    private static Logger log = LogManager.getLogger(AdminRestApi.class);
    @Autowired
    ResourceSortService resourceSortService;
    @Autowired
    PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取资源分类列表", notes = "获取资源分类列表", response = String.class)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<ResourceSort> queryWrapper = new QueryWrapper<ResourceSort>();
        if (StringUtils.isNotEmpty(keyword) && !StringUtils.isEmpty(keyword.trim())) {
            queryWrapper.like(SQLConf.SORT_NAME, keyword.trim());
        }

        Page<ResourceSort> page = new Page<ResourceSort>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<ResourceSort> pageList = resourceSortService.page(page, queryWrapper);
        List<ResourceSort> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + ",");
            }
        });
        String pictureResult = null;
        Map<String, String> pictureMap = new HashMap<String, String>();

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), ",");
        }
        List<Map<String, Object>> picList = WebUtils.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get("uid").toString(), item.get("url").toString());
        });

        for (ResourceSort item : list) {
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
        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加资源分类")
    @ApiOperation(value = "增加资源分类", notes = "增加资源分类", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request,
                      @ApiParam(name = "sortName", value = "资源分类名", required = false) @RequestParam(name = "sortName", required = false) String sortName,
                      @ApiParam(name = "content", value = "分类介绍", required = false) @RequestParam(name = "content", required = false) String content,
                      @ApiParam(name = "fileUid", value = "分类资源UID", required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {

        if (StringUtils.isEmpty(sortName)) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        ResourceSort resourceSort = new ResourceSort();
        resourceSort.setSortName(sortName);
        resourceSort.setContent(content);
        resourceSort.setFileUid(fileUid);
        resourceSort.setStatus(EStatus.ENABLE);
        resourceSort.insert();
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @OperationLogger(value = "编辑资源分类")
    @ApiOperation(value = "编辑资源分类", notes = "编辑资源分类", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request,
                       @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid,
                       @ApiParam(name = "sortName", value = "资源分类名", required = false) @RequestParam(name = "sortName", required = false) String sortName,
                       @ApiParam(name = "content", value = "分类介绍", required = false) @RequestParam(name = "content", required = false) String content,
                       @ApiParam(name = "fileUid", value = "分类资源UID", required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }

        ResourceSort resourceSort = resourceSortService.getById(uid);
        resourceSort.setSortName(sortName);
        resourceSort.setContent(content);
        resourceSort.setFileUid(fileUid);
        resourceSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
    }

    @OperationLogger(value = "删除资源分类")
    @ApiOperation(value = "删除资源分类", notes = "删除资源分类", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        ResourceSort resourceSort = resourceSortService.getById(uid);
        resourceSort.setStatus(EStatus.DISABLED);
        resourceSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }

    @OperationLogger(value = "置顶资源分类")
    @ApiOperation(value = "置顶分类", notes = "置顶分类", response = String.class)
    @PostMapping("/stick")
    public String stick(HttpServletRequest request,
                        @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        ResourceSort resourceSort = resourceSortService.getById(uid);

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
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        if (maxSort.getUid().equals(resourceSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "该分类已经在顶端");
        }

        Integer sortCount = maxSort.getSort() + 1;

        resourceSort.setSort(sortCount);

        resourceSort.updateById();

        return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
    }
}

