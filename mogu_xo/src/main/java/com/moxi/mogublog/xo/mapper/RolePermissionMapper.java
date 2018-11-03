package com.moxi.mogublog.xo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;

import com.moxi.mogublog.xo.entity.RolePermission;
import com.moxi.mougblog.base.mapper.SuperMapper;


/**
 * <p>
 * 角色权限关系表 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
public interface RolePermissionMapper extends SuperMapper<RolePermission>{
	
	@Select("select permission_uid from t_role_permission where role_uid = #{uid}")
	List<String> queryPermissionUidsByRoleUid(String uid);

}
