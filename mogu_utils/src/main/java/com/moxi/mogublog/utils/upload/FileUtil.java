package com.moxi.mogublog.utils.upload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg", "webp"};
    public static final String[] DOC_FILE = {"doc", "docx", "txt", "hlp", "wps", "rtf", "html", "pdf", "md", "sql", "css", "js", "vue", "java"};
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};
    public static final String[] ALL_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg", "webp",
            "doc", "docx", "txt", "hlp", "wps", "rtf", "html", "pdf", "md", "sql", "css", "js", "vue", "java",
            "avi", "mp4", "mpg", "mov", "swf",
            "wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"
    };
    public static final int IMAGE_TYPE = 1;
    public static final int DOC_TYPE = 2;
    public static final int VIDEO_TYPE = 3;
    public static final int MUSIC_TYPE = 4;
    public static final int OTHER_TYPE = 5;

    public static List<String> getFileExtendsByType(int fileType) {

        List<String> fileExtends;
        switch (fileType) {
            case IMAGE_TYPE:
                fileExtends = Arrays.asList(IMG_FILE);
                break;

            case DOC_TYPE:
                fileExtends = Arrays.asList(DOC_FILE);
                break;
            case VIDEO_TYPE:
                fileExtends = Arrays.asList(VIDEO_FILE);
                break;
            case MUSIC_TYPE:
                fileExtends = Arrays.asList(MUSIC_FILE);
                break;
            case OTHER_TYPE: {
                fileExtends = Arrays.asList(ALL_FILE);
            }
            break;
            default:
                fileExtends = new ArrayList<>();
                break;


        }
        return fileExtends;
    }

    /**
     * 判断是否为图片文件
     *
     * @param extendName 文件扩展名
     * @return 是否为图片文件
     */
    public static boolean isImageFile(String extendName) {
        for (int i = 0; i < IMG_FILE.length; i++) {
            if (extendName.equalsIgnoreCase(IMG_FILE[i])) {
                return true;
            }
        }
        return false;
    }


    public static String pathSplitFormat(String filePath) {
        String path = filePath.replace("///", "/")
                .replace("//", "/")
                .replace("\\\\\\", "\\")
                .replace("\\\\", "\\");
        return path;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileType(String fileName) {
        if (fileName.lastIndexOf(".") == -1) {
            return "";
            //这里暂时用jpg，后续应该去获取真实的文件类型
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取不包含扩展名的文件名
     *
     * @param fileName 文件名
     * @return 文件名（不带扩展名）
     */
    public static String getFileNameNotExtend(String fileName) {
        String fileType = getFileType(fileName);
        return fileName.replace("." + fileType, "");
    }


}
