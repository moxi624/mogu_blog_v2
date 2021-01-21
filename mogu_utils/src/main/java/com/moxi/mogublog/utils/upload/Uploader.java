package com.moxi.mogublog.utils.upload;


import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传辅助类
 */
public class Uploader {
    public static final String ROOT_PATH = "upload";
    private static final Logger LOG = LoggerFactory.getLogger(Uploader.class);
    List<UploadFile> saveUploadFileList = new ArrayList<>();
    private StandardMultipartHttpServletRequest request = null;
    // 文件允许格式
    private String[] allowFiles = {".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp", "blob", ".mp4"};
    // 文件大小限制，单位KB
    private int maxSize = 10000000;

    public Uploader(HttpServletRequest request) {
        this.request = (StandardMultipartHttpServletRequest) request;
        saveUploadFileList = new ArrayList<>();
    }

    public List<UploadFile> upload() {

        // 判断enctype属性是否为multipart/form-data 
        boolean isMultipart = ServletFileUpload.isMultipartContent(this.request);
        if (!isMultipart) {
            UploadFile uploadFile = new UploadFile();
            uploadFile.setSuccess(0);
            uploadFile.setMessage("未包含文件上传域");
            saveUploadFileList.add(uploadFile);
            return saveUploadFileList;
        }
        DiskFileItemFactory dff = new DiskFileItemFactory();//1、创建工厂
        String savePath = getSaveFilePath(ROOT_PATH);
        dff.setRepository(new File(savePath));
        try {
            ServletFileUpload sfu = new ServletFileUpload(dff);//2、创建文件上传解析器
            sfu.setSizeMax(this.maxSize * 1024L);
            sfu.setHeaderEncoding("utf-8");//3、解决文件名的中文乱码
            Iterator<String> iter = this.request.getFileNames();
            while (iter.hasNext()) {
                UploadFile uploadFile = new UploadFile();
                MultipartFile multipartfile = this.request.getFile(iter.next());
                InputStream inputStream = multipartfile.getInputStream();

                String originalName = multipartfile.getOriginalFilename();
//                if (!this.checkFileType(originalName)) {
//                    uploadFile.setSuccess(0);
//                    uploadFile.setMessage("未包含文件上传域");
//                    saveUploadFileList.add(uploadFile);
//                    continue;
//                }
                String fileName = getFileName(originalName);
                String timeStampName = getTimeStampName();
                String fileType = FileUtil.getFileType(originalName);
                uploadFile.setFileName(fileName);
                uploadFile.setFileType(fileType);
                uploadFile.setTimeStampName(timeStampName);

                String saveFilePath = savePath + File.separator + timeStampName + "." + fileType;
                String minFilePath = savePath + File.separator + timeStampName + "_min" + "." + fileType;
                uploadFile.setUrl(saveFilePath);
                File file = new File(PathUtil.getStaticPath() + File.separator + saveFilePath);
                File minFile = new File(PathUtil.getStaticPath() + File.separator + minFilePath);
                BufferedInputStream in = null;
                FileOutputStream out = null;
                BufferedOutputStream output = null;

                try {
                    in = new BufferedInputStream(inputStream);
                    out = new FileOutputStream(file);
                    output = new BufferedOutputStream(out);
                    Streams.copy(in, output, true);
                    if (FileUtil.isImageFile(uploadFile.getFileType())) {
                        ImageOperation.thumbnailsImage(file, minFile, 300);
                    }

                } catch (FileNotFoundException e) {
                    LOG.error("文件没有发现" + e);
                } catch (IOException e) {
                    LOG.error("文件读取失败" + e);
                } finally {
                    closeStream(in, out, output);
                }
                uploadFile.setSuccess(1);
                uploadFile.setMessage("上传成功");
                uploadFile.setFileSize(file.length());
                saveUploadFileList.add(uploadFile);

            }
        } catch (IOException e) {
            UploadFile uploadFile = new UploadFile();
            uploadFile.setSuccess(1);
            uploadFile.setMessage("未知错误");
            saveUploadFileList.add(uploadFile);
            e.printStackTrace();
        }

        return saveUploadFileList;
    }


    private void closeStream(BufferedInputStream in, FileOutputStream out,
                             BufferedOutputStream output) throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (output != null) {
            output.close();
        }
    }

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }


    private String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 依据原始文件名生成新文件名
     *
     * @return
     */
    private String getTimeStampName() {
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            return "" + number.nextInt(10000)
                    + System.currentTimeMillis();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("生成安全随机数失败");
        }
        return ""
                + System.currentTimeMillis();

    }

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     *
     * @param path
     * @return
     */
    private String getSaveFilePath(String path) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        path = File.separator + path + File.separator + format.format(new Date());
        File dir = new File(PathUtil.getStaticPath() + path);
        LOG.error(PathUtil.getStaticPath() + path);
        if (!dir.exists()) {
            try {
                boolean isSuccessMakeDir = dir.mkdirs();
                if (!isSuccessMakeDir) {
                    LOG.error("文件创建失败:" + PathUtil.getStaticPath() + path);
                }
            } catch (Exception e) {
                LOG.error("目录创建失败" + PathUtil.getStaticPath() + path);
                return "";
            }
        }
        return path;
    }

}
