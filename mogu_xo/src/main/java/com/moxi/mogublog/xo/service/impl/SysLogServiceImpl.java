package com.moxi.mogublog.xo.service.impl;

import org.springframework.stereotype.Service;

import com.moxi.mogublog.xo.entity.SysLog;
import com.moxi.mogublog.xo.mapper.SysLogMapper;
import com.moxi.mogublog.xo.service.SysLogService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;


/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Service
public class SysLogServiceImpl extends SuperServiceImpl<SysLogMapper, SysLog> implements SysLogService {

}
