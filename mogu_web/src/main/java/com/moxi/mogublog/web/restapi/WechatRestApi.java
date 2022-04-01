package com.moxi.mogublog.web.restapi;


import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.utils.wechat.SignUtil;
import com.moxi.mogublog.web.global.RedisConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.service.SystemConfigService;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mougblog.base.enums.EGender;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号 RestApi
 *
 * @author 陌溪
 * @date 2018年11月12日14:51:54
 */
@RestController
@RequestMapping("/wechat")
@Api(value = "关于我相关接口", tags = {"关于我相关接口"})
@Slf4j
public class WechatRestApi {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private UserService userService;
    @Resource
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private SystemConfigService systemConfigService;
    @Value(value = "${data.web.project_name_en}")
    private String PROJECT_NAME_EN;
    @Value(value = "${DEFAULE_PWD}")
    private String DEFAULE_PWD;
    @Value(value = "${BLOG.USER_TOKEN_SURVIVAL_TIME}")
    private Long userTokenSurvivalTime;

    @ApiOperation(value = "获取微信公众号状态", notes = "获取微信公众号状态")
    @GetMapping("/wechatCheck")
    public String wechatCheck(HttpServletRequest request) {
        System.out.println("我进来了");
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (SignUtil.checkSignature(msgSignature, msgTimestamp, msgNonce)) {
            return echostr;
        }
        return null;
    }

    @ApiOperation(value = "用户扫码后触发的事件", notes = "用户扫码后触发的事件")
    @PostMapping("/wechatCheck")
    public String index(HttpServletRequest request) throws Exception {

        Map<String, String> map = SignUtil.xmlToMap(request);
        System.out.println(map);
        String event = map.get("Event");
        if ("SCAN".equals(event)) {
            System.out.println("扫码进来了，已经关注过");
        } else if ("subscribe".equals(event)) {
            System.out.println("订阅公众号");
        } else if ("subscribe".equals(event)) {
            System.out.println("取消订阅公众号");
        }

        String accessToken = redisUtil.get("WE_CHAT_ACCESS_TOKEN");
        // 可以当做token
        String ticket = map.get("Ticket");

        String toUserName = map.get("ToUserName");
        String fromUserName = map.get("FromUserName");

        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + fromUserName;
        String result = HttpRequest.get(url).execute().body();
        System.out.println(result);


        //##############################  开始  ############################
        Map<String, Object> data = JsonUtils.jsonToMap(result);
        Boolean exist = false;
        User user;
        //判断user是否存在
        if (data.get("openid") != null) {
            user = userService.getUserBySourceAnduuid("wechat", data.get("openid").toString());
            if (user != null) {
                exist = true;
            } else {
                user = new User();
            }
        } else {
            System.out.println("获取用户失败");
            return "error";
        }

        // 判断邮箱是否存在
        if (data.get(SysConf.EMAIL) != null) {
            String email = data.get(SysConf.EMAIL).toString();
            user.setEmail(email);
        }

        // 判断用户性别
        if (data.get("sex") != null && !exist) {
            String gender = data.get("sex").toString();
            if (EGender.MALE.equals(gender)) {
                user.setGender(EGender.MALE);
            } else if (EGender.FEMALE.equals(gender)) {
                user.setGender(EGender.FEMALE);
            } else {
                user.setGender(EGender.UNKNOWN);
            }
        }

        // 通过头像uid获取图片
        String pictureList = this.pictureFeignClient.getPicture(user.getAvatar(), SysConf.FILE_SEGMENTATION);
        List<String> photoList = webUtil.getPicture(pictureList);
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(pictureList, Map.class);

        // 判断该用户是否含有头像信息
        if (SysConf.SUCCESS.equals(picMap.get(SysConf.CODE)) && photoList.size() > 0) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get(SysConf.DATA);
            String fileOldName = picData.get(0).get(SysConf.FILE_OLD_NAME).toString();

            // 判断本地的图片是否和第三方登录的一样，如果不一样，那么更新
            // 如果旧名称为blob表示是用户自定义的，代表用户在本网站使用了自定义头像，那么就再也不同步更新网站上的了
            if (fileOldName.equals(data.get(SysConf.AVATAR)) || SysConf.BLOB.equals(fileOldName)) {
                user.setPhotoUrl(photoList.get(0));
            } else {
                updateUserPhoto(data, user);
            }
        } else {
            // 当获取头像失败时，需要从网站上进行获取
            updateUserPhoto(data, user);
        }

        if (data.get(SysConf.NICKNAME) != null) {
            user.setNickName(data.get(SysConf.NICKNAME).toString());
        }

        if (user.getLoginCount() == null) {
            user.setLoginCount(0);
        } else {
            user.setLoginCount(user.getLoginCount() + 1);
        }
        // 获取浏览器，IP来源，以及操作系统
        user = userService.serRequestInfo(user);
        // 暂时将token也存入到user表中，为了以后方便更新redis中的内容
        user.setValidCode(ticket);
        if (exist) {
            user.updateById();
        } else {
            user.setUuid(data.get("openid").toString());
            user.setSource("wechat");
            String userName = PROJECT_NAME_EN.concat(Constants.SYMBOL_UNDERLINE).concat(user.getSource()).concat(Constants.SYMBOL_UNDERLINE).concat(user.getUuid());
            user.setUserName(userName);
            // 如果昵称为空，那么直接设置用户名
            if (StringUtils.isEmpty(user.getNickName())) {
                user.setNickName(userName);
            }
            // 默认密码
            user.setPassWord(MD5Utils.string2MD5(DEFAULE_PWD));
            // 设置是否开启评论邮件通知【关闭】
            user.setStartEmailNotification(EOpenStatus.CLOSE_STATUS);
            user.insert();
        }
        // 过滤密码
        user.setPassWord("");
        if (user != null) {
            //将从数据库查询的数据缓存到redis中
            redisUtil.setEx(RedisConf.USER_TOKEN + Constants.SYMBOL_COLON + ticket, JsonUtils.objectToJson(user), userTokenSurvivalTime, TimeUnit.HOURS);
        }

