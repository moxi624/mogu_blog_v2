package com.moxi.mogublog.web.restapi;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Feedback;
import com.moxi.mogublog.commons.entity.Link;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.utils.RabbitMqUtil;
import com.moxi.mogublog.xo.service.FeedbackService;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.service.SystemConfigService;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.FeedbackVO;
import com.moxi.mogublog.xo.vo.LinkVO;
import com.moxi.mogublog.xo.vo.UserVO;
import com.moxi.mougblog.base.enums.EGender;
import com.moxi.mougblog.base.enums.ELinkStatus;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 第三方登录认证
 */
@RestController
@RequestMapping("/oauth")
@Api(value = "第三方登录相关接口", tags = {"第三方登录相关接口"})
@Slf4j
public class AuthRestApi {
    @Autowired
    WebUtil webUtil;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    LinkService linkService;
    @Autowired
    RabbitMqUtil rabbitMqUtil;
    @Autowired
    private UserService userService;
    @Value(value = "${justAuth.clientId.gitee}")
    private String giteeClienId;
    @Value(value = "${justAuth.clientSecret.gitee}")
    private String giteeClientSecret;
    @Value(value = "${justAuth.clientId.github}")
    private String githubClienId;
    @Value(value = "${justAuth.clientSecret.github}")
    private String githubClientSecret;
    @Value(value = "${justAuth.clientId.qq}")
    private String qqClienId;
    @Value(value = "${justAuth.clientSecret.qq}")
    private String qqClientSecret;
    @Value(value = "${data.webSite.url}")
    private String webSiteUrl;
    @Value(value = "${data.web.url}")
    private String moguWebUrl;
    @Value(value = "${BLOG.USER_TOKEN_SURVIVAL_TIME}")
    private Long userTokenSurvivalTime;
    @Value(value = "${PROJECT_NAME_EN}")
    private String PROJECT_NAME_EN;
    @Value(value = "${DEFAULE_PWD}")
    private String DEFAULE_PWD;

