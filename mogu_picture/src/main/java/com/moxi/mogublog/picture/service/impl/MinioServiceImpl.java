package com.moxi.mogublog.picture.service.impl;


import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.service.MinioService;
import com.moxi.mogublog.picture.util.MinioUtil;
import com.moxi.mogublog.picture.util.MoGuFileUtil;
import com.moxi.mougblog.base.exception.exceptionType.InsertException;
import com.moxi.mougblog.base.global.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地对象存储服务 Minio文件上传实现类
 *
 * @author 陌溪
 * @date 2020年10月19日11:13:21
 */
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    @Autowired
    MinioUtil minioUtil;

    @Override
    public List<String> batchUploadFile(List<MultipartFile> multipartFileList) {
        List<String> urlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            urlList.add(this.uploadFile(multipartFile));
        }
        return urlList;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        return minioUtil.uploadFile(multipartFile);
    }

    @Override
    public String uploadPictureByUrl(String itemUrl) {
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
            // TODO 不关闭流，小图片就无法显示？
            out.flush();
            out.close();
            MultipartFile multipartFile = MoGuFileUtil.fileToMultipartFile(dest);
            return minioUtil.uploadFile(multipartFile);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InsertException(ErrorCode.SYSTEM_CONFIG_NOT_EXIST, MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        } finally {
            if (dest != null && dest.getParentFile().exists()) {
                dest.delete();
            }
        }
    }
}
