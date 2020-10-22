package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.SysDictData;
import com.moxi.mogublog.commons.entity.SysDictType;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.SysDictDataMapper;
import com.moxi.mogublog.xo.service.SysDictDataService;
import com.moxi.mogublog.xo.service.SysDictTypeService;
import com.moxi.mogublog.xo.vo.SysDictDataVO;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 字典数据 服务实现类
 *
 * @author 陌溪
 * @date 2020年2月15日21:09:15
 */
@Service
public class SysDictDataServiceImpl extends SuperServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Autowired
    private SysDictDataService sysDictDataService;
    @Autowired
    private SysDictTypeService sysDictTypeService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage<SysDictData> getPageList(SysDictDataVO sysDictDataVO) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        // 字典类型UID
        if (StringUtils.isNotEmpty(sysDictDataVO.getDictTypeUid())) {
            queryWrapper.eq(SQLConf.DICT_TYPE_UID, sysDictDataVO.getDictTypeUid());
        }
        // 字典标签
        if (StringUtils.isNotEmpty(sysDictDataVO.getDictLabel()) && !StringUtils.isEmpty(sysDictDataVO.getDictLabel().trim())) {
            queryWrapper.like(SQLConf.DICT_LABEL, sysDictDataVO.getDictLabel().trim());
        }
        Page<SysDictData> page = new Page<>();
        page.setCurrent(sysDictDataVO.getCurrentPage());
        page.setSize(sysDictDataVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT, SQLConf.CREATE_TIME);
        IPage<SysDictData> pageList = sysDictDataService.page(page, queryWrapper);
        List<SysDictData> sysDictDataList = pageList.getRecords();
        Set<String> dictTypeUidList = new HashSet<>();
        sysDictDataList.forEach(item -> {
            dictTypeUidList.add(item.getDictTypeUid());
        });
        Collection<SysDictType> dictTypeList = new ArrayList<>();
        if (dictTypeUidList.size() > 0) {
            dictTypeList = sysDictTypeService.listByIds(dictTypeUidList);
        }
        Map<String, SysDictType> dictTypeMap = new HashMap<>();
        dictTypeList.forEach(item -> {
            dictTypeMap.put(item.getUid(), item);
        });
        sysDictDataList.forEach(item -> {
            item.setSysDictType(dictTypeMap.get(item.getDictTypeUid()));
        });
        pageList.setRecords(sysDictDataList);
        return pageList;
    }

    @Override
    public String addSysDictData(SysDictDataVO sysDictDataVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        // 判断添加的字典数据是否存在
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.DICT_LABEL, sysDictDataVO.getDictLabel());
        queryWrapper.eq(SQLConf.DICT_TYPE_UID, sysDictDataVO.getDictTypeUid());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last(SysConf.LIMIT_ONE);
        SysDictData temp = sysDictDataService.getOne(queryWrapper);
        if (temp != null) {
            return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
        }
        SysDictData sysDictData = new SysDictData();
        // 插入字典数据，忽略状态位【使用Spring工具类提供的深拷贝，避免出现大量模板代码】
        BeanUtils.copyProperties(sysDictDataVO, sysDictData, SysConf.STATUS);
        sysDictData.setCreateByUid(adminUid);
        sysDictData.setUpdateByUid(adminUid);
        sysDictData.insert();
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editSysDictData(SysDictDataVO sysDictDataVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        SysDictData sysDictData = sysDictDataService.getById(sysDictDataVO.getUid());
        // 更改了标签名时，判断更改的字典数据是否存在
        if (!sysDictData.getDictLabel().equals(sysDictDataVO.getDictLabel())) {
            QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.DICT_LABEL, sysDictDataVO.getDictLabel());
            queryWrapper.eq(SQLConf.DICT_TYPE_UID, sysDictDataVO.getDictTypeUid());
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.last(SysConf.LIMIT_ONE);
            SysDictData temp = sysDictDataService.getOne(queryWrapper);
            if (temp != null) {
                return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
            }
        }
        // 更新数据字典【使用Spring工具类提供的深拷贝，避免出现大量模板代码】
        BeanUtils.copyProperties(sysDictDataVO, sysDictData, SysConf.STATUS, SysConf.UID);
        sysDictData.setUpdateByUid(adminUid);
        sysDictData.setUpdateTime(new Date());
        sysDictData.setUpdateByUid(adminUid);
        sysDictData.updateById();

        // 获取Redis中特定前缀
        Set<String> keys = redisUtil.keys(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + "*");
        redisUtil.delete(keys);
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchSysDictData(List<SysDictDataVO> sysDictDataVOList) {
        HttpServletRequest request = RequestHolder.getRequest();
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        if (sysDictDataVOList.size() <= 0) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();
        sysDictDataVOList.forEach(item -> {
            uids.add(item.getUid());
        });
        Collection<SysDictData> sysDictDataList = sysDictDataService.listByIds(uids);
        sysDictDataList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
            item.setUpdateTime(new Date());
            item.setUpdateByUid(adminUid);
        });
        Boolean save = sysDictDataService.updateBatchById(sysDictDataList);
        // 获取Redis中特定前缀
        Set<String> keys = redisUtil.keys(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + "*");
        redisUtil.delete(keys);
        if (save) {
            return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.errorWithMessage(MessageConf.DELETE_FAIL);
        }
    }

    @Override
    public Map<String, Object> getListByDictType(String dictType) {
        //从Redis中获取内容
        String jsonResult = redisUtil.get(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + dictType);
        //判断redis中是否有字典
        if (StringUtils.isNotEmpty(jsonResult)) {
            Map<String, Object> map = JsonUtils.jsonToMap(jsonResult);
            return map;
        }
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.DICT_TYPE, dictType);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.last(SysConf.LIMIT_ONE);
        SysDictType sysDictType = sysDictTypeService.getOne(queryWrapper);
        if (sysDictType == null) {
            return new HashMap<>();
        }
        QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
        sysDictDataQueryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        sysDictDataQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        sysDictDataQueryWrapper.eq(SQLConf.DICT_TYPE_UID, sysDictType.getUid());
        sysDictDataQueryWrapper.orderByDesc(SQLConf.SORT, SQLConf.CREATE_TIME);
        List<SysDictData> list = sysDictDataService.list(sysDictDataQueryWrapper);

        String defaultValue = null;
        for (SysDictData sysDictData : list) {
            // 获取默认值
            if (sysDictData.getIsDefault() == SysConf.ONE) {
                defaultValue = sysDictData.getDictValue();
                break;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.DEFAULT_VALUE, defaultValue);
        result.put(SysConf.LIST, list);
        redisUtil.setEx(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + dictType, JsonUtils.objectToJson(result).toString(), 1, TimeUnit.DAYS);
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> getListByDictTypeList(List<String> dictTypeList) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        List<String> tempTypeList = new ArrayList<>();
        dictTypeList.forEach(item -> {
            //从Redis中获取内容
            String jsonResult = redisUtil.get(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + item);
            //判断redis中是否有字典
            if (StringUtils.isNotEmpty(jsonResult)) {
                Map<String, Object> tempMap = JsonUtils.jsonToMap(jsonResult);
                map.put(item, tempMap);
            } else {
                // 如果redis中没有该字典，那么从数据库中查询
                tempTypeList.add(item);
            }
        });
        // 表示数据全部从redis中获取到了，直接返回即可
        if (tempTypeList.size() <= 0) {
            return map;
        }
        // 查询 dict_type 在 tempTypeList中的
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(SQLConf.DICT_TYPE, tempTypeList);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        List<SysDictType> sysDictTypeList = sysDictTypeService.list(queryWrapper);
        sysDictTypeList.forEach(item -> {
            QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
            sysDictDataQueryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
            sysDictDataQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            sysDictDataQueryWrapper.eq(SQLConf.DICT_TYPE_UID, item.getUid());
            sysDictDataQueryWrapper.orderByDesc(SQLConf.SORT, SQLConf.CREATE_TIME);
            List<SysDictData> list = sysDictDataService.list(sysDictDataQueryWrapper);
            String defaultValue = null;
            for (SysDictData sysDictData : list) {
                // 获取默认值
                if (sysDictData.getIsDefault() == SysConf.ONE) {
                    defaultValue = sysDictData.getDictValue();
                    break;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put(SysConf.DEFAULT_VALUE, defaultValue);
            result.put(SysConf.LIST, list);
            map.put(item.getDictType(), result);
            redisUtil.setEx(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + item.getDictType(), JsonUtils.objectToJson(result).toString(), 1, TimeUnit.DAYS);
        });
        return map;
    }
}
