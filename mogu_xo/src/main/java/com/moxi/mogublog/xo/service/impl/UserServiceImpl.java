package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.mapper.UserMapper;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
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
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {

}
