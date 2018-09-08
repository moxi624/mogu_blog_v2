package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.Comment;
import com.moxi.mogublog.xo.mapper.CommentMapper;
import com.moxi.mogublog.xo.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
