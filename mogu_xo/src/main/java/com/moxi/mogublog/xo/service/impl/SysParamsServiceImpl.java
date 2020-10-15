package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.SysParams;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.SysParamsMapper;
import com.moxi.mogublog.xo.service.SysParamsService;
import com.moxi.mogublog.xo.vo.SysParamsVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
import com.moxi.mougblog.base.global.ErrorCode;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * 参数配置 服务实现类
 *
 * @author 陌溪
 * @date 2020年7月21日16:41:28
 */
@Service
public class SysParamsServiceImpl extends SuperServiceImpl<SysParamsMapper, SysParams> implements SysParamsService {

    @Autowired
    SysParamsService sysParamsService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public IPage<SysParams> getPageList(SysParamsVO sysParamsVO) {
        QueryWrapper<SysParams> queryWrapper = new QueryWrapper<>();
        // 参数名称
        if (StringUtils.isNotEmpty(sysParamsVO.getParamsName())) {
            queryWrapper.like(SQLConf.PARAMS_NAME, sysParamsVO.getParamsName().trim());
        }
        // 参数键名
        if (StringUtils.isNotEmpty(sysParamsVO.getParamsKey())) {
            queryWrapper.like(SQLConf.PARAMS_KEY, sysParamsVO.getParamsKey().trim());
        }
        Page<SysParams> page = new Page<>();
        page.setCurrent(sysParamsVO.getCurrentPage());
        page.setSize(sysParamsVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT, SQLConf.CREATE_TIME);
        IPage<SysParams> pageList = sysParamsService.page(page, queryWrapper);
        return pageList;
    }

    @Override
    public SysParams getSysParamsByKey(String paramsKey) {
        QueryWrapper<SysParams> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.PARAMS_KEY, paramsKey);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last(SysConf.LIMIT_ONE);
        SysParams sysParams = sysParamsService.getOne(queryWrapper);
        return sysParams;
    }

    @Override
    public String getSysParamsValueByKey(String paramsKey) {
        // 判断Redis中是否包含该key的数据
        String redisKey = RedisConf.SYSTEM_PARAMS + RedisConf.SEGMENTATION + paramsKey;
        String paramsValue = redisUtil.get(redisKey);
        // 如果Redis中不存在，那么从数据库中获取
        if (StringUtils.isEmpty(paramsValue)) {
            SysParams sysParams = sysParamsService.getSysParamsByKey(paramsKey);
            // 如果数据库也不存在，将抛出异常【需要到找到 doc/数据库脚本 更新数据库中的 t_sys_params表】
            if (sysParams == null || StringUtils.isEmpty(sysParams.getParamsValue())) {
                throw new QueryException(ErrorCode.PLEASE_CONFIGURE_SYSTEM_PARAMS, MessageConf.PLEASE_CONFIGURE_SYSTEM_PARAMS);
            }
            paramsValue = sysParams.getParamsValue();
            redisUtil.set(redisKey, paramsValue);
        }
        return paramsValue;
    }

    @Override
    public String addSysParams(SysParamsVO sysParamsVO) {
        // 判断添加的字典类型是否存在
        QueryWrapper<SysParams> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.PARAMS_KEY, sysParamsVO.getParamsKey());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last(SysConf.LIMIT_ONE);
        SysParams temp = sysParamsService.getOne(queryWrapper);
        if (temp != null) {
            return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
        }
        SysParams sysParams = new SysParams();
        sysParams.setParamsName(sysParamsVO.getParamsName());
        sysParams.setParamsKey(sysParamsVO.getParamsKey());
        sysParams.setParamsValue(sysParamsVO.getParamsValue());
        sysParams.setParamsType(sysParamsVO.getParamsType());
        sysParams.setRemark(sysParamsVO.getRemark());
        sysParams.setSort(sysParamsVO.getSort());
        sysParams.insert();
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editSysParams(SysParamsVO sysParamsVO) {
        SysParams sysParams = sysParamsService.getById(sysParamsVO.getUid());
        // 判断编辑的参数键名是否存在
        if (!sysParamsVO.getParamsKey().equals(sysParams.getParamsKey())) {
            QueryWrapper<SysParams> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.PARAMS_KEY, sysParamsVO.getParamsKey());
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.last(SysConf.LIMIT_ONE);
            SysParams temp = sysParamsService.getOne(queryWrapper);
            if (temp != null) {
                return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
            }
        }
        sysParams.setParamsName(sysParamsVO.getParamsName());
        sysParams.setParamsKey(sysParamsVO.getParamsKey());
        sysParams.setParamsValue(sysParamsVO.getParamsValue());
        sysParams.setParamsType(sysParamsVO.getParamsType());
        sysParams.setRemark(sysParamsVO.getRemark());
        sysParams.setSort(sysParamsVO.getSort());
        sysParams.setUpdateTime(new Date());
        sysParams.updateById();
        // 清空Redis中存在的配置
        redisUtil.delete(RedisConf.SYSTEM_PARAMS + RedisConf.SEGMENTATION + sysParamsVO.getParamsKey());
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchSysParams(List<SysParamsVO> sysParamsVOList) {
        List<String> sysParamsUidList = new ArrayList<>();
        sysParamsVOList.forEach(item -> {
            sysParamsUidList.add(item.getUid());
        });
        if (sysParamsUidList.size() >= 0) {
            Collection<SysParams> sysParamsList = sysParamsService.listByIds(sysParamsUidList);
            // 更新完成数据库后，还需要清空Redis中的缓存，因此需要存储键值
            List<String> redisKeys = new ArrayList<>();
            sysParamsList.forEach(item -> {
                item.setStatus(EStatus.DISABLED);
                redisKeys.add(RedisConf.SYSTEM_PARAMS + RedisConf.SEGMENTATION + item.getParamsKey());
            });
            sysParamsService.updateBatchById(sysParamsList);
            // 清空Redis中的配置
            redisUtil.delete(redisKeys);
            return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.errorWithMessage(MessageConf.DELETE_FAIL);
        }
    }
}
