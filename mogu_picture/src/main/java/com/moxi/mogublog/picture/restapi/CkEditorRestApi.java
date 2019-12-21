package com.moxi.mogublog.picture.restapi;

import com.moxi.mogublog.picture.util.Aboutfile;
import com.moxi.mogublog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/ckeditor")
public class CkEditorRestApi {

    @Value(value = "${file.upload.path}") //获取基本路径
    private String basePath;

    @Value(value = "${data.image.url}")
    private String imgURL;

    private String uploadImageUrl = "ckEditorUploadImg";//图像存放路径

    private String uploadFileUrl = "ckEditorUploadFile";//文件存放路径

    /**
     * 获取后缀名
     *
     * @param fileName
     * @return
     */
    private static String getPicExpandedName(String fileName) {
        String ext = "";
        if (StringUtils.isNotBlank(fileName) &&
                StringUtils.contains(fileName, ".")) {
            ext = StringUtils.substring(fileName, fileName.lastIndexOf(".") + 1);
        }
        ext = ext.toLowerCase();
        if (ext == null || ext.length() < 1)
            ext = "jpg";

        return ext;
    }

    /**
     * 图像中的图片上传
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public Object imgUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> errorMap = new HashMap<String, Object>();
        Aboutfile af = new Aboutfile();//引用自己设计的一个工具类

        // 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile file = multiRequest.getFile(iter.next());
            if (file != null) {

                String oldName = file.getOriginalFilename(); //获取旧名称
                String expandedName = getPicExpandedName(oldName); //获取扩展名

                if (!af.isPic(expandedName)) { //判断是否是图片
                    map.put("uploaded", 0);
                    errorMap.put("message", "请上传正确的图片");
                    map.put("error", errorMap);
                    return map;
                }

                if (file.getSize() > (2 * 1024 * 1024)) {//对图片大小进行限制
                    map.put("uploaded", 0);
                    errorMap.put("message", "图片大小不能超过2M");
                    map.put("error", errorMap);
                    return map;
                }

                //图片上传路径  
                String uploadPath = basePath + uploadImageUrl;
                String fileName = System.currentTimeMillis() + ""; // 采用时间格式命名
                fileName += ("." + expandedName);
                String fileLocation = uploadPath + "/" + fileName;
                //上传文件用的是个人工具类上传文件

                File file1 = new File(uploadPath);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                try {
                    File saveFile = new File(fileLocation);
                    saveFile.createNewFile();
                    file.transferTo(saveFile);
                    map.put("uploaded", 1);
                    map.put("fileName", fileName);
                    map.put("url", imgURL + uploadImageUrl + "/" + fileName);
                    return map;
                } catch (Exception e) {
                    map.put("uploaded", 0);
                    errorMap.put("message", "上传失败");
                    map.put("error", errorMap);
                    return map;
                }

            }
        }
        return null;

    }

    /**
     * 工具栏“插入\编辑超链接”的文件上传
     *
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public Object fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {


        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> errorMap = new HashMap<String, Object>();
        Aboutfile af = new Aboutfile();//引用自己设计的一个工具类

        // 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile file = multiRequest.getFile(iter.next());
            if (file != null) {

                String oldName = file.getOriginalFilename(); //获取旧名称
                String expandedName = getPicExpandedName(oldName); //获取扩展名

                if (!af.isSafe(expandedName)) { //判断是否安全文件
                    map.put("uploaded", 0);
                    errorMap.put("message", "请上传正确的图片");
                    map.put("error", errorMap);
                    return map;
                }

                if (file.getSize() > (50 * 1024 * 1024)) {//对图片大小进行限制
                    map.put("uploaded", 0);
                    errorMap.put("message", "文件大小不能超过50M");
                    map.put("error", errorMap);
                    return map;
                }

                //图片上传路径  
                String uploadPath = basePath + uploadFileUrl;
                String fileName = System.currentTimeMillis() + ""; // 采用时间格式命名
                fileName += ("." + expandedName);
                String fileLocation = uploadPath + "/" + fileName;
                //上传文件用的是个人工具类上传文件

                File file1 = new File(uploadPath);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                try {
                    File saveFile = new File(fileLocation);
                    saveFile.createNewFile();
                    file.transferTo(saveFile);
                    map.put("uploaded", 1);
                    map.put("fileName", fileName);
                    map.put("url", imgURL + uploadFileUrl + "/" + fileName);
                    return map;
                } catch (Exception e) {
                    map.put("uploaded", 0);
                    errorMap.put("message", "上传失败");
                    map.put("error", errorMap);
                    return map;
                }

            }
        }
        return null;
    }
}
