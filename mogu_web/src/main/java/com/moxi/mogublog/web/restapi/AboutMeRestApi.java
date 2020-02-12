package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.util.WebUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.WebConfig;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mougblog.base.enums.EAccountType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 关于我 RestApi
 *
 * @author xzx19950624@qq.com
 * @date 2018年11月12日14:51:54
 */
@RestController
@RequestMapping("/about")
@Api(value = "关于我 RestApi", tags = {"AboutMeRestApi"})
@Slf4j
public class AboutMeRestApi {
    @Autowired
    WebUtils webUtils;
    @Autowired
    AdminService adminService;
    @Autowired
    private PictureFeignClient pictureFeignClient;

    @Autowired
    WebConfigService webConfigService;

    /**
     * 获取关于我的信息
     *
     * @author xzx19950624@qq.com
     * @date 2018年11月6日下午8:57:48
     */

    @ApiOperation(value = "关于我", notes = "关于我")
    @GetMapping("/getMe")
    public String getMe(HttpServletRequest request) {

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, SysConf.ADMIN);
        queryWrapper.last("LIMIT 1");
        //清空密码，防止泄露
        Admin admin = adminService.getOne(queryWrapper);
        admin.setPassWord(null);
        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), ",");
            admin.setPhotoList(webUtils.getPicture(pictureList));
        }
        log.info("获取用户信息");
        Admin result = new Admin();
        result.setNickName(admin.getNickName());
        result.setOccupation(admin.getOccupation());
        result.setSummary(admin.getSummary());
        result.setWeChat(admin.getWeChat());
        result.setQqNumber(admin.getQqNumber());
        result.setEmail(admin.getEmail());
        result.setMobile(admin.getMobile());
        result.setAvatar(admin.getAvatar());
        result.setPhotoList(admin.getPhotoList());
        result.setGithub(admin.getGithub());
        result.setGitee(admin.getGitee());
        result.setPersonResume(admin.getPersonResume());
        return ResultUtil.result(SysConf.SUCCESS, result);
    }

    @ApiOperation(value = "获取联系方式", notes = "获取联系方式")
    @GetMapping("/getContact")
    public String getContact() {

        QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();

        // getOne结果集为多个的时候，会抛异常，随机取一条加上限制条件 queryWrapper.last("LIMIT 1")
        queryWrapper.last("LIMIT 1");
        WebConfig webConfig = webConfigService.getOne(queryWrapper);

        if (webConfig != null) {

            // 过滤一些不需要显示的用户账号信息
            String showListJson = webConfig.getShowList();

            WebConfig result = new WebConfig();

            List<String> showList = JsonUtils.jsonToList(showListJson, String.class);

            for(String item : showList) {
                if(EAccountType.EMail.getCode().equals(item)) {
                    result.setEmail(webConfig.getEmail());
                }
                if(EAccountType.QQNumber.getCode().equals(item)) {
                    result.setQqNumber(webConfig.getQqNumber());
                }
                if(EAccountType.QQGroup.getCode().equals(item)) {
                    result.setQqGroup(webConfig.getQqGroup());
                }
                if(EAccountType.Github.getCode().equals(item)) {
                    result.setGithub(webConfig.getGithub());
                }
                if(EAccountType.Gitee.getCode().equals(item)) {
                    result.setGitee(webConfig.getGitee());
                }
                if(EAccountType.WeChat.getCode().equals(item)) {
                    result.setWeChat(webConfig.getWeChat());
                }
            }
            return ResultUtil.result(SysConf.SUCCESS, result);
        } else {
            return ResultUtil.result(SysConf.ERROR, "获取失败");
        }

    }

}

