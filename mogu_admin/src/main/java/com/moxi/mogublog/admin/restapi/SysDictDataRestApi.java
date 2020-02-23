package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.SysDictData;
import com.moxi.mogublog.xo.entity.SysDictType;
import com.moxi.mogublog.xo.service.SysDictDataService;
import com.moxi.mogublog.xo.service.SysDictDataService;
import com.moxi.mogublog.xo.service.SysDictTypeService;
import com.moxi.mogublog.xo.vo.SysDictDataVO;
import com.moxi.mougblog.base.enums.EPublish;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 字典数据 RestApi
 * </p>
 *
 * @author 陌溪
 * @since 2020年2月15日21:16:31
 */
@RestController
@RequestMapping("/sysDictData")
@Api(value = "字典数据RestApi", tags = {"SysDictDataRestApi"})
@Slf4j
public class SysDictDataRestApi {

    @Autowired
    SysDictDataService sysDictDataService;

    @Autowired
    SysDictTypeService sysDictTypeService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "获取字典数据列表", notes = "获取字典数据列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody SysDictDataVO sysDictDataVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

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

        Collection<SysDictType>  dictTypeList = new ArrayList<>();
        if(dictTypeUidList.size() > 0) {
            dictTypeList = sysDictTypeService.listByIds(dictTypeUidList);
        }
        Map<String, SysDictType> dictTypeMap = new HashMap<>();
        dictTypeList.forEach(item-> {
            dictTypeMap.put(item.getUid(), item);
        });

        sysDictDataList.forEach(item -> {
            item.setSysDictType(dictTypeMap.get(item.getDictTypeUid()));
        });

        pageList.setRecords(sysDictDataList);

