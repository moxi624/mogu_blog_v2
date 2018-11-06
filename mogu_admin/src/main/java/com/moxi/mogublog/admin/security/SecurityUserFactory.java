package com.moxi.mogublog.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.config.security.SecurityUser;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.AdminRole;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.AdminRoleService;
import com.moxi.mogublog.xo.service.RoleService;

public final class SecurityUserFactory {
	
	@Autowired
	private static AdminRoleService adminRoleService;
	
	@Autowired
	private static RoleService roleService;
	
	private SecurityUserFactory() {
    }
	
	public static List<String> getRolesByAdmin(Admin admin){
	//根据admin获取账户拥有的角色uid集合
	    QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
	    wrapper.eq(SQLConf.ADMINUID,admin.getUid());
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
		return roleNames;
    }
	
    public static SecurityUser create(Admin admin) {
    	boolean enabled = (admin.getStatus()==1)?true:false;
    	return new SecurityUser(
                admin.getUid(),
                admin.getUserName(),
                admin.getPassWord(),
                enabled,
                getAuthorities(admin)
        );
    }
 
    private static Collection<? extends GrantedAuthority> getAuthorities(Admin admin) {
    	Collection<GrantedAuthority> authorities  = new ArrayList<GrantedAuthority>();
    	List<String> roleNames = getRolesByAdmin(admin);
    	for (String roleName : roleNames) {
    		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
    		authorities.add(authority);
		}
		return authorities;
    }

}
