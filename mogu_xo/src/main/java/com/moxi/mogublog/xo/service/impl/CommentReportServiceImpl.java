package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.commons.entity.CommentReport;
import com.moxi.mogublog.xo.mapper.CommentReportMapper;
import com.moxi.mogublog.xo.service.CommentReportService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 评论举报表 服务实现类
 *
 * @author 陌溪
 * @date 2020年1月12日15:47:47
 */
@Service
public class CommentReportServiceImpl extends SuperServiceImpl<CommentReportMapper, CommentReport> implements CommentReportService {

}
