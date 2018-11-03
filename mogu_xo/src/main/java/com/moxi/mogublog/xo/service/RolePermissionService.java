package com.moxi.mogublog.xo.service;

import java.util.List;
import com.moxi.mogublog.xo.entity.RolePermission;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 角色权限关系表 服务类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
public interface RolePermissionService extends SuperService<RolePermission>{

	public List<String> queryPermissionUidsByRoleUid(String uid);

}
