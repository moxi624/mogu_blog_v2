package com.moxi.mogublog.picture.service;

import com.moxi.mogublog.picture.entity.File;
import com.moxi.mougblog.base.service.SuperService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
public interface FileService extends SuperService<File> {

    /**
     * 多文件上传
     * @param path
     * @param request
     * @param filedatas
     * @return
     */
    String uploadImgs(String path, HttpServletRequest request, List<MultipartFile> filedatas, Map<String, String> qiNiuConfig);
}
