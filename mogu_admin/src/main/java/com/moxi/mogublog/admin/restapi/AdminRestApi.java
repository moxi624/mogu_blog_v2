package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.RedisConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.security.AuthorityVerify;
import com.moxi.mogublog.commons.entity.Admin;
import com.moxi.mogublog.commons.entity.Role;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.CheckUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.RoleService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 管理员表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/admin")
@Api(value = "管理员RestApi", tags = {"AdminRestApi"})
@Slf4j
public class AdminRestApi {

    @Autowired
    WebUtil webUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Value(value = "${DEFAULE_PWD}")
    private String DEFAULE_PWD;

    @AuthorityVerify
    @ApiOperation(value = "获取管理员列表", notes = "获取管理员列表")
    @GetMapping("/getList")
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        String pictureResult = null;
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.like(SQLConf.USER_NAME, keyword).or().like(SQLConf.NICK_NAME, keyword.trim());
        }
        Page<Admin> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        // 去除密码
        queryWrapper.select(Admin.class, i -> !i.getProperty().equals(SQLConf.PASS_WORD));
        IPage<Admin> pageList = adminService.page(page, queryWrapper);
        List<Admin> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });

        Map<String, String> pictureMap = new HashMap<>();

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        for (Admin item : list) {

            Role role = roleService.getById(item.getRoleUid());

            item.setRole(role);

            //获取图片
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getAvatar(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();
                pictureUidsTemp.forEach(picture -> {
                    if (pictureMap.get(picture) != null && pictureMap.get(picture) != "") {
                        pictureListTemp.add(pictureMap.get(picture));
                    }
                });
                item.setPhotoList(pictureListTemp);
            }
        }

        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @AuthorityVerify
    @OperationLogger(value = "重置用户密码")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @PostMapping("/restPwd")
    public String restPwd(HttpServletRequest request,
                          @ApiParam(name = "uid", value = "管理员uid", required = true) @RequestParam(name = "uid", required = false) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }

        Admin admin = adminService.getById(uid);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassWord(encoder.encode(DEFAULE_PWD));
        admin.setUpdateTime(new Date());
        admin.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @AuthorityVerify
    @OperationLogger(value = "注册管理员")
    @ApiOperation(value = "注册管理员", notes = "注册管理员")
    @PostMapping("/add")
    public String add(HttpServletRequest request,
                      @ApiParam(name = "assignbody", value = "管理员注册对象", required = true) @RequestBody(required = true) Admin registered) {

        String mobile = registered.getMobile();
        String userName = registered.getUserName();
        String email = registered.getEmail();

        if (StringUtils.isEmpty(userName)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }

        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
            return ResultUtil.result(SysConf.ERROR, "邮箱和手机号至少一项不能为空");
        }

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, userName);
        Admin admin = adminService.getOne(queryWrapper);

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        if (admin == null) {
//            if (StringUtils.isNotEmpty(email)) {
//                wrapper.eq(SQLConf.EMAIL, email);
//            } else {
//                wrapper.eq(SQLConf.MOBILE, mobile);
//            }
//
//            if (adminService.getOne(wrapper) != null) {
//                return ResultUtil.result(SysConf.ERROR, "管理员账户已存在");
//            }

            // 设置为未审核状态
            registered.setStatus(EStatus.ENABLE);

            PasswordEncoder encoder = new BCryptPasswordEncoder();

            //设置默认密码
            registered.setPassWord(encoder.encode(DEFAULE_PWD));

            adminService.save(registered);

            //这里需要通过SMS模块，发送邮件告诉初始密码

            return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
        }
        return ResultUtil.result(SysConf.ERROR, "管理员账户已存在");
    }

    @AuthorityVerify
    @OperationLogger(value = "更新管理员基本信息")
    @ApiOperation(value = "更新管理员基本信息", notes = "更新管理员基本信息")
    @PostMapping("/edit")
    public String edit(HttpServletRequest request,
                       @ApiParam(name = "updateBody", value = "管理员对象", required = true) @RequestBody(required = true) Admin updateBody) {

        if (StringUtils.isEmpty(updateBody.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        Admin admin = adminService.getById(updateBody.getUid());
        if (admin != null) {

            //判断修改的对象是否是超级管理员，超级管理员不能修改用户名
            if (admin.getUserName().equals(SysConf.ADMIN) && !updateBody.getUserName().equals(SysConf.ADMIN)) {
                return ResultUtil.result(SysConf.ERROR, "超级管理员用户名必须为admin");
            }
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.eq(SQLConf.USER_NAME, updateBody.getUserName());
            List<Admin> adminList = adminService.list(queryWrapper);
            if (adminList != null) {
                for (Admin item : adminList) {
                    if (item.getUid().equals(updateBody.getUid())) {
                        continue;
                    } else {
                        return ResultUtil.result(SysConf.ERROR, "修改失败，用户名存在");
                    }
                }
            }
        }
        updateBody.setPassWord(null);
        updateBody.setUpdateTime(new Date());
        updateBody.updateById();

        // 修改成功后，判断是否更改了RoleUid，更新redis中admin的URL访问路径
        if (!admin.getRoleUid().equals(updateBody.getRoleUid())) {
            redisUtil.delete(RedisConf.ADMIN_VISIT_MENU + RedisConf.SEGMENTATION + admin.getUid());
        }

        return ResultUtil.result(SysConf.SUCCESS, "更新管理员成功");
    }

    @AuthorityVerify
    @OperationLogger(value = "更新管理员邮箱或手机号")
    @ApiOperation(value = "更新管理员邮箱或手机号", notes = "更新管理员邮箱或手机号")
    @PostMapping("/updateEmail")
    public String updateEmail(HttpServletRequest request,
                              @ApiParam(name = "newInfo", value = "管理员uid", required = true) @RequestParam(name = "adminUid", required = true) String adminUid,
                              @ApiParam(name = "newInfo", value = "管理员新邮箱或新手机号", required = true) @RequestParam(name = "newInfo", required = true) String newInfo,
                              @ApiParam(name = "validCode", value = "验验码", required = true) @RequestParam(name = "validCode", required = true) String validCode) {


        Admin admin = adminService.getById(adminUid);
        if (admin == null) {
            return ResultUtil.result(SysConf.ERROR, "管理员不存在");
        }

        //从redis中获取验证码
        String checkValidCode = stringRedisTemplate.opsForValue().get(newInfo);
        if (checkValidCode.isEmpty()) {
            return ResultUtil.result(SysConf.ERROR, "验证码已过期");
        }
        if (!checkValidCode.equals(validCode)) {
            return ResultUtil.result(SysConf.ERROR, "验证码不正确");
        }
        if (checkValidCode.equals(validCode)) {
            if (CheckUtils.checkEmail(newInfo)) {
                admin.setEmail(newInfo);
            } else if (CheckUtils.checkMobileNumber(newInfo)) {
                admin.setMobile(newInfo);
            } else {
                return ResultUtil.result(SysConf.ERROR, "输入的邮箱或手机号格式有误");
            }
            admin.setUpdateTime(new Date());
            adminService.updateById(admin);
            //删除缓存中的验证码
            stringRedisTemplate.delete(newInfo);
            return ResultUtil.result(SysConf.SUCCESS, "更新成功");
        }
        return ResultUtil.result(SysConf.ERROR, "验证码错误");
    }

    @AuthorityVerify
    @OperationLogger(value = "删除部分管理员信息")
    @ApiOperation(value = "删除部分管理员信息", notes = "删除部分管理员信息")
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "adminUids", value = "管理员uid集合", required = true) @RequestParam(name = "adminUids", required = true) List<String> adminUids) {

        if (adminUids.isEmpty()) {
            return ResultUtil.result(SysConf.ERROR, "管理员uid不能为空");
        }
        Collection<Admin> adminList = adminService.listByIds(adminUids);

        for (Admin admin : adminList) {
            if (SysConf.ADMIN.equals(admin.getUserName())) {
                return ResultUtil.result(SysConf.ERROR, "无法删除Admin账号");
            }
            admin.setUpdateTime(new Date());
            admin.setStatus(EStatus.DISABLED);
        }
        adminService.updateBatchById(adminList);
        return ResultUtil.result(SysConf.SUCCESS, "删除管理员成功");
    }

    @OperationLogger(value = "分配用户角色信息列表")
    @ApiOperation(value = "分配用户角色信息列表", notes = "分配用户角色信息列表")
    @PostMapping("/assign")
    public String assign(HttpServletRequest request,
                         @ApiParam(name = "adminUid", value = "管理员uid", required = true) @RequestParam(name = "adminUid", required = true) String adminUid) {

        Map<String, Object> map = new HashMap<>();

        Admin admin = adminService.getById(adminUid);
        map.put("admin", admin);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        List<Role> roles = roleService.list(queryWrapper);

        List<Role> assignedRoles = new ArrayList<>();
        List<Role> unassignRoles = new ArrayList<>();

        //根据admin获取账户拥有的角色uid集合
        List<String> roleUids = new ArrayList<>();
        roleUids.add(admin.getRoleUid());
        for (Role role : roles) {
            if (roleUids.contains(role.getUid())) {
                assignedRoles.add(role);
            } else {
                unassignRoles.add(role);
            }
        }
        map.put("assignedRoles", assignedRoles);
        map.put("unassignRoles", unassignRoles);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

}

