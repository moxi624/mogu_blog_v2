package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.SysDictData;
import com.moxi.mogublog.commons.entity.SysDictType;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = String.class)
    @PostMapping("/getListByDictType")
    public String getListByDictType(@RequestParam("dictType") String dictType) {

        log.info("根据字典类型获取字典数据");
        return ResultUtil.result(SysConf.SUCCESS, sysDictDataService.getListByDictType(dictType));
    }

    @ApiOperation(value = "根据字典类型数组获取字典数据", notes = "根据字典类型数组获取字典数据", response = String.class)
    @PostMapping("/getListByDictTypeList")
    public String getListByDictTypeList(@RequestBody List<String> dictTypeList) {
        log.info("根据字典类型数组获取字典数据");
        return ResultUtil.result(SysConf.SUCCESS, sysDictDataService.getListByDictTypeList(dictTypeList));
    }

}

