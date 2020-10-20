package com.moxi.mogublog.picture.service;

import com.moxi.mogublog.commons.entity.File;
import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mougblog.base.service.SuperService;
import com.moxi.mougblog.base.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 文件服务类
 *
 * @author 陌溪
 * @since 2018-09-17
 */
public interface FileService extends SuperService<File> {

    /**
     * 批量文件上传
     * @param request
     * @param multipartFileList
     * @param systemConfig
     * @return
     */
    String batchUploadFile(HttpServletRequest request, List<MultipartFile> multipartFileList, SystemConfig systemConfig);

    /**
     * 通过URL上传图片
     * @param fileVO
     * @return
     */
    String uploadPictureByUrl(FileVO fileVO);

    /**
     * CKeditor图像中的图片上传
     * @return
     */
    Object ckeditorUploadFile(HttpServletRequest request);
    /**
     * CKeditor上传 复制的图片
     * @return
     */
    String ckeditorUploadCopyFile();


}
