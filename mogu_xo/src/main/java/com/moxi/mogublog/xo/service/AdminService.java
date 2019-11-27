package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
public interface AdminService extends SuperService<Admin> {

    public Admin getAdminByUid(String uid);
}
