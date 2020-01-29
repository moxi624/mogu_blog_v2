package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.PictureSort;
import com.moxi.mogublog.xo.service.PictureSortService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片分类表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 22018年9月17日16:37:13
 */
//@PreAuthorize("hasRole('Administrator')")
@Api(value = "图片分类RestApi", tags = {"PictureSortRestApi"})
@RestController
@RequestMapping("/pictureSort")
@Slf4j
public class PictureSortRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    PictureSortService pictureSortService;
    @Autowired
    PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取图片分类列表", notes = "获取图片分类列表", response = String.class)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<PictureSort> queryWrapper = new QueryWrapper<PictureSort>();
        if (StringUtils.isNotEmpty(keyword) && !StringUtils.isEmpty(keyword.trim())) {
            queryWrapper.like(SQLConf.NAME, keyword.trim());
        }

        Page<PictureSort> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<PictureSort> pageList = pictureSortService.page(page, queryWrapper);
        List<PictureSort> list = pageList.getRecords();

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
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get("uid").toString(), item.get("url").toString());
        });

        for (PictureSort item : list) {
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
        pageList.setRecords(list);
        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加图片分类")
    @ApiOperation(value = "增加图片分类", notes = "增加图片分类", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request,
                      @ApiParam(name = "name", value = "图片分类名", required = false) @RequestParam(name = "name", required = false) String name,
                      @ApiParam(name = "parentUid", value = "图片分类父UID", required = false) @RequestParam(name = "parentUid", required = false) String parentUid,
                      @ApiParam(name = "fileUid", value = "分类图片UID", required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {

        if (StringUtils.isEmpty(name)) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        PictureSort pictureSort = new PictureSort();
        pictureSort.setName(name);
        pictureSort.setParentUid(parentUid);
        pictureSort.setFileUid(fileUid);
        pictureSort.setStatus(EStatus.ENABLE);
        pictureSort.insert();
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @OperationLogger(value = "编辑图片分类")
    @ApiOperation(value = "编辑图片分类", notes = "编辑图片分类", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request,
                       @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid,
                       @ApiParam(name = "name", value = "图片分类名", required = false) @RequestParam(name = "name", required = false) String name,
                       @ApiParam(name = "parentUid", value = "图片分类父UID", required = false) @RequestParam(name = "parentUid", required = false) String parentUid,
                       @ApiParam(name = "fileUid", value = "分类图片UID", required = false) @RequestParam(name = "fileUid", required = false) String fileUid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }

        PictureSort pictureSort = pictureSortService.getById(uid);
        pictureSort.setName(name);
        pictureSort.setParentUid(parentUid);
        pictureSort.setFileUid(fileUid);
        pictureSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
    }

    @OperationLogger(value = "删除图片分类")
    @ApiOperation(value = "删除图片分类", notes = "删除图片分类", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        PictureSort pictureSort = pictureSortService.getById(uid);
        pictureSort.setStatus(EStatus.DISABLED);
        pictureSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }

    @OperationLogger(value = "置顶分类")
    @ApiOperation(value = "置顶分类", notes = "置顶分类", response = String.class)
    @PostMapping("/stick")
    public String stick(HttpServletRequest request,
                        @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        PictureSort pictureSort = pictureSortService.getById(uid);

        //查找出最大的那一个
        QueryWrapper<PictureSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.SORT);
        Page<PictureSort> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<PictureSort> pageList = pictureSortService.page(page, queryWrapper);
        List<PictureSort> list = pageList.getRecords();
        PictureSort maxSort = list.get(0);


        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        if (maxSort.getUid().equals(pictureSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "该分类已经在顶端");
        }

        Integer sortCount = maxSort.getSort() + 1;

        pictureSort.setSort(sortCount);

        pictureSort.updateById();

        return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
    }

    @OperationLogger(value = "通过Uid获取分类")
    @ApiOperation(value = "通过Uid获取分类", notes = "通过Uid获取分类", response = String.class)
    @PostMapping("/getPictureSortByUid")
    public String getPictureSortByUid(HttpServletRequest request,
                                      @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        PictureSort pictureSort = pictureSortService.getById(uid);
        return ResultUtil.result(SysConf.SUCCESS, pictureSort);
    }
}

