package com.moxi.mogublog.xo.service;

import com.moxi.mogublog.xo.entity.Todo;
import com.moxi.mougblog.base.service.SuperService;

/**
 * <p>
 * 待办事项表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2019年6月29日10:31:18
 */
public interface TodoService extends SuperService<Todo> {

    /**
     * 批量更新代办事项的状态
     *
     * @param done     : 状态
     * @param adminUid : 管理员UID
     */
    public void toggleAll(Integer done, String adminUid);
}
