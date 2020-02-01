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
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.Picture;
import com.moxi.mogublog.xo.entity.PictureSort;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.PictureService;
import com.moxi.mogublog.xo.service.PictureSortService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月17日16:21:43
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    PictureService pictureService;

    @Autowired
    BlogService blogService;

    @Autowired
    PictureSortService pictureSortService;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${data.image.url}")
    private String IMG_HOST;

    @ApiOperation(value = "获取图片列表", notes = "获取图片列表", response = String.class)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                          @ApiParam(name = "pictureSortUid", value = "图片分类UID", required = true) @RequestParam(name = "pictureSortUid", required = true) String pictureSortUid,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "14") Long pageSize) {

        if (StringUtils.isEmpty(pictureSortUid)) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<Picture>();
        if (StringUtils.isNotEmpty(keyword) && !StringUtils.isEmpty(keyword.trim())) {
            queryWrapper.like(SQLConf.PIC_NAME, keyword.trim());
        }

        Page<Picture> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.PICTURE_SORT_UID, pictureSortUid);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Picture> pageList = pictureService.page(page, queryWrapper);
        List<Picture> pictureList = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        pictureList.forEach(item -> {
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

        for (Picture item : pictureList) {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                log.info(pictureMap.get(item.getFileUid()));
                item.setPictureUrl(pictureMap.get(item.getFileUid()));
            }
        }

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加图片")
    @ApiOperation(value = "增加图片", notes = "增加图片", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request,
                      @ApiParam(name = "fileUids", value = "图片UIDs", required = false) @RequestParam(name = "fileUids", required = false) String fileUids,
                      @ApiParam(name = "pictureSortUid", value = "图片分类UID", required = false) @RequestParam(name = "pictureSortUid", required = false) String pictureSortUid) {

        if (StringUtils.isEmpty(fileUids) || StringUtils.isEmpty(pictureSortUid)) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        List<String> list = StringUtils.changeStringToString(fileUids, ",");
        List<Picture> pictureList = new ArrayList<>();
        if (list.size() > 0) {
            for (String fileUid : list) {
                Picture picture = new Picture();
                picture.setFileUid(fileUid);
                picture.setPictureSortUid(pictureSortUid);
                picture.setStatus(EStatus.ENABLE);
                pictureList.add(picture);
            }
            pictureService.saveBatch(pictureList);
        }
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @OperationLogger(value = "编辑图片")
    @ApiOperation(value = "编辑图片", notes = "编辑图片", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request,
                       @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid,
                       @ApiParam(name = "fileUid", value = "图片UID", required = false) @RequestParam(name = "fileUid", required = false) String fileUid,
                       @ApiParam(name = "picName", value = "图片名称", required = false) @RequestParam(name = "picName", required = false) String picName,
                       @ApiParam(name = "pictureSortUid", value = "图片分类UID", required = false) @RequestParam(name = "pictureSortUid", required = false) String pictureSortUid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        Picture picture = pictureService.getById(uid);

        // 这里需要更新所有的博客，将图片替换成 裁剪的图片
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.FILE_UID, picture.getFileUid());
        List<Blog> blogList = blogService.list(queryWrapper);
        if(blogList.size() > 0) {
            blogList.forEach(item -> {
                item.setFileUid(fileUid);
            });
            blogService.updateBatchById(blogList);

            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.EDIT_BATCH);

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);
        }

        picture.setFileUid(fileUid);
        picture.setPicName(picName);
        picture.setPictureSortUid(pictureSortUid);
        picture.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
    }

    @OperationLogger(value = "删除图片")
    @ApiOperation(value = "删除图片", notes = "删除图片", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        List<String> uids = StringUtils.changeStringToString(uid, ",");
        for (String item : uids) {
            Picture picture = pictureService.getById(item);
            picture.setStatus(EStatus.DISABLED);
            picture.updateById();
        }
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }

    @OperationLogger(value = "通过图片Uid将图片设为封面")
    @ApiOperation(value = "通过图片Uid将图片设为封面", notes = "通过图片Uid将图片设为封面", response = String.class)
    @PostMapping("/setCover")
    public String setCover(HttpServletRequest request,
                           @ApiParam(name = "pictureUid", value = "图片UID", required = true) @RequestParam(name = "pictureUid", required = true) String pictureUid,
                           @ApiParam(name = "pictureSortUid", value = "图片分类UID", required = true) @RequestParam(name = "pictureSortUid", required = true) String pictureSortUid) {

        if (StringUtils.isEmpty(pictureUid) || StringUtils.isEmpty(pictureSortUid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }

        PictureSort pictureSort = pictureSortService.getById(pictureSortUid);

        if (pictureSort != null) {

            Picture picture = pictureService.getById(pictureUid);

            if (picture != null) {
                pictureSort.setFileUid(picture.getFileUid());
                pictureSort.updateById();
            } else {
                return ResultUtil.result(SysConf.ERROR, "找不到该图片");
            }


        } else {
            return ResultUtil.result(SysConf.ERROR, "找不到该图片分类");
        }

        return ResultUtil.result(SysConf.SUCCESS, "设置成功");
    }
}