    @Value(value = "${uniapp.qq.appid}")
    private String APP_ID;
    @Value(value = "${uniapp.qq.appid}")
    private String SECRET;
    @Value(value = "${uniapp.qq.grant_type}")
    private String GRANT_TYPE;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取认证", notes = "获取认证")
    @RequestMapping("/render")
    public String renderAuth(String source, HttpServletResponse response) throws IOException {
        log.info("进入render:" + source);
        AuthRequest authRequest = getAuthRequest(source);
        String token = AuthStateUtils.createState();
        String authorizeUrl = authRequest.authorize(token);
        Map<String, String> map = new HashMap<>();
        map.put(SQLConf.URL, authorizeUrl);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }


    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建gitee授权应用时的回调地址应为：http://127.0.0.1:8603/oauth/callback/gitee
     */
    @RequestMapping("/callback/{source}")
    public void login(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        log.info("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        if (response.getCode() == 5000) {
            // 跳转到500错误页面
            httpServletResponse.sendRedirect(webSiteUrl + "500");
            return;
        }
        String result = JSONObject.toJSONString(response);
        Map<String, Object> map = JsonUtils.jsonToMap(result);
        Map<String, Object> data = JsonUtils.jsonToMap(JsonUtils.objectToJson(map.get(SysConf.DATA)));
        Map<String, Object> token = new HashMap<>();
        String accessToken = "";
        if (data == null || data.get(SysConf.TOKEN) == null) {
            // 跳转到500错误页面
            httpServletResponse.sendRedirect(webSiteUrl + "500");
            return;
        } else {
            token = JsonUtils.jsonToMap(JsonUtils.objectToJson(data.get(SysConf.TOKEN)));
            accessToken = token.get(SysConf.ACCESS_TOKEN).toString();
        }

        Boolean exist = false;
        User user = null;
        //判断user是否存在
        if (data.get(SysConf.UUID) != null && data.get(SysConf.SOURCE) != null) {
            user = userService.getUserBySourceAnduuid(data.get(SysConf.SOURCE).toString(), data.get(SysConf.UUID).toString());
            if (user != null) {
                exist = true;
            } else {
                user = new User();
            }
        } else {
            return;
        }

        // 判断邮箱是否存在
        if (data.get(SysConf.EMAIL) != null) {
            String email = data.get(SysConf.EMAIL).toString();
            user.setEmail(email);
        }

        // 判断用户性别
        if (data.get(SysConf.GENDER) != null) {
            Object gender = data.get(SysConf.GENDER).toString();
            if (SysConf.MALE.equals(gender)) {
                user.setGender(EGender.MALE);
            } else if (SysConf.FEMALE.equals(gender)) {
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
        user.setValidCode(accessToken);

        if (exist) {
            user.updateById();
        } else {

            user.setUuid(data.get(SysConf.UUID).toString());
            user.setSource(data.get(SysConf.SOURCE).toString());

            String userName = PROJECT_NAME_EN.concat("_").concat(user.getSource()).concat("_").concat(user.getUuid());
            user.setUserName(userName);

            // 如果昵称为空，那么直接设置用户名
            if (StringUtils.isEmpty(user.getNickName())) {
                user.setNickName(userName);
            }

            // 默认密码
            user.setPassWord(MD5Utils.string2MD5(DEFAULE_PWD));
            user.insert();
        }

        // 过滤密码
        user.setPassWord("");

        if (user != null) {
            //将从数据库查询的数据缓存到redis中
            stringRedisTemplate.opsForValue().set(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken, JsonUtils.objectToJson(user), userTokenSurvivalTime, TimeUnit.HOURS);
        }

        httpServletResponse.sendRedirect(webSiteUrl + "?token=" + accessToken);
    }

    private void updateUserPhoto(Map<String, Object> data, User user) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last("LIMIT 1");
        Map<String, Object> systemConfigMap = systemConfigService.getMap(queryWrapper);
        // 获取到头像，然后上传到自己服务器
        FileVO fileVO = new FileVO();
        fileVO.setAdminUid(SysConf.DEFAULT_UID);
        fileVO.setUserUid(SysConf.DEFAULT_UID);
        fileVO.setProjectName(SysConf.BLOG);
        fileVO.setSortName(SysConf.ADMIN);
        fileVO.setSystemConfig(systemConfigMap);
        List<String> urlList = new ArrayList<>();
        if (data.get(SysConf.AVATAR) != null) {
            urlList.add(data.get(SysConf.AVATAR).toString());
        } else if (data.get(SysConf.AVATAR_URL) != null) {
            urlList.add(data.get(SysConf.AVATAR_URL).toString());
        }
        fileVO.setUrlList(urlList);
        String res = this.pictureFeignClient.uploadPicsByUrl(fileVO);
        Map<String, Object> resultMap = JsonUtils.jsonToMap(res);
        if (resultMap.get(SysConf.CODE) != null && SysConf.SUCCESS.equals(resultMap.get(SysConf.CODE).toString())) {
            if (resultMap.get(SysConf.DATA) != null) {
                List<Map<String, Object>> listMap = (List<Map<String, Object>>) resultMap.get(SysConf.DATA);
                if (listMap != null && listMap.size() > 0) {
                    Map<String, Object> pictureMap = listMap.get(0);

                    String localPictureBaseUrl = systemConfigMap.get(SQLConf.LOCAL_PICTURE_BASE_URL).toString();
                    String qiNiuPictureBaseUrl = systemConfigMap.get(SQLConf.QI_NIU_PICTURE_BASE_URL).toString();
                    String picturePriority = systemConfigMap.get(SQLConf.PICTURE_PRIORITY).toString();

                    user.setAvatar(pictureMap.get(SysConf.UID).toString());

                    // 判断图片优先展示
                    if ("1".equals(picturePriority)) {
                        // 使用七牛云
                        if (pictureMap != null && pictureMap.get(SysConf.QI_NIU_URL) != null && pictureMap.get(SysConf.UID) != null) {
                            user.setPhotoUrl(qiNiuPictureBaseUrl + pictureMap.get(SysConf.QI_NIU_URL).toString());
                        }
                    } else {
                        // 使用自建图片服务器
                        if (pictureMap != null && pictureMap.get(SysConf.PIC_URL) != null && pictureMap.get(SysConf.UID) != null) {
                            user.setPhotoUrl(localPictureBaseUrl + pictureMap.get(SysConf.PIC_URL).toString());
                        }
                    }
                }
            }
        }
    }

    /**
     * 解析移动端数据
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "decryptData", notes = "QQ小程序登录数据解析")
    @PostMapping("/decryptData")
    public String decryptData(@RequestBody Map<String, String> map) {

        try {
            String encryptDataB64 = map.get("encryptDataB64");
            String jsCode = map.get("jsCode");
            String ivB64 = map.get("ivB64");

//            String params = "appid=" + APP_ID + "&secret=" + SECRET + "&js_code=" +  jsCode + "&grant_type=" + GRANT_TYPE;
//            String result = HttpRequestUtil.sendGet("https://api.q.qq.com/sns/jscode2session", params);

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("appid", APP_ID);
            paramMap.put("secret", SECRET);
            paramMap.put("js_code", jsCode);
            paramMap.put("grant_type", GRANT_TYPE);

            String result = HttpUtil.get("https://api.q.qq.com/sns/jscode2session", paramMap);

            log.error("获取UnionID");
            log.error(result);

            Map<String, Object> resultMap = JsonUtils.jsonToMap(result);

            if (resultMap != null) {
                String sessionKey = resultMap.get("session_key").toString();
                String userInfo = UniappUtils.decryptData(encryptDataB64, sessionKey, ivB64);
                log.error("解析加密数据");
                log.error(userInfo);
                Map<String, Object> userInfoMap = JsonUtils.jsonToMap(userInfo);

                Boolean exist = false;
                User user = null;
                //判断user是否存在
                if (userInfoMap.get(SysConf.OPEN_ID) != null) {
                    user = userService.getUserBySourceAnduuid("QQ", userInfoMap.get(SysConf.OPEN_ID).toString());
                    if (user != null) {
                        exist = true;
                    } else {
                        user = new User();
                    }
                } else {
                    return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
                }

                // 判断用户性别
                if (userInfoMap.get(SysConf.GENDER) != null) {
                    String genderStr = userInfoMap.get(SysConf.GENDER).toString();
                    String gender = Double.valueOf(genderStr).intValue() + "";
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
                    if (fileOldName.equals(userInfoMap.get(SysConf.AVATAR_URL)) || SysConf.BLOB.equals(fileOldName)) {
                        user.setPhotoUrl(photoList.get(0));
                    } else {
                        updateUserPhoto(userInfoMap, user);
                    }
                } else {
                    updateUserPhoto(userInfoMap, user);
                }

                if (userInfoMap.get(SysConf.NICK_NAME) != null) {
                    user.setNickName(userInfoMap.get(SysConf.NICK_NAME).toString());
                }

                if (user.getLoginCount() == null) {
                    user.setLoginCount(0);
                } else {
                    user.setLoginCount(user.getLoginCount() + 1);
                }

                // 获取浏览器，IP来源，以及操作系统
                user = userService.serRequestInfo(user);

                // 暂时将token也存入到user表中，为了以后方便更新redis中的内容
                String accessToken = StringUtils.getUUID();
                user.setValidCode(accessToken);

                if (exist) {
                    user.updateById();
                } else {

                    user.setUuid(userInfoMap.get(SysConf.OPEN_ID).toString());
                    user.setSource("QQ");

                    String userName = PROJECT_NAME_EN.concat("_").concat(user.getSource()).concat("_").concat(user.getUuid());
                    user.setUserName(userName);
                    // 如果昵称为空，那么直接设置用户名
                    if (StringUtils.isEmpty(user.getNickName())) {
                        user.setNickName(userName);
                    }
                    // 默认密码
                    user.setPassWord(MD5Utils.string2MD5(DEFAULE_PWD));
                    user.insert();
                }

                // 过滤密码
                user.setPassWord("");

                if (user != null) {
                    //将从数据库查询的数据缓存到redis中
                    stringRedisTemplate.opsForValue().set(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken, JsonUtils.objectToJson(user), userTokenSurvivalTime, TimeUnit.HOURS);
                }
                return ResultUtil.result(SysConf.SUCCESS, user);
            } else {
                return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
            }
        } catch (Exception e) {
            e.getStackTrace();
            return ResultUtil.result(SysConf.ERROR, MessageConf.SERVER_ERROR);
        }
    }

    @RequestMapping("/revoke/{source}/{token}")
    public Object revokeAuth(@PathVariable("source") String source, @PathVariable("token") String token) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    @RequestMapping("/refresh/{source}")
    public Object refreshAuth(@PathVariable("source") String source, String token) {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.refresh(AuthToken.builder().refreshToken(token).build());
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GetMapping("/verify/{accessToken}")
    public String verifyUser(@PathVariable("accessToken") String accessToken) {
        String userInfo = stringRedisTemplate.opsForValue().get(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken);
        if (StringUtils.isEmpty(userInfo)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        } else {
            Map<String, Object> map = JsonUtils.jsonToMap(userInfo);
            return ResultUtil.result(SysConf.SUCCESS, map);
        }
    }

    @ApiOperation(value = "删除accessToken", notes = "删除accessToken")
    @RequestMapping("/delete/{accessToken}")
    public String deleteUserAccessToken(@PathVariable("accessToken") String accessToken) {
        stringRedisTemplate.delete(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    /**
     * 通过token获取七牛云配置
     *
     * @param token
     * @return
     */
    @GetMapping("/getSystemConfig")
    public String getSystemConfig(@RequestParam("token") String token) {
        String userInfo = stringRedisTemplate.opsForValue().get(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + token);
        if (StringUtils.isEmpty(userInfo)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.last("LIMIT 1");
        SystemConfig SystemConfig = systemConfigService.getOne(queryWrapper);
        return ResultUtil.result(SysConf.SUCCESS, SystemConfig);
    }

    /**
     * 获取关于我的信息
     *
     * @author xzx19950624@qq.com
     * @date 2018年11月6日下午8:57:48
     */

    @ApiOperation(value = "编辑用户信息", notes = "编辑用户信息")
    @PostMapping("/editUser")
    public String editUser(HttpServletRequest request, @RequestBody UserVO userVO) {
        if (request.getAttribute(SysConf.USER_UID) == null || request.getAttribute(SysConf.TOKEN) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        String userUid = request.getAttribute(SysConf.USER_UID).toString();
        String token = request.getAttribute(SysConf.TOKEN).toString();
        User user = userService.getById(userUid);
        user.setNickName(userVO.getNickName());
        user.setAvatar(userVO.getAvatar());
        user.setBirthday(userVO.getBirthday());
        user.setSummary(userVO.getSummary());
        user.setGender(userVO.getGender());
        user.setQqNumber(userVO.getQqNumber());
        user.setOccupation(userVO.getOccupation());

        // 如果开启邮件通知，必须保证邮箱已存在
        if (userVO.getStartEmailNotification() == SysConf.ONE && !StringUtils.isNotEmpty(user.getEmail())) {
            return ResultUtil.result(SysConf.ERROR, "必须填写并绑定邮箱后，才能开启评论邮件通知~");
        }
        user.setStartEmailNotification(userVO.getStartEmailNotification());

        user.updateById();
        user.setPassWord("");
        user.setPhotoUrl(userVO.getPhotoUrl());

        // 判断用户是否更改了邮箱
        if (userVO.getEmail() != null && !userVO.getEmail().equals(user.getEmail())) {

            user.setEmail(userVO.getEmail());

            // 使用RabbitMQ发送邮件
            rabbitMqUtil.sendRegisterEmail(user, token);

            // 修改成功后，更新Redis中的用户信息
            stringRedisTemplate.opsForValue().set(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + token, JsonUtils.objectToJson(user), userTokenSurvivalTime, TimeUnit.HOURS);
            return ResultUtil.result(SysConf.SUCCESS, "您已修改邮箱，请先到邮箱进行确认绑定");
        } else {
            stringRedisTemplate.opsForValue().set(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + token, JsonUtils.objectToJson(user), userTokenSurvivalTime, TimeUnit.HOURS);
            return ResultUtil.result(SysConf.SUCCESS, "修改成功");
        }
    }

    @ApiOperation(value = "更新用户密码", notes = "更新用户密码")
    @PostMapping("/updateUserPwd")
    public String updateUserPwd(HttpServletRequest request, @RequestParam(value = "oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd) {
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(oldPwd)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        if (request.getAttribute(SysConf.USER_UID) == null || request.getAttribute(SysConf.TOKEN) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        String userUid = request.getAttribute(SysConf.USER_UID).toString();
        User user = userService.getById(userUid);
        // 判断是否是第三方登录的账号
        if (!user.getSource().equals(SysConf.MOGU)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.CANNOT_CHANGE_THE_PASSWORD_BY_USER);
        }
        // 判断旧密码是否一致
        if (user.getPassWord().equals(MD5Utils.string2MD5(oldPwd))) {
            user.setPassWord(MD5Utils.string2MD5(newPwd));
            user.updateById();
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
        }
        return ResultUtil.result(SysConf.ERROR, MessageConf.PASSWORD_IS_ERROR);
    }

    @ApiOperation(value = "申请友链", notes = "申请友链")
    @PostMapping("/replyBlogLink")
    public String replyBlogLink(HttpServletRequest request, @RequestBody LinkVO linkVO) {
        if (request.getAttribute(SysConf.USER_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        String userUid = request.getAttribute(SysConf.USER_UID).toString();

        User user = userService.getById(userUid);

        // 判断该用户是否被禁言，被禁言的用户，也无法进行友链申请操作
        if (user != null && user.getCommentStatus() == SysConf.ZERO) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.YOU_DONT_HAVE_PERMISSION_TO_REPLY);
        }

        // 判断是否开启邮件通知
        SystemConfig systemConfig = systemConfigService.getConfig();
        if (systemConfig != null && EOpenStatus.OPEN.equals(systemConfig.getStartEmailNotification())) {
            if (StringUtils.isNotEmpty(systemConfig.getEmail())) {
                log.info("发送友链申请邮件通知");
                String feedback = "收到新的友链申请: " + "<br />"
                        + "名称：" + linkVO.getTitle() + "<br />"
                        + "简介：" + linkVO.getSummary() + "<br />"
                        + "地址：" + linkVO.getUrl();
                rabbitMqUtil.sendSimpleEmail(systemConfig.getEmail(), feedback);
            } else {
                log.error("网站没有配置通知接收的邮箱地址！");
            }
        }

        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_UID, userUid);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.TITLE, linkVO.getTitle());
        queryWrapper.last("LIMIT 1");
        Link existLink = linkService.getOne(queryWrapper);

        if (existLink != null) {
            Integer linkStatus = existLink.getLinkStatus();
            String message = "";
            switch (linkStatus) {
                case 0: {
                    message = MessageConf.BLOG_LINK_IS_EXIST;
                }
                break;
                case 1: {
                    message = MessageConf.BLOG_LINK_IS_PUBLISH;
                }
                break;
                case 2: {
                    message = MessageConf.BLOG_LINK_IS_NO_PUBLISH;
                }
                break;
            }
            return ResultUtil.result(SysConf.ERROR, message);
        }

        Link link = new Link();
        link.setLinkStatus(ELinkStatus.APPLY);
        link.setTitle(linkVO.getTitle());
        link.setSummary(linkVO.getSummary());
        link.setUrl(linkVO.getUrl());
        link.setClickCount(0);
        link.setSort(0);
        link.setStatus(EStatus.ENABLE);
        link.setUserUid(userUid);
        link.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);

    }

    @ApiOperation(value = "获取用户反馈", notes = "获取用户反馈")
    @GetMapping("/getFeedbackList")
    public String getFeedbackList(HttpServletRequest request) {
        if (request.getAttribute(SysConf.USER_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        String userUid = request.getAttribute(SysConf.USER_UID).toString();

        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_UID, userUid);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        Page<Feedback> page = new Page<>();
        page.setSize(20);
        page.setCurrent(1);
        IPage<Feedback> pageList = feedbackService.page(page, queryWrapper);
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @ApiOperation(value = "提交反馈", notes = "提交反馈", response = String.class)
    @PostMapping("/addFeedback")
    public String edit(HttpServletRequest request, @Validated({Insert.class}) @RequestBody FeedbackVO feedbackVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        if (request.getAttribute(SysConf.USER_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }

        String userUid = request.getAttribute(SysConf.USER_UID).toString();
        User user = userService.getById(userUid);

        // 判断该用户是否被禁言，被禁言的用户，也无法进行反馈操作
        if (user != null && user.getCommentStatus() == SysConf.ZERO) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.YOU_DONT_HAVE_PERMISSION_TO_FEEDBACK);
        }

        // 判断是否开启邮件通知
        SystemConfig systemConfig = systemConfigService.getConfig();
        if (systemConfig != null && EOpenStatus.OPEN.equals(systemConfig.getStartEmailNotification())) {
            if (StringUtils.isNotEmpty(systemConfig.getEmail())) {
                log.info("发送反馈邮件通知");
                String feedback = "网站收到新的反馈: " + "<br />"
                        + "标题：" + feedbackVO.getTitle() + "<br />" + "<br />"
                        + "内容" + feedbackVO.getContent();
                rabbitMqUtil.sendSimpleEmail(systemConfig.getEmail(), feedback);
            } else {
                log.error("网站没有配置通知接收的邮箱地址！");
            }
        }

        Feedback feedback = new Feedback();
        feedback.setUserUid(userUid);
        feedback.setTitle(feedbackVO.getTitle());
        feedback.setContent(feedbackVO.getContent());

        // 设置反馈已开启
        feedback.setFeedbackStatus(0);
        feedback.setReply(feedbackVO.getReply());
        feedback.setUpdateTime(new Date());
        feedback.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @ApiOperation(value = "绑定用户邮箱", notes = "绑定用户邮箱")
    @GetMapping("/bindUserEmail/{token}/{code}")
    public String bindUserEmail(@PathVariable("token") String token, @PathVariable("code") String code) {

        String userInfo = stringRedisTemplate.opsForValue().get(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + token);
        if (StringUtils.isEmpty(userInfo)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        User user = JsonUtils.jsonToPojo(userInfo, User.class);
        user.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 鉴权
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case SysConf.GITHUB:
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubClienId)
                        .clientSecret(githubClientSecret)
                        .redirectUri(moguWebUrl + "/oauth/callback/github")
                        .build());
                break;
            case SysConf.GITEE:
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeClienId)
                        .clientSecret(giteeClientSecret)
                        .redirectUri(moguWebUrl + "/oauth/callback/gitee")
                        .build());
                break;
            case SysConf.QQ:
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId(qqClienId)
                        .clientSecret(qqClientSecret)
                        .redirectUri(moguWebUrl + "/oauth/callback/qq")
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException(MessageConf.OPERATION_FAIL);
        }
        return authRequest;
    }
}
