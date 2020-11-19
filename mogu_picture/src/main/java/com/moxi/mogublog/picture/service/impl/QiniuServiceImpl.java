package com.moxi.mogublog.picture.service.impl;


import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.service.QiniuService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.FileUtils;
import com.moxi.mougblog.base.exception.exceptionType.InsertException;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.global.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 七牛云实现类
 *
 * @author 陌溪
 * @since 2020年1月20日20:05:45
 */
@Service
@RefreshScope
@Slf4j
public class QiniuServiceImpl implements QiniuService {

    @Autowired
    FileSortService fileSortService;

    @Autowired
    FileService fileService;
    @Autowired
    FeignUtil feignUtil;
    @Autowired
    QiniuUtil qiniuUtil;
    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String path;

    @Override
    public List<String> batchUploadFile(List<MultipartFile> multipartFileList) throws IOException {
        List<String> urlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            urlList.add(this.uploadSingleFile(multipartFile));
        }
        return urlList;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return this.uploadSingleFile(multipartFile);
    }

    @Override
    public String uploadPictureByUrl(String itemUrl, SystemConfig systemConfig) {
        java.io.File dest = null;
        // 将图片上传到本地服务器中以及七牛云中
        BufferedOutputStream out = null;
        FileOutputStream os = null;
        // 输入流
        InputStream inputStream = null;
        //获取新文件名 【默认为jpg】
        String newFileName = System.currentTimeMillis() + ".jpg";
        try {
            // 构造URL
            URL url = new URL(itemUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            // 设置用户代理
            con.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
            // 设置10秒
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            // 当获取的相片无法正常显示的时候，需要给一个默认图片
            inputStream = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            String tempFiles = "temp/" + newFileName;
            dest = new java.io.File(tempFiles);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            os = new FileOutputStream(dest, true);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            FileInputStream fileInputStream = new FileInputStream(dest);
            MultipartFile fileData = new MockMultipartFile(dest.getName(), dest.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            out = new BufferedOutputStream(new FileOutputStream(dest));
            out.write(fileData.getBytes());
            QiniuUtil qn = new QiniuUtil();
            // TODO 不关闭流，小图片就无法显示？
            out.flush();
            out.close();
            return qn.uploadQiniu(dest, systemConfig);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InsertException(ErrorCode.SYSTEM_CONFIG_NOT_EXIST, MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        } finally {
            if (dest != null && dest.getParentFile().exists()) {
                dest.delete();
            }
        }
    }

    /**
     * 七牛云服务图片上传【上传到七牛云中】
     *
     * @return
     */
    private String uploadSingleFile(MultipartFile multipartFile) {
        BufferedOutputStream out = null;
        java.io.File dest = null;
        String url = "";
        try {
            // 从Redis中获取系统配置
            SystemConfig systemConfig = feignUtil.getSystemConfig();
            String oldName = multipartFile.getOriginalFilename();
            //获取扩展名，默认是jpg
            String picExpandedName = FileUtils.getPicExpandedName(oldName);
            //获取新文件名
            String newFileName = System.currentTimeMillis() + Constants.SYMBOL_POINT + picExpandedName;

            // 创建一个临时目录文件
            String tempFiles = path + "/temp/" + newFileName;
            dest = new java.io.File(tempFiles);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            out = new BufferedOutputStream(new FileOutputStream(dest));
            out.write(multipartFile.getBytes());
            out.flush();
            out.close();
            url = qiniuUtil.uploadQiniu(dest, systemConfig);
        } catch (Exception e) {
            e.getStackTrace();
            log.error("文件上传七牛云失败: {}", e.getMessage());
        } finally {
            if (dest != null && dest.getParentFile().exists()) {
                dest.delete();
            }
        }
        return url;
    }
}
