package com.moxi.mogublog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.vo.UserVO;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.GetOne;
import com.moxi.mougblog.base.validator.group.Insert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户RestApi
 * </p>
 *
 * @author 陌溪
 * @since 2019年12月14日12:57:18
 */
@RestController
@RequestMapping("/user")
@Api(value = "登录管理RestApi", tags = {"loginRestApi"})
@Slf4j
public class UserRestApi {


    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public String login(@Validated({GetOne.class}) @RequestBody UserVO userVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, "我登录啦" + userVO.getEmail());
    }

    @ApiOperation(value = "用户注册", notes = "用户登录")
    @PostMapping("/register")
    public String register(@Validated({Insert.class}) @RequestBody UserVO userVO, BindingResult result) {

        ThrowableUtils.checkParamArgument(result);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, userVO.getUserName());
        User user = userService.getOne(queryWrapper);

        if (user != null) {
            return ResultUtil.result(SysConf.ERROR, "用户已存在");
        }
        user = new User();
        user.setUserName(userVO.getUserName());
        user.setPassWord(userVO.getPassWord());
        user.setEmail(userVO.getEmail());
        return ResultUtil.result(SysConf.SUCCESS, "注册成功");
    }


    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public String logout(@ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {
        String destroyToken = null;
        return ResultUtil.result(SysConf.SUCCESS, destroyToken);
    }

}
