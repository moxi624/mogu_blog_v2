package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.commons.entity.Admin;
import com.moxi.mogublog.xo.vo.AdminVO;
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

    /**
     * 通过UID获取Admin
     *
     * @param uid
     * @return
     */
    public Admin getAdminByUid(String uid);

    /**
     * Web端通过用户名获取一个Admin
     * @param userName
     * @return
     */
    public Admin getAdminByUser(String userName);


    /**
     * 获取当前管理员
     *
     * @return
     */
    public Admin getMe();

    /**
     * 编辑当前管理员信息
     *
     * @return
     */
    public String editMe(AdminVO adminVO);

    /**
     * 修改密码
     *
     * @return
     */
    public String changePwd(String oldPwd, String newPwd);
}
