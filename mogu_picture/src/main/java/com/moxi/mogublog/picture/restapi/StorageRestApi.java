package com.moxi.mogublog.picture.restapi;

import com.alibaba.fastjson.JSON;
import com.moxi.mogublog.picture.entity.NetworkDisk;
import com.moxi.mogublog.picture.entity.Storage;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.NetworkDiskService;
import com.moxi.mogublog.picture.service.StorageService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.picture.vo.NetworkDiskVO;
import com.moxi.mogublog.utils.FileUtils;
import com.moxi.mogublog.utils.RestResult;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.utils.upload.FileOperation;
import com.moxi.mogublog.utils.upload.ImageOperation;
import com.moxi.mogublog.utils.upload.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 存储控制类
 *
 * @author 陌溪
 * @date 2020年7月11日21:23:14
 */
@RestController
@RequestMapping("/storage")
public class StorageRestApi {

    @Autowired
    QiniuUtil qiniuUtil;

    //获取上传路径
    @Value(value = "${file.upload.path}")
    String path;

    @Resource
    FileService fileService;

    @Resource
    NetworkDiskService networkDiskService;

    @Resource
    StorageService filetransferService;

    @Autowired
    FeignUtil feignUtil;

    /**
     * 旋转图片
     *
     * @param direction 方向
     * @param imageid   图片id
     * @return 返回结果
     */
    @RequestMapping(value = "/totationimage", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> totationImage(@RequestBody String direction, @RequestBody String imageid) {
        RestResult<String> result = new RestResult<String>();
        NetworkDisk networkDisk = new NetworkDisk();
        networkDisk.setUid(imageid);
        networkDisk = networkDiskService.selectFileById(networkDisk);
        String imageUrl = networkDisk.getFileUrl();
        String extendName = networkDisk.getExtendName();
        File file = FileOperation.newFile(PathUtil.getStaticPath() + imageUrl);
        File minfile = FileOperation.newFile(PathUtil.getStaticPath() + imageUrl.replace("." + extendName, "_min." + extendName));
        if ("left".equals(direction)) {
            try {
                ImageOperation.leftTotation(file, file, 90);
                ImageOperation.leftTotation(minfile, minfile, 90);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("right".equals(direction)) {
            try {
                ImageOperation.rightTotation(file, file, 90);
                ImageOperation.rightTotation(minfile, minfile, 90);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 批量删除图片
     *
     * @return
     */
    @RequestMapping(value = "/deleteimagebyids", method = RequestMethod.POST)
    @ResponseBody
    public String deleteImageByIds(@RequestBody String imageids) {
        RestResult<String> result = new RestResult<>();
        List<Integer> imageidList = JSON.parseArray(imageids, Integer.class);

        List<NetworkDisk> fileList = networkDiskService.selectFileListByIds(imageidList);
        networkDiskService.deleteFileByIds(imageidList);
        long totalFileSize = 0;
        for (NetworkDisk networkDisk : fileList) {
            String imageUrl = PathUtil.getStaticPath() + networkDisk.getFileUrl();
            String minImageUrl = imageUrl.replace("." + networkDisk.getExtendName(), "_min." + networkDisk.getExtendName());
            totalFileSize += FileOperation.getFileSize(imageUrl);
            FileOperation.deleteFile(imageUrl);
            FileOperation.deleteFile(minImageUrl);
        }
//        Storage storage = filetransferService.selectStorageBean(new Storage(SysConf.DEFAULT_UID));
//        if (storage != null){
//            long updateFileSize = storage.getStoragesize() - totalFileSize;
//            if (updateFileSize < 0){
//                updateFileSize = 0;
//            }
//            storage.setStoragesize(updateFileSize);
//            filetransferService.updateStorageBean(storage);
//        }

        result.setData("删除文件成功");
        result.setSuccess(true);
        String resultJson = JSON.toJSONString(result);
        return resultJson;
    }

    /**
     * 删除图片
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteimage", method = RequestMethod.POST)
    @ResponseBody
    public String deleteImage(HttpServletRequest request, @RequestBody NetworkDiskVO networkDiskVO) {
        RestResult<String> result = new RestResult<>();
        if (request.getAttribute(SysConf.TOKEN) == null) {
            result.setSuccess(false);
            result.setErrorMessage("请先登录");
            return JSON.toJSONString(result);
        }
        Map<String, String> qiNiuConfig = feignUtil.getQiNiuConfigByWebToken(request.getAttribute(SysConf.TOKEN).toString());

        String imageUrl = PathUtil.getStaticPath() + networkDiskVO.getFileUrl();
        String minImageUrl = imageUrl.replace("." + networkDiskVO.getExtendName(), "_min." + networkDiskVO.getExtendName());
        long fileSize = FileOperation.getFileSize(imageUrl);
        networkDiskVO.setIsDir(0);
        networkDiskService.deleteFile(networkDiskVO, qiNiuConfig);
        FileOperation.deleteFile(imageUrl);
        FileOperation.deleteFile(minImageUrl);
        // 更新存储空间
        String resultJson = JSON.toJSONString(result);
        return resultJson;
    }

    /**
     * 上传文件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, NetworkDisk networkDisk) {
        RestResult<String> restResult = new RestResult<>();
        if (request.getAttribute(SysConf.TOKEN) == null) {
            restResult.setSuccess(false);
            restResult.setErrorMessage("请先登录");
            return JSON.toJSONString(restResult);
        }
        String qiNiuConfigResult = qiniuUtil.getQiNiuConfig();
        Map<String, String> qiNiuConfig = WebUtils.getData(qiNiuConfigResult, Map.class);
        if (qiNiuConfig == null) {
            restResult.setSuccess(false);
            restResult.setErrorMessage(MessageConf.SYSTEM_CONFIG_NOT_EXIST);
            String resultJson = JSON.toJSONString(restResult);
            return resultJson;
        }
        // 获取文件
        List<MultipartFile> fileDatas = FileUtils.getMultipartFileList(request);
        // 上传文件
        String result = fileService.uploadImgs(path, request, fileDatas, qiNiuConfig);
        List<com.moxi.mogublog.picture.entity.File> fileList = WebUtils.getAllList(result, com.moxi.mogublog.picture.entity.File.class);
        // 写入NetworkDisk表中
        filetransferService.uploadFile(request, networkDisk, fileList);
        restResult.setSuccess(true);
        String resultJson = JSON.toJSONString(restResult);
        return resultJson;
    }

    /**
     * 下载文件
     *
     * @return
     */
    @RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
    public String downloadFile(HttpServletResponse response, NetworkDisk networkDisk) {
        RestResult<String> restResult = new RestResult<>();
        String fileName = null;// 文件名
        try {
            fileName = new String(networkDisk.getFileName().getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (fileName != null) {
            fileName = fileName + "." + networkDisk.getExtendName();
            //设置文件路径
            File file = FileOperation.newFile(PathUtil.getStaticPath() + networkDisk.getFileUrl());
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;

    }


    /**
     * 获取存储信息
     *
     * @return
     */
    @RequestMapping(value = "/getStorage", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<Storage> getStorage() {
        RestResult<Storage> restResult = new RestResult<>();
        Storage storage = filetransferService.getStorageByAdmin();
        restResult.setData(storage);
        restResult.setSuccess(true);
        return restResult;
    }
}
