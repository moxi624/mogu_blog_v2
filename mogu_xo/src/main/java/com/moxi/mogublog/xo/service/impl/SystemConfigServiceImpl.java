package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.SystemConfigMapper;
import com.moxi.mogublog.xo.service.SystemConfigService;
import com.moxi.mogublog.xo.vo.SystemConfigVO;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 系统配置关系表 服务实现类
 * </p>
 *
 * @author 陌溪
 * @since 2020年1月21日09:06:18
 */
@Service
public class SystemConfigServiceImpl extends SuperServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public SystemConfig getConfig() {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last("LIMIT 1");
        SystemConfig SystemConfig = systemConfigService.getOne(queryWrapper);
        return SystemConfig;
    }

    @Override
    public String cleanRedisByKey(List<String> key) {
        if (key == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }

        key.forEach(item -> {
            // 表示清空所有key
            if (RedisConf.ALL.equals(item)) {
                Set<String> keys = redisUtil.keys("*");
                redisUtil.delete(keys);
            } else {
                // 获取Redis中特定前缀
                Set<String> keys = redisUtil.keys(key + "*");
                redisUtil.delete(keys);
            }
        });
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public String editSystemConfig(SystemConfigVO systemConfigVO) {
        if (EOpenStatus.CLOSE.equals(systemConfigVO.getUploadLocal()) && EOpenStatus.CLOSE.equals(systemConfigVO.getUploadQiNiu())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PICTURE_MUST_BE_SELECT_AREA);
        }

        if (EOpenStatus.CLOSE.equals(systemConfigVO.getPicturePriority()) && EOpenStatus.CLOSE.equals(systemConfigVO.getUploadLocal())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.MUST_BE_OPEN_LOCAL_UPLOAD);
        }

        if (EOpenStatus.OPEN.equals(systemConfigVO.getPicturePriority()) && EOpenStatus.CLOSE.equals(systemConfigVO.getUploadQiNiu())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.MUST_BE_OPEN_QI_NIU_UPLOAD);
        }

        if (StringUtils.isEmpty(systemConfigVO.getUid())) {

            SystemConfig systemConfig = new SystemConfig();

            // 设置七牛云相关
            systemConfig.setLocalPictureBaseUrl(systemConfigVO.getLocalPictureBaseUrl());
            systemConfig.setQiNiuPictureBaseUrl(systemConfigVO.getQiNiuPictureBaseUrl());
            systemConfig.setQiNiuAccessKey(systemConfigVO.getQiNiuAccessKey());
            systemConfig.setQiNiuSecretKey(systemConfigVO.getQiNiuSecretKey());
            systemConfig.setQiNiuBucket(systemConfigVO.getQiNiuBucket());
            systemConfig.setQiNiuArea(systemConfigVO.getQiNiuArea());
            systemConfig.setUploadLocal(systemConfigVO.getUploadLocal());
            systemConfig.setUploadQiNiu(systemConfigVO.getUploadQiNiu());
            systemConfig.setPicturePriority(systemConfigVO.getPicturePriority());

            // 设置邮箱相关
            systemConfig.setEmail(systemConfigVO.getEmail());
            systemConfig.setEmailPassword(systemConfigVO.getEmailPassword());
            systemConfig.setEmailUserName(systemConfigVO.getEmailUserName());
            systemConfig.setSmtpAddress(systemConfigVO.getSmtpAddress());
            systemConfig.setSmtpPort(systemConfigVO.getSmtpPort());
            systemConfig.insert();
        } else {

            SystemConfig systemConfig = systemConfigService.getById(systemConfigVO.getUid());
            // 设置七牛云相关
            systemConfig.setLocalPictureBaseUrl(systemConfigVO.getLocalPictureBaseUrl());
            systemConfig.setQiNiuPictureBaseUrl(systemConfigVO.getQiNiuPictureBaseUrl());
            systemConfig.setQiNiuAccessKey(systemConfigVO.getQiNiuAccessKey());
            systemConfig.setQiNiuSecretKey(systemConfigVO.getQiNiuSecretKey());
            systemConfig.setQiNiuBucket(systemConfigVO.getQiNiuBucket());
            systemConfig.setQiNiuArea(systemConfigVO.getQiNiuArea());
            systemConfig.setUploadLocal(systemConfigVO.getUploadLocal());
            systemConfig.setUploadQiNiu(systemConfigVO.getUploadQiNiu());
            systemConfig.setPicturePriority(systemConfigVO.getPicturePriority());

            // 设置邮箱相关
            systemConfig.setEmail(systemConfigVO.getEmail());
            systemConfig.setEmailPassword(systemConfigVO.getEmailPassword());
            systemConfig.setEmailUserName(systemConfigVO.getEmailUserName());
            systemConfig.setSmtpAddress(systemConfigVO.getSmtpAddress());
            systemConfig.setSmtpPort(systemConfigVO.getSmtpPort());

            systemConfig.setUpdateTime(new Date());
            systemConfig.updateById();

        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }
}
