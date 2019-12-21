package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.mapper.RoleMapper;
import com.moxi.mogublog.xo.service.RoleService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Service
public class RoleServiceImpl extends SuperServiceImpl<RoleMapper, Role> implements RoleService {

}
