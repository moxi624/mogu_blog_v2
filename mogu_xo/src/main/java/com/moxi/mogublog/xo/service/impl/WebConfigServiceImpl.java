package com.moxi.mogublog.xo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.WebConfig;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.WebConfigMapper;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.WebConfigVO;
import com.moxi.mougblog.base.enums.EAccountType;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 网站配置关系表 服务实现类
 * </p>
 *
 * @author 陌溪
 * @since 2018年11月11日15:10:41
 */
@Service
public class WebConfigServiceImpl extends SuperServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    @Autowired
    WebUtil webUtil;

    @Autowired
    WebConfigService webConfigService;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @Override
    public WebConfig getWebConfig() {

        QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        WebConfig webConfig = webConfigService.getOne(queryWrapper);

        //获取图片
        if (webConfig != null && StringUtils.isNotEmpty(webConfig.getLogo())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getLogo(), SysConf.FILE_SEGMENTATION);
            webConfig.setPhotoList(webUtil.getPicture(pictureList));
        }

        //获取支付宝收款二维码
        if (webConfig != null && StringUtils.isNotEmpty(webConfig.getAliPay())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getAliPay(), SysConf.FILE_SEGMENTATION);
            if (webUtil.getPicture(pictureList).size() > 0) {
                webConfig.setAliPayPhoto(webUtil.getPicture(pictureList).get(0));
            }

        }
        //获取微信收款二维码
        if (webConfig != null && StringUtils.isNotEmpty(webConfig.getWeixinPay())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getWeixinPay(), SysConf.FILE_SEGMENTATION);
            if (webUtil.getPicture(pictureList).size() > 0) {
                webConfig.setWeixinPayPhoto(webUtil.getPicture(pictureList).get(0));
            }
        }
        return webConfig;
    }

    @Override
    public WebConfig getWebConfigByShowList() {
        QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        WebConfig webConfig = webConfigService.getOne(queryWrapper);

        if (ObjectUtil.isNotNull(webConfig) && StringUtils.isNotEmpty(webConfig.getLogo())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getLogo(), SysConf.FILE_SEGMENTATION);
            webConfig.setPhotoList(webUtil.getPicture(pictureList));
        }

        //获取支付宝收款二维码
        if (StringUtils.isNotEmpty(webConfig.getAliPay())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getAliPay(), SysConf.FILE_SEGMENTATION);
            if (webUtil.getPicture(pictureList).size() > 0) {
                webConfig.setAliPayPhoto(webUtil.getPicture(pictureList).get(0));
            }
        }
        //获取微信收款二维码
        if (StringUtils.isNotEmpty(webConfig.getWeixinPay())) {
            String pictureList = this.pictureFeignClient.getPicture(webConfig.getWeixinPay(), SysConf.FILE_SEGMENTATION);
            if (webUtil.getPicture(pictureList).size() > 0) {
                webConfig.setWeixinPayPhoto(webUtil.getPicture(pictureList).get(0));
            }
        }

        // 过滤一些不需要显示的用户账号信息
        String showListJson = webConfig.getShowList();

        String email = webConfig.getEmail();
        webConfig.setEmail("");
        String qqNumber = webConfig.getQqNumber();
        webConfig.setQqNumber("");
        String qqGroup = webConfig.getQqGroup();
        webConfig.setQqGroup("");
        String github = webConfig.getGithub();
        webConfig.setGithub("");
        String gitee = webConfig.getGitee();
        webConfig.setGitee("");
        String weChat = webConfig.getWeChat();
        webConfig.setWeChat("");

        List<String> showList = JsonUtils.jsonToList(showListJson, String.class);

        for (String item : showList) {
            if (EAccountType.EMail.getCode().equals(item)) {
                webConfig.setEmail(email);
            }
            if (EAccountType.QQNumber.getCode().equals(item)) {
                webConfig.setQqNumber(qqNumber);
            }
            if (EAccountType.QQGroup.getCode().equals(item)) {
                webConfig.setQqGroup(qqGroup);
            }
            if (EAccountType.Github.getCode().equals(item)) {
                webConfig.setGithub(github);
            }
            if (EAccountType.Gitee.getCode().equals(item)) {
                webConfig.setGitee(gitee);
            }
            if (EAccountType.WeChat.getCode().equals(item)) {
                webConfig.setWeChat(weChat);
            }
        }
        return webConfig;
    }

    @Override
    public String editWebConfig(WebConfigVO webConfigVO) {
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
            webConfig.setUpdateTime(new Date());
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
            webConfig.setUpdateTime(new Date());
            webConfigService.updateById(webConfig);
        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }
}
