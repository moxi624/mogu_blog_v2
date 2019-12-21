package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.Todo;
import com.moxi.mogublog.xo.mapper.TodoMapper;
import com.moxi.mogublog.xo.service.TodoService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 待办事项表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2019年6月29日10:31:44
 */
@Service
public class TodoServiceImpl extends SuperServiceImpl<TodoMapper, Todo> implements TodoService {

    @Autowired
    TodoMapper todoMapper;

    @Override
    public void toggleAll(Integer done, String adminUid) {
        todoMapper.toggleAll(done, adminUid);
    }

}
