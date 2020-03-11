package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.SysDictData;
import com.moxi.mogublog.xo.entity.SysDictType;
import com.moxi.mogublog.xo.service.SysDictDataService;
import com.moxi.mogublog.xo.service.SysDictTypeService;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 字典数据查询 RestApi
 * </p>
 *
 * @author 陌溪
 * @since 2020年3月11日10:37:13
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

