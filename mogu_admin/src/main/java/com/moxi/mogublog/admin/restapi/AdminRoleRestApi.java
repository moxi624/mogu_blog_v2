package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.AdminRole;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.AdminRoleService;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.RoleService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 权限表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年12月5日18:36:44
 */
@RestController
@Api(value = "权限RestApi", tags = {"LinkRestApi"})
@RequestMapping("/adminRole")
public class AdminRoleRestApi {

    private static Logger log = LogManager.getLogger(AdminRestApi.class);
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;

    @ApiOperation(value = "获取权限列表", notes = "获取权限列表", response = String.class)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "adminUid", value = "管理员UID", required = false) @RequestParam(name = "adminUid", required = false) String adminUid,
                          @ApiParam(name = "roleUid", value = "角色UID", required = false) @RequestParam(name = "roleUid", required = false) String roleUid,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminUid)) {
            queryWrapper.like(SQLConf.ADMINUID, adminUid);
        }
        if (!StringUtils.isEmpty(roleUid)) {
            queryWrapper.like(SQLConf.ROLEUID, roleUid);
        }
        Page<AdminRole> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<AdminRole> pageList = adminRoleService.page(page, queryWrapper);
        List<AdminRole> adminRoleList = pageList.getRecords();
        List<String> adminUids = new ArrayList<>();
        List<String> roleUids = new ArrayList<>();
        adminRoleList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAdminUid())) {
                adminUids.add(item.getAdminUid());
            }
            if (StringUtils.isNotEmpty(item.getRoleUid())) {
                roleUids.add(item.getRoleUid());
            }
        });


        Collection<Admin> adminList = new ArrayList<>();
        Collection<Role> roleList = new ArrayList<>();
        if (adminUids.size() > 0) {
            adminList = adminService.listByIds(adminUids);
        }
        if (roleUids.size() > 0) {
            roleList = roleService.listByIds(roleUids);
        }

        Map<String, Admin> adminMap = new HashMap<>();
        Map<String, Role> roleMap = new HashMap<>();

        adminList.forEach(item -> {
            //隐藏管理员的密码
            item.setPassWord("");
            adminMap.put(item.getUid(), item);
        });
        roleList.forEach(item -> {
            roleMap.put(item.getUid(), item);
        });

        adminRoleList.forEach(item -> {

            if (StringUtils.isNotEmpty(item.getAdminUid()) && adminMap.get(item.getAdminUid()) != null) {
                item.setAdmin(adminMap.get(item.getAdminUid()));
            }
            if (StringUtils.isNotEmpty(item.getRoleUid()) && roleMap.get(item.getRoleUid()) != null) {
                item.setRole(roleMap.get(item.getRoleUid()));
            }
        });
        pageList.setRecords(adminRoleList);

        log.info("返回结果");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @OperationLogger(value = "增加权限")
    @ApiOperation(value = "增加权限", notes = "增加权限", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request,
                      @ApiParam(name = "adminRole", value = "管理员角色表", required = false) @RequestBody(required = false) AdminRole adminRole) {

        if (StringUtils.isEmpty(adminRole.getAdminUid()) || StringUtils.isEmpty(adminRole.getRoleUid())) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        adminRole.insert();
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @OperationLogger(value = "编辑权限")
    @ApiOperation(value = "编辑权限", notes = "编辑权限", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request,
                       @ApiParam(name = "adminRole", value = "管理员角色表", required = true) @RequestBody(required = true) AdminRole adminRole) {

        if (StringUtils.isEmpty(adminRole.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        Admin admin = adminRole.getAdmin();
        String userName = admin.getUserName();
        String adminUid = String.valueOf(request.getAttribute("adminUid"));
        if (userName.equals(SysConf.ADMIN) && !adminUid.equals(admin.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "您无法修改admin权限");
        }
        adminRole.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
    }

    @OperationLogger(value = "删除权限")
    @ApiOperation(value = "删除权限", notes = "删除权限", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        AdminRole adminRole = adminRoleService.getById(uid);
        adminRole.setStatus(EStatus.DISABLED);
        adminRole.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }

}

