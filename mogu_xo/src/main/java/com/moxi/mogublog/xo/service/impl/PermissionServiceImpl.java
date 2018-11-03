package com.moxi.mogublog.xo.service.impl;

import org.springframework.stereotype.Service;
import com.moxi.mogublog.xo.entity.Permission;
import com.moxi.mogublog.xo.mapper.PermissionMapper;
import com.moxi.mogublog.xo.service.PermissionService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;


/**
 * <p>
 * 权限员表 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Service
public class PermissionServiceImpl extends SuperServiceImpl<PermissionMapper, Permission> implements PermissionService {
	
}
