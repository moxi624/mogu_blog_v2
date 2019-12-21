package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.mapper.TagMapper;
import com.moxi.mogublog.xo.service.TagService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class TagServiceImpl extends SuperServiceImpl<TagMapper, Tag> implements TagService {

}
