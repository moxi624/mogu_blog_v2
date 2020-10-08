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
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.utils.upload.FileOperation;
import com.moxi.mogublog.utils.upload.ImageOperation;
import com.moxi.mogublog.utils.upload.PathUtil;
import com.moxi.mougblog.base.holder.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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

    @Value(value = "${file.upload.path}")
    String path;
    @Autowired
    private QiniuUtil qiniuUtil;
    @Resource
    private FileService fileService;
    @Resource
    private NetworkDiskService networkDiskService;
    @Resource
    private StorageService storageService;
    @Autowired
    private FeignUtil feignUtil;

    /**
     * 旋转图片
     *
     * @param direction 方向
     * @param imageid   图片id
     * @return 返回结果
     */
    @RequestMapping(value = "/totationimage", method = RequestMethod.POST)
    @ResponseBody
    public String totationImage(@RequestBody String direction, @RequestBody String imageid) {
        NetworkDisk networkDisk = new NetworkDisk();
        networkDisk.setUid(imageid);
        networkDisk = networkDiskService.selectFileById(networkDisk);
        String imageUrl = networkDisk.getFileUrl();
        String extendName = networkDisk.getExtendName();
        File file = FileOperation.newFile(PathUtil.getStaticPath() + imageUrl);
        File minfile = FileOperation.newFile(PathUtil.getStaticPath() + imageUrl.replace("." + extendName, "_min." + extendName));
        if (SysConf.LEFT.equals(direction)) {
            try {
                ImageOperation.leftTotation(file, file, 90);
                ImageOperation.leftTotation(minfile, minfile, 90);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (SysConf.RIGHT.equals(direction)) {
            try {
                ImageOperation.rightTotation(file, file, 90);
                ImageOperation.rightTotation(minfile, minfile, 90);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 批量删除图片
     *
     * @return
     */
    @RequestMapping(value = "/deleteimagebyids", method = RequestMethod.POST)
    @ResponseBody
    public String deleteImageByIds(@RequestBody String imageids) {
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
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
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
        RequestHolder.checkLogin();
        Map<String, String> qiNiuConfig = feignUtil.getQiNiuConfigByWebToken(RequestHolder.getAdminToken());
        String imageUrl = PathUtil.getStaticPath() + networkDiskVO.getFileUrl();
        String minImageUrl = imageUrl.replace("." + networkDiskVO.getExtendName(), "_min." + networkDiskVO.getExtendName());
        long fileSize = FileOperation.getFileSize(imageUrl);
        networkDiskVO.setIsDir(0);
        networkDiskService.deleteFile(networkDiskVO, qiNiuConfig);
        // 更新存储空间
        FileOperation.deleteFile(imageUrl);
        FileOperation.deleteFile(minImageUrl);
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    /**
     * 上传文件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, NetworkDisk networkDisk) {
        RequestHolder.checkLogin();
        Map<String, String> qiNiuConfig = qiniuUtil.getQiNiuConfig();
        if (qiNiuConfig == null) {
            return ResultUtil.errorWithMessage(MessageConf.SYSTEM_CONFIG_NOT_EXIST);
        }
        // 获取文件
        List<MultipartFile> fileDatas = FileUtils.getMultipartFileList(request);
        // 上传文件
        String result = fileService.uploadImgs(path, request, fileDatas, qiNiuConfig);
        List<com.moxi.mogublog.picture.entity.File> fileList = WebUtils.getAllList(result, com.moxi.mogublog.picture.entity.File.class);
        // 写入NetworkDisk表中
        storageService.uploadFile(request, networkDisk, fileList);
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 获取存储信息
     *
     * @return
     */
    @RequestMapping(value = "/getStorage", method = RequestMethod.GET)
    @ResponseBody
    public String getStorage() {
        RequestHolder.checkLogin();
        Storage storage = storageService.getStorageByAdmin();
        return ResultUtil.successWithData(storage);
    }
}
