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
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.RoleService;
import com.moxi.mogublog.xo.vo.RoleVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * <p>
 * 角色表 RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@RestController
@RequestMapping("/role")
@Api(value = "角色管理RestApi", tags = {"RoleRestApi"})
@Slf4j
public class RoleRestApi {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @AuthorityVerify
    @ApiOperation(value = "获取角色信息列表", notes = "获取角色信息列表")
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody RoleVO roleVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        if (StringUtils.isNotEmpty(roleVO.getKeyword()) && StringUtils.isNotEmpty(roleVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.ROLENAEM, roleVO.getKeyword().trim());
        }
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Page<Role> page = new Page<>();
        page.setCurrent(roleVO.getCurrentPage());
        page.setSize(roleVO.getPageSize());
        IPage<Role> pageList = roleService.page(page, queryWrapper);
        log.info("获取角色信息列表");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @AuthorityVerify
    @OperationLogger(value = "新增角色信息")
    @ApiOperation(value = "新增角色信息", notes = "新增角色信息")
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody RoleVO roleVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String roleName = roleVO.getRoleName();
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.ROLENAEM, roleName);
        Role getRole = roleService.getOne(queryWrapper);
        if (getRole == null) {
            Role role = new Role();
            role.setRoleName(roleVO.getRoleName());
            role.setCategoryMenuUids(roleVO.getCategoryMenuUids());
            role.setSummary(roleVO.getSummary());
            role.insert();
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
        }
        return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
    }

    @AuthorityVerify
    @OperationLogger(value = "更新角色信息")
    @ApiOperation(value = "更新角色信息", notes = "更新角色信息")
    @PostMapping("/edit")
    public String update(@Validated({Update.class}) @RequestBody RoleVO roleVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        String uid = roleVO.getUid();
        Role getRole = roleService.getById(uid);
        if (getRole == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        getRole.setRoleName(roleVO.getRoleName());
        getRole.setCategoryMenuUids(roleVO.getCategoryMenuUids());
        getRole.setSummary(roleVO.getSummary());
        getRole.updateById();

        // 修改成功后，需要删除redis中所有的admin访问路径
        deleteAdminVisitUrl();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);

    }

    @AuthorityVerify
    @OperationLogger(value = "删除角色信息")
    @ApiOperation(value = "删除角色信息", notes = "删除角色信息")
    @PostMapping("/delete")
    public String delete(@Validated({Delete.class}) @RequestBody RoleVO roleVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        // 判断该角色下是否绑定了管理员
        QueryWrapper<Admin> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        blogQueryWrapper.in(SQLConf.ROLEUID, roleVO.getUid());
        Integer adminCount = adminService.count(blogQueryWrapper);
        if (adminCount > 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ADMIN_UNDER_THIS_ROLE);
        }

        Role role = roleService.getById(roleVO.getUid());
        role.setStatus(EStatus.DISABLED);
        role.updateById();

        deleteAdminVisitUrl();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    /**
     * 删除Redis中管理员的访问路径
     */
    private void deleteAdminVisitUrl() {
        Set<String> keys = redisUtil.keys(RedisConf.ADMIN_VISIT_MENU + "*");
        redisUtil.delete(keys);
    }

}