package com.moxi.mogublog.picture.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
     *
     * @param file
     * @return
     */
    Map<String, List<String>> uploadImgs(MultipartFile[] file, Map<String, String> qiNiuConfig);

    /**
     * 通过URL上传图片
     *
     * @return
     */
    Object uploadImgByUrl();

    /**
     * 工具栏“插入\编辑超链接”的文件上传
     *
     * @return
     */
    Object fileUpload(HttpServletRequest request);

    /**
     * 图像中的图片上传
     *
     * @return
     */
    Object imgUpload(HttpServletRequest request);
}
