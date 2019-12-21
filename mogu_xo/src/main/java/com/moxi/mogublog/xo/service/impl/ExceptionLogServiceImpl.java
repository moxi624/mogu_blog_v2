package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.xo.entity.ExceptionLog;
import com.moxi.mogublog.xo.mapper.ExceptionLogMapper;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Service
public class ExceptionLogServiceImpl extends SuperServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

}
