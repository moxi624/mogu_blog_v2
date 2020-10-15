package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.SysDictData;
import com.moxi.mogublog.commons.entity.SysDictType;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.SysDictTypeMapper;
import com.moxi.mogublog.xo.service.SysDictDataService;
import com.moxi.mogublog.xo.service.SysDictTypeService;
import com.moxi.mogublog.xo.vo.SysDictTypeVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * <p>
 * 字典类型 服务实现类
 * </p>
 *
 * @author 陌溪
 * @since 2020年2月15日21:09:15
 */
@Service
public class SysDictTypeServiceImpl extends SuperServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Autowired
    SysDictDataService sysDictDataService;

    @Autowired
    SysDictTypeService sysDictTypeService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public IPage<SysDictType> getPageList(SysDictTypeVO sysDictTypeVO) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();

        // 字典名称
        if (StringUtils.isNotEmpty(sysDictTypeVO.getDictName()) && !StringUtils.isEmpty(sysDictTypeVO.getDictName().trim())) {
            queryWrapper.like(SQLConf.DICT_NAME, sysDictTypeVO.getDictName().trim());
        }

        // 字典类型
        if (StringUtils.isNotEmpty(sysDictTypeVO.getDictType()) && !StringUtils.isEmpty(sysDictTypeVO.getDictType().trim())) {
            queryWrapper.like(SQLConf.DICT_TYPE, sysDictTypeVO.getDictType().trim());
        }

        Page<SysDictType> page = new Page<>();
        page.setCurrent(sysDictTypeVO.getCurrentPage());
        page.setSize(sysDictTypeVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT, SQLConf.CREATE_TIME);
        IPage<SysDictType> pageList = sysDictTypeService.page(page, queryWrapper);
        return pageList;
    }

    @Override
    public String addSysDictType(SysDictTypeVO sysDictTypeVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        // 判断添加的字典类型是否存在
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.DICT_TYPE, sysDictTypeVO.getDictType());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last(SysConf.LIMIT_ONE);
        SysDictType temp = sysDictTypeService.getOne(queryWrapper);
        if (temp != null) {
            return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
        }
        SysDictType sysDictType = new SysDictType();
        sysDictType.setDictName(sysDictTypeVO.getDictName());
        sysDictType.setDictType(sysDictTypeVO.getDictType());
        sysDictType.setRemark(sysDictTypeVO.getRemark());
        sysDictType.setIsPublish(sysDictTypeVO.getIsPublish());
        sysDictType.setSort(sysDictTypeVO.getSort());
        sysDictType.setCreateByUid(request.getAttribute(SysConf.ADMIN_UID).toString());
        sysDictType.setUpdateByUid(request.getAttribute(SysConf.ADMIN_UID).toString());
        sysDictType.insert();
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editSysDictType(SysDictTypeVO sysDictTypeVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        SysDictType sysDictType = sysDictTypeService.getById(sysDictTypeVO.getUid());

        // 判断编辑的字典类型是否存在
        if (!sysDictType.getDictType().equals(sysDictTypeVO.getDictType())) {
            QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.DICT_TYPE, sysDictTypeVO.getDictType());
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.last(SysConf.LIMIT_ONE);
            SysDictType temp = sysDictTypeService.getOne(queryWrapper);
            if (temp != null) {
                return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
            }
        }

        sysDictType.setDictName(sysDictTypeVO.getDictName());
        sysDictType.setDictType(sysDictTypeVO.getDictType());
        sysDictType.setRemark(sysDictTypeVO.getRemark());
        sysDictType.setIsPublish(sysDictTypeVO.getIsPublish());
        sysDictType.setSort(sysDictTypeVO.getSort());
        sysDictType.setUpdateByUid(request.getAttribute(SysConf.ADMIN_UID).toString());
        sysDictType.setUpdateTime(new Date());
        sysDictType.updateById();

        // 获取Redis中特定前缀
        Set<String> keys = redisUtil.keys(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + "*");
        redisUtil.delete(keys);
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchSysDictType(List<SysDictTypeVO> sysDictTypeVOList) {
        HttpServletRequest request = RequestHolder.getRequest();
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        if (sysDictTypeVOList.size() <= 0) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        sysDictTypeVOList.forEach(item -> {
            uids.add(item.getUid());
        });

        // 判断要删除的分类，是否有博客
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.in(SQLConf.DICT_TYPE_UID, uids);
        Integer count = sysDictDataService.count(queryWrapper);
        if (count > 0) {
            return ResultUtil.errorWithMessage(MessageConf.DICT_DATA_UNDER_THIS_SORT);
        }
        Collection<SysDictType> sysDictTypeList = sysDictTypeService.listByIds(uids);
        sysDictTypeList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
            item.setUpdateByUid(adminUid);
        });

        Boolean save = sysDictTypeService.updateBatchById(sysDictTypeList);

        // 获取Redis中特定前缀
        Set<String> keys = redisUtil.keys(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + "*");
        redisUtil.delete(keys);
        if (save) {
            return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.errorWithMessage(MessageConf.DELETE_FAIL);
        }
    }
}
