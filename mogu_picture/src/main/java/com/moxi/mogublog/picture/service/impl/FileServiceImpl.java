package com.moxi.mogublog.picture.service.impl;

import com.moxi.mogublog.picture.entity.File;
import com.moxi.mogublog.picture.mapper.FileMapper;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
@Service
public class FileServiceImpl extends SuperServiceImpl<FileMapper, File> implements FileService {

}
