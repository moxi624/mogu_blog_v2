package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.mapper.AdminMapper;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Service
public class AdminServiceImpl extends SuperServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin getAdminByUid(String uid) {
        return adminMapper.getAdminByUid(uid);
    }
}