        log.info("获取字典数据列表");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加字典数据")
    @ApiOperation(value = "增加字典数据", notes = "增加字典数据", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request, @Validated({Insert.class}) @RequestBody SysDictDataVO sysDictDataVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        // 判断添加的字典数据是否存在
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.DICT_LABEL, sysDictDataVO.getDictLabel());
        queryWrapper.eq(SQLConf.DICT_TYPE_UID, sysDictDataVO.getDictTypeUid());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last("LIMIT 1");
        SysDictData temp = sysDictDataService.getOne(queryWrapper);
        if (temp != null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
        }

        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictLabel(sysDictDataVO.getDictLabel());
        sysDictData.setDictTypeUid(sysDictDataVO.getDictTypeUid());
        sysDictData.setDictValue(sysDictDataVO.getDictValue());
        sysDictData.setIsDefault(sysDictDataVO.getIsDefault());
        sysDictData.setCssClass(sysDictDataVO.getCssClass());
        sysDictData.setListClass(sysDictDataVO.getListClass());
        sysDictData.setRemark(sysDictDataVO.getRemark());
        sysDictData.setIsPublish(sysDictDataVO.getIsPublish());
        sysDictData.setSort(sysDictDataVO.getSort());
        sysDictData.setCreateByUid(adminUid);
        sysDictData.setUpdateByUid(adminUid);
        sysDictData.insert();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @OperationLogger(value = "编辑字典数据")
    @ApiOperation(value = "编辑字典数据", notes = "编辑字典数据", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request, @Validated({Update.class}) @RequestBody SysDictDataVO sysDictDataVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();


        SysDictData sysDictData = sysDictDataService.getById(sysDictDataVO.getUid());
        // 但更改了标签名时，判断更改的字典数据是否存在
        if(!sysDictData.getDictLabel().equals(sysDictDataVO.getDictLabel())) {
            QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.DICT_LABEL, sysDictDataVO.getDictLabel());
            queryWrapper.eq(SQLConf.DICT_TYPE_UID, sysDictDataVO.getDictTypeUid());
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.last("LIMIT 1");
            SysDictData temp = sysDictDataService.getOne(queryWrapper);
            if (temp != null) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
            }
        }

        sysDictData.setDictLabel(sysDictDataVO.getDictLabel());
        sysDictData.setDictTypeUid(sysDictDataVO.getDictTypeUid());
        sysDictData.setDictValue(sysDictDataVO.getDictValue());
        sysDictData.setIsDefault(sysDictDataVO.getIsDefault());
        sysDictData.setCssClass(sysDictDataVO.getCssClass());
        sysDictData.setListClass(sysDictDataVO.getListClass());
        sysDictData.setRemark(sysDictDataVO.getRemark());
        sysDictData.setSort(sysDictDataVO.getSort());
        sysDictData.setIsPublish(sysDictDataVO.getIsPublish());
        sysDictData.setCreateByUid(adminUid);
        sysDictData.setUpdateByUid(adminUid);
        sysDictData.updateById();

        // 获取Redis中特定前缀
        Set<String> keys = stringRedisTemplate.keys(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION  + "*");
        stringRedisTemplate.delete(keys);

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @OperationLogger(value = "批量删除字典数据")
    @ApiOperation(value = "批量删除字典数据", notes = "批量删除字典数据", response = String.class)
    @PostMapping("/deleteBatch")
    public String delete(HttpServletRequest request, @Validated({Delete.class}) @RequestBody List<SysDictDataVO> sysDictDataVoList, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        if (sysDictDataVoList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }

        List<String> uids = new ArrayList<>();

        sysDictDataVoList.forEach(item -> {
            uids.add(item.getUid());
        });

        Collection<SysDictData> sysDictDataList = sysDictDataService.listByIds(uids);

        sysDictDataList.forEach(item -> {
            item.setStatus(EStatus.DISABLED);
            item.setUpdateByUid(adminUid);
        });

        Boolean save = sysDictDataService.updateBatchById(sysDictDataList);

        // 获取Redis中特定前缀
        Set<String> keys = stringRedisTemplate.keys(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION  + "*");
        stringRedisTemplate.delete(keys);


        if (save) {
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DELETE_FAIL);
        }
    }

    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = String.class)
    @PostMapping("/getListByDictType")
    public String getListByDictType(@RequestParam("dictType") String dictType) {

        //从Redis中获取内容
        String jsonResult = stringRedisTemplate.opsForValue().get(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + dictType);

        //判断redis中是否有字典
        if (StringUtils.isNotEmpty(jsonResult)) {
            Map<String, Object> map = JsonUtils.jsonToMap(jsonResult);
            return ResultUtil.result(SysConf.SUCCESS, map);
        }

        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.DICT_TYPE, dictType);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        queryWrapper.last("LIMIT 1");
        SysDictType sysDictType = sysDictTypeService.getOne(queryWrapper);
        if(sysDictType == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_NOT_EXIST);
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
            if(sysDictData.getIsDefault() == SysConf.ONE) {
                defaultValue = sysDictData.getDictValue();
                break;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.DEFAULT_VALUE, defaultValue);
        result.put(SysConf.LIST, list);

        stringRedisTemplate.opsForValue().set(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + dictType, JsonUtils.objectToJson(result).toString(), 1, TimeUnit.DAYS);

        return ResultUtil.result(SysConf.SUCCESS, result);
    }

    @ApiOperation(value = "根据字典类型数组获取字典数据", notes = "根据字典类型数组获取字典数据", response = String.class)
    @PostMapping("/getListByDictTypeList")
    public String getListByDictTypeList(@RequestBody List<String> dictTypeList) {

        if(dictTypeList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }

        Map<String, Map<String, Object>> map = new HashMap<>();
        List<String> tempTypeList = new ArrayList<>();
        dictTypeList.forEach(item -> {
            //从Redis中获取内容
            String jsonResult = stringRedisTemplate.opsForValue().get(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + item);

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
        if(tempTypeList.size() <=0 ) {
            return ResultUtil.result(SysConf.SUCCESS, map);
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
                if(sysDictData.getIsDefault() == SysConf.ONE) {
                    defaultValue = sysDictData.getDictValue();
                    break;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put(SysConf.DEFAULT_VALUE, defaultValue);
            result.put(SysConf.LIST, list);

            map.put(item.getDictType(), result);

            stringRedisTemplate.opsForValue().set(SysConf.REDIS_DICT_TYPE + SysConf.REDIS_SEGMENTATION + item.getDictType(), JsonUtils.objectToJson(result).toString(), 1, TimeUnit.DAYS);
        });
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

}

