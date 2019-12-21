package com.moxi.mogublog.picture.util;

import java.io.*;

public class Aboutfile {
    /**
     * 判断一个文件是否存在，不存在就创建它 Method execute,只能建最后面那个目录
     *
     * @param String path
     * @return null
     */
    public void creatfile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            System.out.println("the   dir   is   exits");
        } else {
            file.mkdir();
            System.out.println(path);
            System.out.println("have   made   a   dir   ！");

        }
    }

    /**
     * 从文件名中得到其后缀名
     *
     * @param String filename
     * @return 后缀名
     */
    public String getfileSuffix(String filename) {
        String suffix;
        suffix = filename.substring(
                filename.lastIndexOf(".") + 1);
        return suffix;
    }


    /**
     * 通过其后缀名判断其是否合法,合法后缀名为常见的
     *
     * @param String 后缀名
     * @return 合法返回true，不合法返回false
     */
    public boolean isSafe(String suffix) {
        suffix = suffix.toLowerCase();
        if (suffix.equals("ppt") || suffix.equals("xls") || suffix.equals("pdf") || suffix.equals("docx") || suffix.equals("doc") || suffix.equals("rar")
                || suffix.equals("zip") || suffix.equals("jpg") || suffix.equals("gif") || suffix.equals("jpeg")
                || suffix.equals("png") || suffix.equals("svg") || suffix.equals("msi")
                || suffix.equals("txt") || suffix.equals("docx") || suffix.equals("pptx") || suffix.equals("xlsx")
                || suffix.equals("rm") || suffix.equals("rmvb") || suffix.equals("wmv") || suffix.equals("mp4")
                || suffix.equals("3gp") || suffix.equals("mkv") || suffix.equals("avi")) {
            return true;
        } else {

            return false;
        }

    }

    /**
     * 通过其后缀名判断其是否是图片
     *
     * @param String 后缀名
     * @return 合法返回true，不合法返回false
     */
    public boolean isPic(String suffix) {
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("gif") || suffix.equals("jpeg") || suffix.equals("png")) {
            return true;
        } else {

            return false;
        }

    }


    /**
     * 进行上传文件的相关操作
     *
     * @param Formfile file
     * @throws IOException
     * @throws FileNotFoundException
     */
    public int upFile(File file, String fileLocation) throws FileNotFoundException, IOException {
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
            return result;
        }

    }


    /**
     * 计算文件大小，将long类型转换为String类型
     *
     * @param filesize
     * @return
     */
    public String getFileStringSize(long filesize) {
        //size不能为0？
        double temp = 0.0;
        String ssize = "";
        temp = (double) filesize / 1024;
        if (temp >= 1024) {
            temp = (double) temp / 1024;
            if (temp >= 1024) {
                temp = (double) temp / 1024;
                ssize = temp + "000";
                ssize = ssize.substring(0, ssize.indexOf(".") + 3) + "GB";
            } else {
                ssize = temp + "000";
                ssize = ssize.substring(0, ssize.indexOf(".") + 3) + "MB";
            }
        } else {
            ssize = temp + "000";
            ssize = ssize.substring(0, ssize.indexOf(".") + 3) + "KB";
        }
        return ssize;
    }
}
