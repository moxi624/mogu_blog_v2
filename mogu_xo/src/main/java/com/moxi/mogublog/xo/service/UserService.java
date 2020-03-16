package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.xo.entity.User;
import com.moxi.mougblog.base.service.SuperService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
public interface UserService extends SuperService<User> {

    /**
     * 记录用户信息
     *
     * @param response
     */
    User insertUserInfo(HttpServletRequest request, String response);

    /**
     * 通过source uuid获取用户类
     *
     * @param source
     * @param uuid
     * @return
     */
    User getUserBySourceAnduuid(String source, String uuid);

    /**
     * 获取用户数
     *
     * @param status
     * @return
     */
    public Integer getUserCount(int status);

    /**
     * 设置Request相关，如浏览器，IP，IP来源
     * @param user
     * @return
     */
    public User serRequestInfo(User user);

    /**
     * 通过ids获取用户列表
     * @param ids
     * @return
     */
    public List<User> getUserListByIds(List<String> ids);
}
