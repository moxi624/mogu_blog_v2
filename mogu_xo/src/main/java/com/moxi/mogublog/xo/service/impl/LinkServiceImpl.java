package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Link;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.CheckUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.LinkMapper;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.utils.RabbitMqUtil;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.LinkVO;
import com.moxi.mougblog.base.enums.ELinkStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 友链表 服务实现类
 *
 * @author 陌溪
 * @date 2018-09-08
 */
@Service
@Slf4j
public class LinkServiceImpl extends SuperServiceImpl<LinkMapper, Link> implements LinkService {

    @Resource
    private LinkMapper linkMapper;
    @Autowired
    private LinkService linkService;
    @Resource
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private RabbitMqUtil rabbitMqUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Link> getListByPageSize(Integer pageSize) {
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        Page<Link> page = new Page<>();
        page.setCurrent(1);
        page.setSize(pageSize);
        queryWrapper.eq(BaseSQLConf.LINK_STATUS, ELinkStatus.PUBLISH);
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(BaseSQLConf.SORT);
        IPage<Link> pageList = linkMapper.selectPage(page, queryWrapper);
        return pageList.getRecords();
    }

    @Override
    public IPage<Link> getPageList(LinkVO linkVO) {
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(linkVO.getKeyword()) && !StringUtils.isEmpty(linkVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.TITLE, linkVO.getKeyword().trim());
        }
        if (linkVO.getLinkStatus() != null) {
            queryWrapper.eq(SQLConf.LINK_STATUS, linkVO.getLinkStatus());
        }
        if(StringUtils.isNotEmpty(linkVO.getOrderByAscColumn())) {
            String column = StringUtils.underLine(new StringBuffer(linkVO.getOrderByAscColumn())).toString();
            queryWrapper.orderByAsc(column);
        }else if(StringUtils.isNotEmpty(linkVO.getOrderByDescColumn())) {
            String column = StringUtils.underLine(new StringBuffer(linkVO.getOrderByDescColumn())).toString();
            queryWrapper.orderByDesc(column);
        } else {
            queryWrapper.orderByDesc(SQLConf.SORT);
        }
        Page<Link> page = new Page<>();
        page.setCurrent(linkVO.getCurrentPage());
        page.setSize(linkVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        IPage<Link> pageList = linkService.page(page, queryWrapper);
        List<Link> linkList = pageList.getRecords();
        final StringBuffer fileUids = new StringBuffer();
        // 给友情链接添加图片
        linkList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
        });
        String pictureList = null;
        Map<String, String> pictureMap = new HashMap<>();
        if (fileUids != null) {
            pictureList = pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureList);
        picList.forEach(item -> {
            pictureMap.put(item.get(SysConf.UID).toString(), item.get(SysConf.URL).toString());
        });
        for (Link item : linkList) {
            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), Constants.SYMBOL_COMMA);
                List<String> pictureListTemp = new ArrayList<>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        pageList.setRecords(linkList);
        return pageList;
    }

    @Override
    public String addLink(LinkVO linkVO) {
        Link link = new Link();
        link.setTitle(linkVO.getTitle());
        link.setSummary(linkVO.getSummary());
        link.setUrl(linkVO.getUrl());
        link.setClickCount(0);
        link.setLinkStatus(linkVO.getLinkStatus());
        link.setSort(linkVO.getSort());
        link.setEmail(linkVO.getEmail());
        link.setFileUid(linkVO.getFileUid());
        link.setStatus(EStatus.ENABLE);
        link.setUpdateTime(new Date());
        link.insert();

        // 友链从申请状态到发布状态，需要发送邮件到站长邮箱
        if(StringUtils.isNotEmpty(link.getEmail()) && CheckUtils.checkEmail(link.getEmail())) {
            log.info("发送友链申请通过的邮件通知");
            String linkApplyText =  "<a href=\" " + link.getUrl() + "\">" + link.getTitle() + "</a> 站长，您申请的友链已经成功上架~";
            rabbitMqUtil.sendSimpleEmail(link.getEmail(), linkApplyText);
        }

        // 删除Redis中的BLOG_LINK
        deleteRedisBlogLinkList();

        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editLink(LinkVO linkVO) {
        Link link = linkService.getById(linkVO.getUid());
        Integer linkStatus = link.getLinkStatus();
        link.setTitle(linkVO.getTitle());
        link.setSummary(linkVO.getSummary());
        link.setLinkStatus(linkVO.getLinkStatus());
        link.setUrl(linkVO.getUrl());
        link.setSort(linkVO.getSort());
        link.setEmail(linkVO.getEmail());
        link.setFileUid(linkVO.getFileUid());
        link.setUpdateTime(new Date());
        link.updateById();

        // 友链从申请状态到发布状态，需要发送邮件到站长邮箱
        if(StringUtils.isNotEmpty(link.getEmail()) && CheckUtils.checkEmail(link.getEmail())) {
            if(ELinkStatus.APPLY.equals(linkStatus) && ELinkStatus.PUBLISH.equals(linkVO.getLinkStatus())) {
                log.info("发送友链申请通过的邮件通知");
                String linkApplyText =  "<a href=\" " + link.getUrl() + "\">" + link.getTitle() + "</a> 站长，您申请的友链已经成功上架~";
                rabbitMqUtil.sendSimpleEmail(link.getEmail(), linkApplyText);
            }
        }

        // 删除Redis中的BLOG_LINK
        deleteRedisBlogLinkList();

        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteLink(LinkVO linkVO) {
        Link link = linkService.getById(linkVO.getUid());
        link.setStatus(EStatus.DISABLED);
        link.setUpdateTime(new Date());
        link.updateById();

        // 删除Redis中的BLOG_LINK
        deleteRedisBlogLinkList();

        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String stickLink(LinkVO linkVO) {
        Link link = linkService.getById(linkVO.getUid());
        //查找出最大的那一个
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.SORT);
        Page<Link> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<Link> pageList = linkService.page(page, queryWrapper);
        List<Link> list = pageList.getRecords();
        Link maxSort = list.get(0);
        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        if (maxSort.getUid().equals(link.getUid())) {
            return ResultUtil.errorWithMessage(MessageConf.OPERATION_FAIL);
        }
        Integer sortCount = maxSort.getSort() + 1;
        link.setSort(sortCount);
        link.setUpdateTime(new Date());
        link.updateById();
        // 删除Redis中的BLOG_LINK
        deleteRedisBlogLinkList();
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public String addLinkCount(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        Link link = linkService.getById(uid);
        if (link != null) {
            int count = link.getClickCount() + 1;
            link.setClickCount(count);
            link.updateById();
        } else {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    /**
     * 删除Redis中的友链列表
     */
    private void deleteRedisBlogLinkList() {
        // 删除Redis中的BLOG_LINK
        Set<String> keys = redisUtil.keys(RedisConf.BLOG_LINK + Constants.SYMBOL_COLON + "*");
        redisUtil.delete(keys);
    }
}
