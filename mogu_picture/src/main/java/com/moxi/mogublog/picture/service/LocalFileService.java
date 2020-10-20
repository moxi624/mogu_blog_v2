package com.moxi.mogublog.picture.service;

import com.moxi.mogublog.commons.entity.File;
import com.moxi.mogublog.commons.entity.FileSort;
import com.moxi.mougblog.base.service.SuperService;
import com.moxi.mougblog.base.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 本地文件系统服务类【通过IO流存储到本地】
 *
 * @author 陌溪
 * @date 2020年10月19日16:57:23
 */
public interface LocalFileService {

    /**
     * 多文件上传
     * @param multipartFileList
     * @param fileSort
     * @return
     * @throws IOException
     */
    List<String> batchUploadFile(List<MultipartFile> multipartFileList, FileSort fileSort) throws IOException;

    /**
     * 文件上传
     * @param multipartFile
     * @param fileSort
     * @return
     * @throws IOException
     */
    String uploadFile(MultipartFile multipartFile, FileSort fileSort) throws IOException;

    /**
     * 通过URL上传图片
     * @param url
     * @param fileSort
     * @return
     */
    String uploadPictureByUrl(String url, FileSort fileSort);
}
