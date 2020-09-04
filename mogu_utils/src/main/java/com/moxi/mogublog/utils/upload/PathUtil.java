package com.moxi.mogublog.utils.upload;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PathUtil {
    /**
     * 获取项目所在的根目录路径 resources路径
     *
     * @return
     */
    public static String getProjectRootPath() {
        String absolutePath = null;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            absolutePath = urlDecode(new File(url).getAbsolutePath()) + File.separator;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return absolutePath;
    }

    /**
     * 路径解码
     *
     * @param url
     * @return
     */
    public static String urlDecode(String url) {
        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeUrl;
    }

    /**
     * 得到static路径
     *
     * @return
     */
    public static String getStaticPath() {
        String projectRootAbsolutePath = getProjectRootPath();

        int index = projectRootAbsolutePath.indexOf("file:");
        if (index != -1) {
            projectRootAbsolutePath = projectRootAbsolutePath.substring(0, index);
        }

        return projectRootAbsolutePath + "static" + File.separator;


    }

    /**
     * 依据原始文件名生成新文件名
     *
     * @return
     */
    public static String getFileName(String fileName) {
        String getfileName = "";
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            return getfileName = "" + number.nextInt(10000)
                    + System.currentTimeMillis() + getFileExt(fileName);
        } catch (NoSuchAlgorithmException e) {
            //LOG.error("生成安全随机数失败");
        }
        return getfileName = ""
                + System.currentTimeMillis() + getFileExt(fileName);

    }

    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private static String getFileExt(String fileName) {
        if (fileName.lastIndexOf(".") == -1) {
            return ".png";
            //这里暂时用jpg，后续应该去获取真实的文件类型
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
