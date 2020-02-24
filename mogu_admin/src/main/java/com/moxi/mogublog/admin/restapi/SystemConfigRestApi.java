package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.SystemConfig;
import com.moxi.mogublog.xo.service.SystemConfigService;
import com.moxi.mogublog.xo.vo.SystemConfigVO;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统配置表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2020年1月21日09:24:37
 */
@Api(value = "系统配置RestApi", tags = {"SystemConfigRestApi"})
@RestController
@RequestMapping("/systemConfig")
@Slf4j
public class SystemConfigRestApi {

    @Autowired
    SystemConfigService systemConfigService;


    @ApiOperation(value = "获取系统配置", notes = "获取系统配置")
    @GetMapping("/getSystemConfig")
    public String getSystemConfig() {

        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        SystemConfig SystemConfig = systemConfigService.getOne(queryWrapper);

        return ResultUtil.result(SysConf.SUCCESS, SystemConfig);
    }

    @OperationLogger(value = "修改系统配置")
    @ApiOperation(value = "修改系统配置", notes = "修改系统配置")
    @PostMapping("/editSystemConfig")
    public String editSystemConfig(@RequestBody SystemConfigVO systemConfigVO) {


        if ("0".equals(systemConfigVO.getUploadLocal()) && "0".equals(systemConfigVO.getUploadQiNiu())) {
            return ResultUtil.result(SysConf.ERROR, "图片必须选择上传到一个区域");
        }

        if ("0".equals(systemConfigVO.getPicturePriority()) && "0".equals(systemConfigVO.getUploadLocal())) {
            return ResultUtil.result(SysConf.ERROR, "图片显示优先级为本地优先，必须开启图片上传本地");
        }

        if ("1".equals(systemConfigVO.getPicturePriority()) && "0".equals(systemConfigVO.getUploadQiNiu())) {
            return ResultUtil.result(SysConf.ERROR, "图片显示优先级为七牛云优先，必须开启图片上传七牛云");
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
            systemConfig.updateById();

        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }
}

