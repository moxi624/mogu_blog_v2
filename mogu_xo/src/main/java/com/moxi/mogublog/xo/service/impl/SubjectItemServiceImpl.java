package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Blog;
import com.moxi.mogublog.commons.entity.SubjectItem;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.mapper.SubjectItemMapper;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.SubjectItemService;
import com.moxi.mogublog.xo.vo.SubjectItemVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseMessageConf;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 专题item 服务实现类
 * </p>
 *
 * @author 陌溪
 * @since 2020年8月23日08:08:18
 */
@Service
public class SubjectItemServiceImpl extends SuperServiceImpl<SubjectItemMapper, SubjectItem> implements SubjectItemService {

    @Resource
    SubjectItemService subjectItemService;

    @Resource
    BlogService blogService;

    @Override
    public IPage<SubjectItem> getPageList(SubjectItemVO subjectItemVO) {
        QueryWrapper<SubjectItem> queryWrapper = new QueryWrapper<>();
        Page<SubjectItem> page = new Page<>();
        if (StringUtils.isNotEmpty(subjectItemVO.getSubjectUid())) {
            queryWrapper.eq(BaseSQLConf.SUBJECT_UID, subjectItemVO.getSubjectUid());
        }
        page.setCurrent(subjectItemVO.getCurrentPage());
        page.setSize(subjectItemVO.getPageSize());
        queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(BaseSQLConf.SORT);
        IPage<SubjectItem> pageList = subjectItemService.page(page, queryWrapper);
        List<SubjectItem> subjectItemList = pageList.getRecords();
        List<String> blogUidList = new ArrayList<>();
        subjectItemList.forEach(item -> {
            blogUidList.add(item.getBlogUid());
        });
        Collection<Blog> blogCollection = null;
        if (blogUidList.size() > 0) {
            blogCollection = blogService.listByIds(blogUidList);
            if (blogCollection.size() > 0) {
                List<Blog> blogTempList = new ArrayList<>(blogCollection);
                List<Blog> blogList = blogService.setTagAndSortAndPictureByBlogList(blogTempList);
                Map<String, Blog> blogMap = new HashMap<>();
                blogList.forEach(item -> {
                    blogMap.put(item.getUid(), item);
                });
                subjectItemList.forEach(item -> {
                    item.setBlog(blogMap.get(item.getBlogUid()));
                });
                pageList.setRecords(subjectItemList);
            }
        }

        return pageList;
    }

    @Override
    public String addSubjectItemList(List<SubjectItemVO> subjectItemVOList) {
        List<String> blogUidList = new ArrayList<>();
        String subjectUid = "";
        for (SubjectItemVO subjectItemVO : subjectItemVOList) {
            blogUidList.add(subjectItemVO.getBlogUid());
            if (StringUtils.isEmpty(subjectUid) && StringUtils.isNotEmpty(subjectItemVO.getSubjectUid())) {
                subjectUid = subjectItemVO.getSubjectUid();
            }
        }
        // 查询SubjectItem中是否包含重复的博客
        QueryWrapper<SubjectItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SUBJECT_UID, subjectUid);
        queryWrapper.in(SQLConf.BLOG_UID, blogUidList);
        List<SubjectItem> repeatSubjectItemList = subjectItemService.list(queryWrapper);
        // 找出重复的博客UID
        List<String> repeatBlogList = new ArrayList<>();
        repeatSubjectItemList.forEach(item -> {
            repeatBlogList.add(item.getBlogUid());
        });

        List<SubjectItem> subjectItemList = new ArrayList<>();
        for (SubjectItemVO subjectItemVO : subjectItemVOList) {
            if (StringUtils.isEmpty(subjectItemVO.getSubjectUid()) || StringUtils.isEmpty(subjectItemVO.getBlogUid())) {
                return ResultUtil.result(BaseSysConf.ERROR, BaseMessageConf.PARAM_INCORRECT);
            }
            // 判断是否重复添加
            if (repeatBlogList.contains(subjectItemVO.getBlogUid())) {
                continue;
            } else {
                SubjectItem subjectItem = new SubjectItem();
                subjectItem.setSubjectUid(subjectItemVO.getSubjectUid());
                subjectItem.setBlogUid(subjectItemVO.getBlogUid());
                subjectItem.setStatus(EStatus.ENABLE);
                subjectItemList.add(subjectItem);
            }
        }

        if (subjectItemList.size() <= 0) {
            if (repeatBlogList.size() == 0) {
                return ResultUtil.result(BaseSysConf.ERROR, BaseMessageConf.INSERT_FAIL);
            } else {
                return ResultUtil.result(BaseSysConf.ERROR, BaseMessageConf.INSERT_FAIL + "，已跳过" + repeatBlogList.size() + "个重复数据");
            }
        } else {
            subjectItemService.saveBatch(subjectItemList);
            if (repeatBlogList.size() == 0) {
                return ResultUtil.result(BaseSysConf.SUCCESS, BaseMessageConf.INSERT_SUCCESS);
            } else {
                return ResultUtil.result(BaseSysConf.SUCCESS, BaseMessageConf.INSERT_SUCCESS + "，已跳过" + repeatBlogList.size() + "个重复数据，成功插入" + (subjectItemVOList.size() - repeatBlogList.size()) + "条数据");
            }
        }
    }

    @Override
    public String editSubjectItemList(List<SubjectItemVO> subjectItemVOList) {
        List<String> subjectItemUidList = new ArrayList<>();
        subjectItemVOList.forEach(item -> {
            subjectItemUidList.add(item.getUid());
        });
        Collection<SubjectItem> subjectItemCollection = null;
        if (subjectItemUidList.size() > 0) {
            subjectItemCollection = subjectItemService.listByIds(subjectItemUidList);
            if (subjectItemCollection.size() > 0) {
                HashMap<String, SubjectItemVO> subjectItemVOHashMap = new HashMap<>();
                subjectItemVOList.forEach(item -> {
                    subjectItemVOHashMap.put(item.getUid(), item);
                });
                // 修改排序字段
                List<SubjectItem> subjectItemList = new ArrayList<>();
                subjectItemCollection.forEach(item -> {
                    SubjectItemVO subjectItemVO = subjectItemVOHashMap.get(item.getUid());
                    item.setSubjectUid(subjectItemVO.getSubjectUid());
                    item.setBlogUid(subjectItemVO.getBlogUid());
                    item.setStatus(EStatus.ENABLE);
                    item.setSort(subjectItemVO.getSort());
                    item.setUpdateTime(new Date());
                    subjectItemList.add(item);
                });
                subjectItemService.updateBatchById(subjectItemList);
            }
        }
        return ResultUtil.result(BaseSysConf.SUCCESS, BaseMessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchSubjectItem(List<SubjectItemVO> subjectItemVOList) {
        if (subjectItemVOList.size() <= 0) {
            return ResultUtil.result(BaseSysConf.ERROR, BaseMessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        subjectItemVOList.forEach(item -> {
            uids.add(item.getUid());
        });
        subjectItemService.removeByIds(uids);
        return ResultUtil.result(BaseSysConf.SUCCESS, BaseMessageConf.DELETE_SUCCESS);
    }
}
