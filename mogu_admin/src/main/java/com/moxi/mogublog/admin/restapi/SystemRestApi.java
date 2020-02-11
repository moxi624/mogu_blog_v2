package com.moxi.mogublog.admin.restapi;

import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
 * 系统设置RestApi
 *
 * @author xzx19950624@qq.com
 * @date 2018年11月6日下午8:23:36
 */

@RestController
@RequestMapping("/system")
@Api(value = "系统设置RestApi", tags = {"SystemRestApi"})
@Slf4j
public class SystemRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    AdminService adminService;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    /**
     * 获取关于我的信息
     *
     * @author xzx19950624@qq.com
     * @date 2018年11月6日下午8:57:48
     */

    @ApiOperation(value = "获取我的信息", notes = "获取我的信息")
    @GetMapping("/getMe")
    public String getMe(HttpServletRequest request) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null || request.getAttribute(SysConf.ADMIN_UID) == "") {
            return ResultUtil.result(SysConf.ERROR, "登录失效，请重新登录");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        //清空密码，防止泄露
        admin.setPassWord(null);

        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), ",");
            admin.setPhotoList(webUtils.getPicture(pictureList));
        }

        return ResultUtil.result(SysConf.SUCCESS, admin);
    }

    @OperationLogger(value = "编辑我的信息")
    @ApiOperation(value = "编辑我的信息", notes = "获取我的信息")
    @PostMapping("/editMe")
    public String editMe(HttpServletRequest request, @RequestBody Admin admin) {

        Boolean save = adminService.updateById(admin);

        return ResultUtil.result(SysConf.SUCCESS, save);
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping("/changePwd")
    public String changePwd(HttpServletRequest request,
                            @ApiParam(name = "oldPwd", value = "旧密码", required = false) @RequestParam(name = "oldPwd", required = false) String oldPwd,
                            @ApiParam(name = "newPwd", value = "新密码", required = false) @RequestParam(name = "newPwd", required = false) String newPwd) throws NoSuchAlgorithmException {

        if (request.getAttribute(SysConf.ADMIN_UID) == null || request.getAttribute(SysConf.ADMIN_UID) == "") {
            return ResultUtil.result(SysConf.ERROR, "登录失效，请重新登录");
        }
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean isPassword = encoder.matches(oldPwd, admin.getPassWord());

        if (isPassword) {
            admin.setPassWord(encoder.encode(newPwd));
            admin.updateById();
            return ResultUtil.result(SysConf.SUCCESS, "修改成功");
        } else {
            return ResultUtil.result(SysConf.ERROR, "输入密码错误");
        }

    }

}
