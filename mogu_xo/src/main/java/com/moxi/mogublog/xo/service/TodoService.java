package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.mogublog.commons.entity.Todo;
import com.moxi.mogublog.xo.vo.TodoVO;
import com.moxi.mougblog.base.service.SuperService;

/**
 * 待办事项表 服务类
 *
 * @author 陌溪
 * @date 2019年6月29日10:31:18
 */
public interface TodoService extends SuperService<Todo> {

    /**
     * 批量更新代办事项的状态
     *
     * @param done     : 状态
     * @param adminUid : 管理员UID
     */
    public void toggleAll(Integer done, String adminUid);

    /**
     * 获取待办事项列表
     *
     * @param todoVO
     * @return
     */
    public IPage<Todo> getPageList(TodoVO todoVO);

    /**
     * 新增待办事项
     *
     * @param todoVO
     */
    public String addTodo(TodoVO todoVO);

    /**
     * 编辑待办事项
     *
     * @param todoVO
     */
    public String editTodo(TodoVO todoVO);

    /**
     * 删除待办事项
     *
     * @param todoVO
     */
    public String deleteTodo(TodoVO todoVO);

    /**
     * 批量编辑待办事项
     *
     * @param todoVO
     */
    public String editBatchTodo(TodoVO todoVO);
}