        //############################## 结束 ############################

        String returnData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                " <xml>" +
                " <Content>" + "hello" + "</Content>" +
                " <ToUserName>" + fromUserName + "</ToUserName>" +
                " <FromUserName>" + toUserName + "</FromUserName>" +
                " <CreateTime>" + new Date().getTime() / 1000 + "</CreateTime>" +
                " <MsgType>text</MsgType>" +
                " </xml>";

        return returnData;
    }


    @ApiOperation(value = "获取微信公众号登录二维码", notes = "获取微信公众号登录二维码")
    @GetMapping("/getWechatOrCodeTicket")
    public String getWechatOrCodeTicket() {
        String requireAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxfe2c610ba4fa2839&secret=49077bace79e0b11a53c1ced6eaf289d";
        String result = HttpRequest.post(requireAccessTokenUrl).execute().body();
        System.out.println(result);
        Map<String, Object> map = JsonUtils.jsonToMap(result);
        if (map.get("access_token") != null) {
            String accessToken = map.get("access_token").toString();
            // 获取到accessToken，存到redis
            redisUtil.setEx("WE_CHAT_ACCESS_TOKEN", accessToken, 1, TimeUnit.HOURS);
            String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
            String json = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
            String result2 = HttpRequest.post(url).body(json).execute().body();
            System.out.println(result2);
            Map<String, Object> resultMap = JsonUtils.jsonToMap(result2);
            return ResultUtil.successWithData(resultMap);
        }
        return ResultUtil.errorWithMessage("获取失败二维码失败");
    }

    @ApiOperation(value = "获取微信用户扫码登录状态", notes = "获取微信用户扫码登录状态")
    @GetMapping("/getUserLoginStatus")
    public String getUserLoginStatus(@RequestParam("ticket") String ticket) {

        String json = redisUtil.get(RedisConf.USER_TOKEN + Constants.SYMBOL_COLON + ticket);
        if (StringUtils.isNotEmpty(json)) {
            return ResultUtil.successWithData(JsonUtils.jsonToMap(json));
        }
        return ResultUtil.errorWithMessage("获取用户信息失败");
    }


    /**
     * 更新用户头像
     *
     * @param data
     * @param user
     */
    private void updateUserPhoto(Map<String, Object> data, User user) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last(SysConf.LIMIT_ONE);
        SystemConfig systemConfig = systemConfigService.getOne(queryWrapper);
        // 获取到头像，然后上传到自己服务器
        FileVO fileVO = new FileVO();
        fileVO.setAdminUid(SysConf.DEFAULT_UID);
        fileVO.setUserUid(SysConf.DEFAULT_UID);
        fileVO.setProjectName(SysConf.BLOG);
        fileVO.setSortName(SysConf.ADMIN);
        fileVO.setSystemConfig(JsonUtils.object2Map(systemConfig));
        List<String> urlList = new ArrayList<>();
        if (data.get("headimgurl") != null) {
            urlList.add(data.get("headimgurl").toString());
        }
        fileVO.setUrlList(urlList);
        String res = this.pictureFeignClient.uploadPicsByUrl(fileVO);
        Map<String, Object> resultMap = JsonUtils.jsonToMap(res);
        if (resultMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultMap.get(SysConf.CODE).toString())) {
            if (resultMap.get(SysConf.DATA) != null) {
                List<Map<String, Object>> listMap = (List<Map<String, Object>>) resultMap.get(SysConf.DATA);
                if (listMap != null && listMap.size() > 0) {
                    Map<String, Object> pictureMap = listMap.get(0);

                    String localPictureBaseUrl = systemConfig.getLocalPictureBaseUrl();
                    String qiNiuPictureBaseUrl = systemConfig.getQiNiuPictureBaseUrl();
                    String picturePriority = systemConfig.getPicturePriority();
                    user.setAvatar(pictureMap.get(SysConf.UID).toString());
                    // 判断图片优先展示
                    if (EOpenStatus.OPEN.equals(picturePriority)) {
                        // 使用七牛云
                        if (pictureMap.get(SysConf.QI_NIU_URL) != null && pictureMap.get(SysConf.UID) != null) {
                            user.setPhotoUrl(qiNiuPictureBaseUrl + pictureMap.get(SysConf.QI_NIU_URL).toString());
                        }
                    } else {
                        // 使用自建图片服务器
                        if (pictureMap.get(SysConf.PIC_URL) != null && pictureMap.get(SysConf.UID) != null) {
                            user.setPhotoUrl(localPictureBaseUrl + pictureMap.get(SysConf.PIC_URL).toString());
                        }
                    }
                }
            }
        }
    }

}

