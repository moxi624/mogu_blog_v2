package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.commons.entity.Picture;
import com.moxi.mogublog.commons.entity.PictureSort;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.PictureMapper;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.PictureService;
import com.moxi.mogublog.xo.service.PictureSortService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.PictureVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 图片表 服务实现类
 *
 * @author 陌溪
 * @since 2018-09-04
 */
@Service
public class PictureServiceImpl extends SuperServiceImpl<PictureMapper, Picture> implements PictureService {

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private PictureSortService pictureSortService;

    @Resource
    private PictureFeignClient pictureFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public IPage<Picture> getPageList(PictureVO pictureVO) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(pictureVO.getKeyword()) && !StringUtils.isEmpty(pictureVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.PIC_NAME, pictureVO.getKeyword().trim());
        }

        Page<Picture> page = new Page<>();
        page.setCurrent(pictureVO.getCurrentPage());
        page.setSize(pictureVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.PICTURE_SORT_UID, pictureVO.getPictureSortUid());
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<Picture> pageList = pictureService.page(page, queryWrapper);
        List<Picture> pictureList = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        pictureList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
        });

        String pictureResult = null;
        Map<String, String> pictureMap = new HashMap<>();

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get(SysConf.UID).toString(), item.get(SysConf.URL).toString());
        });

        for (Picture item : pictureList) {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                item.setPictureUrl(pictureMap.get(item.getFileUid()));
            }
        }
        pageList.setRecords(pictureList);
        return pageList;
    }

    @Override
    public String addPicture(List<PictureVO> pictureVOList) {
        List<Picture> pictureList = new ArrayList<>();
        if (pictureVOList.size() > 0) {
            for (PictureVO pictureVO : pictureVOList) {
                Picture picture = new Picture();
                picture.setFileUid(pictureVO.getFileUid());
                picture.setPictureSortUid(pictureVO.getPictureSortUid());
                picture.setPicName(pictureVO.getPicName());
                picture.setStatus(EStatus.ENABLE);
                pictureList.add(picture);
            }
            pictureService.saveBatch(pictureList);
        } else {
            return ResultUtil.errorWithMessage(MessageConf.INSERT_FAIL);
        }
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editPicture(PictureVO pictureVO) {
        Picture picture = pictureService.getById(pictureVO.getUid());
        // 这里需要更新所有的博客，将图片替换成 裁剪的图片
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.FILE_UID, picture.getFileUid());
        List<Blog> blogList = blogService.list(queryWrapper);
        if (blogList.size() > 0) {
            blogList.forEach(item -> {
                item.setFileUid(pictureVO.getFileUid());
            });
            blogService.updateBatchById(blogList);

            Map<String, Object> map = new HashMap<>();
            map.put(SysConf.COMMAND, SysConf.EDIT_BATCH);

            //发送到RabbitMq
            rabbitTemplate.convertAndSend(SysConf.EXCHANGE_DIRECT, SysConf.MOGU_BLOG, map);
        }
        picture.setFileUid(pictureVO.getFileUid());
        picture.setPicName(pictureVO.getPicName());
        picture.setPictureSortUid(pictureVO.getPictureSortUid());
        picture.setUpdateTime(new Date());
        picture.updateById();
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchPicture(PictureVO pictureVO) {
        // 参数校验
        // 图片删除的时候，是携带多个id拼接而成的
        String uidStr = pictureVO.getUid();
        if (StringUtils.isEmpty(uidStr)) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = StringUtils.changeStringToString(pictureVO.getUid(), SysConf.FILE_SEGMENTATION);
        for (String item : uids) {
            Picture picture = pictureService.getById(item);
            picture.setStatus(EStatus.DISABLED);
            picture.setUpdateTime(new Date());
            picture.updateById();
        }
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String setPictureCover(PictureVO pictureVO) {
        PictureSort pictureSort = pictureSortService.getById(pictureVO.getPictureSortUid());
        if (pictureSort != null) {
            Picture picture = pictureService.getById(pictureVO.getUid());
            if (picture != null) {
                pictureSort.setFileUid(picture.getFileUid());
                picture.setUpdateTime(new Date());
                pictureSort.updateById();
            } else {
                return ResultUtil.errorWithMessage(MessageConf.THE_PICTURE_NOT_EXIST);
            }
        } else {
            return ResultUtil.errorWithMessage(MessageConf.THE_PICTURE_SORT_NOT_EXIST);
        }
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public Picture getTopOne() {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByAsc(SQLConf.CREATE_TIME);
        queryWrapper.last(SysConf.LIMIT_ONE);
        Picture picture = pictureService.getOne(queryWrapper);
        return picture;
    }
}
