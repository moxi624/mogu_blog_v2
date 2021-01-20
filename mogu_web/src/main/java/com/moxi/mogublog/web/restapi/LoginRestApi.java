package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.web.global.MessageConf;
import com.moxi.mogublog.web.global.RedisConf;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mogublog.xo.utils.RabbitMqUtil;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.UserVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.validator.group.GetOne;
import com.moxi.mougblog.base.validator.group.Insert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录RestApi，系统自带的登录注册功能
 * 第三方登录请移步AuthRestApi
 *
 * @author 陌溪
 * @date 2020年5月6日17:50:23
 */
@RestController
@RefreshScope
@RequestMapping("/login")
@Api(value = "登录管理相关接口", tags = {"登录管理相关接口"})
@Slf4j
public class LoginRestApi {

    @Autowired
    private RabbitMqUtil rabbitMqUtil;
    @Autowired
    private WebConfigService webConfigService;
    @Resource
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Value(value = "${BLOG.USER_TOKEN_SURVIVAL_TIME}")
    private Long userTokenSurvivalTime;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public String login(@Validated({GetOne.class}) @RequestBody UserVO userVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        Boolean isOpenLoginType = webConfigService.isOpenLoginType(RedisConf.PASSWORD);
        if (!isOpenLoginType) {
            return ResultUtil.result(SysConf.ERROR, "后台未开启该登录方式!");
        }
        String userName = userVO.getUserName();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(SQLConf.USER_NAME, userName).or().eq(SQLConf.EMAIL, userName));
        queryWrapper.last(SysConf.LIMIT_ONE);
        User user = userService.getOne(queryWrapper);
        if (user == null || EStatus.DISABLED == user.getStatus()) {
            return ResultUtil.result(SysConf.ERROR, "用户不存在");
        }
        if (EStatus.FREEZE == user.getStatus()) {
            return ResultUtil.result(SysConf.ERROR, "用户账号未激活");
        }
        if (StringUtils.isNotEmpty(user.getPassWord()) && user.getPassWord().equals(MD5Utils.string2MD5(userVO.getPassWord()))) {
            // 更新登录信息
            HttpServletRequest request = RequestHolder.getRequest();
            String ip = IpUtils.getIpAddr(request);
            Map<String, String> userMap = IpUtils.getOsAndBrowserInfo(request);
            user.setBrowser(userMap.get(SysConf.BROWSER));
            user.setOs(userMap.get(SysConf.OS));
            user.setLastLoginIp(ip);
            user.setLastLoginTime(new Date());
            user.updateById();
            // 获取用户头像
            if (!StringUtils.isEmpty(user.getAvatar())) {
                String avatarResult = pictureFeignClient.getPicture(user.getAvatar(), ",");
                List<String> picList = webUtil.getPicture(avatarResult);
                if (picList != null && picList.size() > 0) {
                    user.setPhotoUrl(webUtil.getPicture(avatarResult).get(0));
                }
            }
            // 生成token
            String token = StringUtils.getUUID();
            // 过滤密码
            user.setPassWord("");
            //将从数据库查询的数据缓存到redis中
            redisUtil.setEx(RedisConf.USER_TOKEN + Constants.SYMBOL_COLON + token, JsonUtils.objectToJson(user), userTokenSurvivalTime, TimeUnit.HOURS);
            log.info("登录成功，返回token: ", token);
            return ResultUtil.result(SysConf.SUCCESS, token);
        } else {
            return ResultUtil.result(SysConf.ERROR, "账号或密码错误");
        }
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping("/register")
    public String register(@Validated({Insert.class}) @RequestBody UserVO userVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        // 判断是否开启登录方式
        Boolean isOpenLoginType = webConfigService.isOpenLoginType(RedisConf.PASSWORD);
        if (!isOpenLoginType) {
            return ResultUtil.result(SysConf.ERROR, "后台未开启注册功能!");
        }
        if (userVO.getUserName().length() < Constants.NUM_FIVE || userVO.getUserName().length() >= Constants.NUM_TWENTY || userVO.getPassWord().length() < Constants.NUM_FIVE || userVO.getPassWord().length() >= Constants.NUM_TWENTY) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        HttpServletRequest request = RequestHolder.getRequest();
        String ip = IpUtils.getIpAddr(request);
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(SQLConf.USER_NAME, userVO.getUserName()).or().eq(SQLConf.EMAIL, userVO.getEmail()));
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        queryWrapper.last(SysConf.LIMIT_ONE);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.USER_OR_EMAIL_EXIST);
        }
        user = new User();
        user.setUserName(userVO.getUserName());
        user.setNickName(userVO.getNickName());
        user.setPassWord(MD5Utils.string2MD5(userVO.getPassWord()));
        user.setEmail(userVO.getEmail());
        // 设置账号来源，蘑菇博客
        user.setSource(SysConf.MOGU);
        user.setLastLoginIp(ip);
        user.setBrowser(map.get(SysConf.BROWSER));
        user.setOs(map.get(SysConf.OS));
        user.setStatus(EStatus.FREEZE);
        user.insert();

        // 生成随机激活的token
        String token = StringUtils.getUUID();

        // 过滤密码
        user.setPassWord("");

        //将从数据库查询的数据缓存到redis中，用于用户邮箱激活，1小时后过期
        redisUtil.setEx(RedisConf.ACTIVATE_USER + RedisConf.SEGMENTATION + token, JsonUtils.objectToJson(user), 1, TimeUnit.HOURS);

        // 发送邮件，进行账号激活
        rabbitMqUtil.sendActivateEmail(user, token);

        return ResultUtil.result(SysConf.SUCCESS, "注册成功，请登录邮箱进行账号激活");
    }

    @ApiOperation(value = "激活用户账号", notes = "激活用户账号")
    @GetMapping("/activeUser/{token}")
    public String bindUserEmail(@PathVariable("token") String token) {
        // 从redis中获取用户信息
        String userInfo = redisUtil.get(RedisConf.ACTIVATE_USER + RedisConf.SEGMENTATION + token);
        if (StringUtils.isEmpty(userInfo)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        User user = JsonUtils.jsonToPojo(userInfo, User.class);
        if (EStatus.FREEZE != user.getStatus()) {
            return ResultUtil.result(SysConf.ERROR, "用户账号已经被激活");
        }
        user.setStatus(EStatus.ENABLE);
        user.updateById();

        // 更新成功后，需要把该用户名下其它未激活的用户删除【删除】
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, user.getUserName());
        queryWrapper.ne(SQLConf.UID, user.getUid());
        queryWrapper.ne(SQLConf.STATUS, EStatus.ENABLE);
        List<User> userList = userService.list(queryWrapper);
        if (userList.size() > 0) {
            List<String> uidList = new ArrayList<>();
            userList.forEach(item -> {
                uidList.add(item.getUid());
            });
            // 移除所有未激活的用户【该用户名下的】
            userService.removeByIds(uidList);
        }

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public String logout(@ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {
        if (StringUtils.isEmpty(token)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        redisUtil.set(RedisConf.USER_TOKEN + Constants.SYMBOL_COLON + token, "");
        return ResultUtil.result(SysConf.SUCCESS, "退出成功");
    }

}
