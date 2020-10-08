package com.moxi.mogublog.picture.util;

import com.moxi.mougblog.base.global.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文件相关工具类
 *
 * @author 陌溪
 * @date 2020/9/14 9:31
 */
@Slf4j
public class AboutFileUtil {

    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg", "webp"};
    public static final String[] DOC_FILE = {"doc", "docx", "txt", "hlp", "wps", "rtf", "html", "pdf", "md", "sql", "css", "js", "vue", "java"};
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};
    public static final String[] ALL_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg", "webp",
            "doc", "docx", "txt", "hlp", "wps", "rtf", "html", "pdf", "md", "sql", "css", "js", "vue", "java",
            "avi", "mp4", "mpg", "mov", "swf",
            "wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"
    };

    /**
     * 判断一个文件是否存在，不存在就创建它 Method execute,只能建最后面那个目录
     *
     * @param path
     * @return
     */
    public void creatFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            log.error("该目录不存在");
        } else {
            file.mkdir();
        }
    }

    /**
     * 从文件名中得到其后缀名
     *
     * @param filename
     * @return 后缀名
     */
    public String getFileSuffix(String filename) {
        String suffix;
        suffix = filename.substring(
                filename.lastIndexOf(Constants.SYMBOL_POINT) + 1);
        return suffix;
    }

    /**
     * 通过其后缀名判断其是否合法,合法后缀名为常见的
     *
     * @param suffix 后缀名
     * @return 合法返回true，不合法返回false
     */
    public boolean isSafe(String suffix) {
        suffix = suffix.toLowerCase();
        for (int i = 0; i < ALL_FILE.length; i++) {
            if (ALL_FILE[i].equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过其后缀名判断其是否是图片
     *
     * @param suffix 后缀名
     * @return 合法返回true，不合法返回false
     */
    public boolean isPic(String suffix) {
        suffix = suffix.toLowerCase();
        for (int i = 0; i < IMG_FILE.length; i++) {
            if (IMG_FILE[i].equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行上传文件的相关操作
     *
     * @param file
     * @throws IOException
     * @throws FileNotFoundException
     */
    public int uploadFile(File file, String fileLocation) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int result = 1;
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(new File(fileLocation));
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            try {
                try {
                    if (null != bis) {
                        bis.close();
                        bis = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    result = 0;
                }
                try {
                    if (null != bos) {
                        bos.close();
                        bos = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    result = 0;
                }
                fis.close();
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;

            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;

            }
        }
        return result;
    }

    /**
     * 计算文件大小，将long类型转换为String类型
     *
     * @param fileSize
     * @return
     */
    public String getFileStringSize(long fileSize) {
        //size不能为0？
        double temp = 0.0;
        String ssize = "";
        temp = (double) fileSize / Constants.NUM_1024;
        if (temp >= Constants.NUM_1024) {
            temp = temp / Constants.NUM_1024;
            if (temp >= Constants.NUM_1024) {
                temp = temp / Constants.NUM_1024;
                ssize = temp + "000";
                ssize = ssize.substring(Constants.NUM_ZERO, ssize.indexOf(Constants.SYMBOL_POINT) + Constants.NUM_THREE) + "GB";
            } else {
                ssize = temp + "000";
                ssize = ssize.substring(Constants.NUM_ZERO, ssize.indexOf(Constants.SYMBOL_POINT) + Constants.NUM_THREE) + "MB";
            }
        } else {
            ssize = temp + "000";
            ssize = ssize.substring(Constants.NUM_ZERO, ssize.indexOf(Constants.SYMBOL_POINT) + Constants.NUM_THREE) + "KB";
        }
        return ssize;
    }
}
