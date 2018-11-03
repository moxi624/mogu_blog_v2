package com.moxi.mogublog.xo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.entity.RolePermission;
import com.moxi.mogublog.xo.mapper.RolePermissionMapper;
import com.moxi.mogublog.xo.service.RolePermissionService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;


/**
 * <p>
 * 角色权限关系表 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Service
public class RolePermissionServiceImpl extends SuperServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService  {

	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Override
	public List<String> queryPermissionUidsByRoleUid(String uid) {
		return rolePermissionMapper.queryPermissionUidsByRoleUid(uid);
	}

}
