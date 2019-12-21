package com.moxi.mogublog.admin.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.AdminRole;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.AdminRoleService;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
        queryWrapper.eq(SQLConf.USER_NAME, username);
        Admin admin = adminService.getOne(queryWrapper);

        if (admin == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            //查询出角色信息封装到admin中
            QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
            wrapper.eq(SQLConf.ADMINUID, admin.getUid());
            List<AdminRole> adminRoleList = adminRoleService.list(wrapper);
            List<String> roleUids = new ArrayList<>();
            for (AdminRole adminRole : adminRoleList) {
                String roleUid = adminRole.getRoleUid();
                roleUids.add(roleUid);
            }
            List<Role> roles = (List<Role>) roleService.listByIds(roleUids);
            List<String> roleNames = new ArrayList<String>();
            for (Role role : roles) {
                roleNames.add(role.getRoleName());
            }
            admin.setRoleNames(roleNames);

            return SecurityUserFactory.create(admin);
        }
    }
}
