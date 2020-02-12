package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.WebConfig;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mogublog.xo.vo.WebConfigVO;
import com.moxi.mougblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 网站配置表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年11月11日15:19:28
 */
@Api(value = "网站配置RestApi", tags = {"WebConfigRestApi"})
@RestController
@RequestMapping("/webConfig")
@Slf4j
public class WebConfigRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    WebConfigService webConfigService;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取网站配置", notes = "获取网站配置")
    @GetMapping("/getWebConfig")
    public String getWebConfig() {

        QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        WebConfig webConfig = webConfigService.getOne(queryWrapper);

        //获取图片
        if (webConfig != null && StringUtils.isNotEmpty(webConfig.getLogo())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getLogo(), SysConf.FILE_SEGMENTATION);
            webConfig.setPhotoList(webUtils.getPicture(pictureList));
        }

        //获取支付宝收款二维码
        if (webConfig != null && StringUtils.isNotEmpty(webConfig.getAliPay())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getAliPay(), SysConf.FILE_SEGMENTATION);
            if (webUtils.getPicture(pictureList).size() > 0) {
                webConfig.setAliPayPhoto(webUtils.getPicture(pictureList).get(0));
            }

        }
        //获取微信收款二维码
        if (webConfig != null && StringUtils.isNotEmpty(webConfig.getWeixinPay())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getWeixinPay(), SysConf.FILE_SEGMENTATION);
            if (webUtils.getPicture(pictureList).size() > 0) {
                webConfig.setWeixinPayPhoto(webUtils.getPicture(pictureList).get(0));
            }

        }

        return ResultUtil.result(SysConf.SUCCESS, webConfig);
    }

    @OperationLogger(value = "修改网站配置")
    @ApiOperation(value = "修改网站配置", notes = "修改网站配置")
    @PostMapping("/editWebConfig")
    public String editWebConfig(@Validated({Update.class}) @RequestBody WebConfigVO webConfigVO, BindingResult result) {

        if (StringUtils.isEmpty(webConfigVO.getUid())) {

            WebConfig webConfig = new WebConfig();
            webConfig.setLogo(webConfigVO.getLogo());
            webConfig.setName(webConfigVO.getName());
            webConfig.setTitle(webConfigVO.getTitle());
            webConfig.setSummary(webConfigVO.getSummary());
            webConfig.setKeyword(webConfigVO.getKeyword());
            webConfig.setAuthor(webConfigVO.getAuthor());
            webConfig.setRecordNum(webConfigVO.getRecordNum());
            webConfig.setAliPay(webConfigVO.getAliPay());
            webConfig.setWeixinPay(webConfigVO.getWeixinPay());
            webConfig.setStartComment(webConfigVO.getStartComment());

            // 设置关注我们
            webConfig.setEmail(webConfigVO.getEmail());
            webConfig.setQqNumber(webConfigVO.getQqNumber());
            webConfig.setQqGroup(webConfigVO.getQqGroup());
            webConfig.setGithub(webConfigVO.getGithub());
            webConfig.setGitee(webConfigVO.getGitee());
            webConfig.setWeChat(webConfigVO.getWeChat());
            webConfig.setShowList(webConfigVO.getShowList());


            webConfigService.save(webConfig);
        } else {
            WebConfig webConfig = webConfigService.getById(webConfigVO.getUid());
            webConfig.setLogo(webConfigVO.getLogo());
            webConfig.setName(webConfigVO.getName());
            webConfig.setTitle(webConfigVO.getTitle());
            webConfig.setSummary(webConfigVO.getSummary());
            webConfig.setKeyword(webConfigVO.getKeyword());
            webConfig.setAuthor(webConfigVO.getAuthor());
            webConfig.setRecordNum(webConfigVO.getRecordNum());
            webConfig.setAliPay(webConfigVO.getAliPay());
            webConfig.setWeixinPay(webConfigVO.getWeixinPay());
            webConfig.setStartComment(webConfigVO.getStartComment());

            // 设置关注我们
            webConfig.setEmail(webConfigVO.getEmail());
            webConfig.setQqNumber(webConfigVO.getQqNumber());
            webConfig.setQqGroup(webConfigVO.getQqGroup());
            webConfig.setGithub(webConfigVO.getGithub());
            webConfig.setGitee(webConfigVO.getGitee());
            webConfig.setWeChat(webConfigVO.getWeChat());
            webConfig.setShowList(webConfigVO.getShowList());

            webConfigService.updateById(webConfig);
        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }
}

