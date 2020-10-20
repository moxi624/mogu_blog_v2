package com.moxi.mogublog.picture.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Minio文件上传服务类
 *
 * @author 陌溪
 * @since 2020年10月19日11:12:14
 */
public interface MinioService {

    /**
     * 多文件上传
     *
     * @param file
     * @return
     */
    Map<String, List<String>> uploadFile(MultipartFile[] file);
}
