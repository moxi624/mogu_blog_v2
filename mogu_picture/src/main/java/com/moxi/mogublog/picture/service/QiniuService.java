package com.moxi.mogublog.picture.service;

import com.moxi.mogublog.picture.entity.File;
import com.moxi.mougblog.base.service.SuperService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 七牛云服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2020年1月20日20:04:51
 */
public interface QiniuService {

    /**
     * 多文件上传
     * @param file
     * @return
     */
    Map<String, List<String>> uploadImgs(MultipartFile[] file, Map<String, String> qiNiuConfig);
}
