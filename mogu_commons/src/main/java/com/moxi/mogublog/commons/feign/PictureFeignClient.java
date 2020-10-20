package com.moxi.mogublog.commons.feign;

import com.moxi.mogublog.commons.config.feign.FeignConfiguration;
import com.moxi.mogublog.commons.fallback.PictureFeignFallback;
import com.moxi.mougblog.base.vo.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * mogu_picture相关接口
 *
 * @author 陌溪
 */
@FeignClient(name = "mogu-picture", configuration = FeignConfiguration.class, fallback = PictureFeignFallback.class)
public interface PictureFeignClient {

    /**
     * 获取文件的信息接口
     *
     * @param fileIds 图片uid
     * @param code    分隔符
     * @return
     */
    @RequestMapping(value = "/file/getPicture", method = RequestMethod.GET)
    String getPicture(@RequestParam("fileIds") String fileIds, @RequestParam("code") String code);

    /**
     * 通过URL List上传图片
     *
     * @param fileVO
     * @return
     */
    @RequestMapping(value = "/file/uploadPicsByUrl", method = RequestMethod.POST)
    String uploadPicsByUrl(FileVO fileVO);


    /**
     * 初始化网盘容量大小
     * @param adminUid
     * @param maxStorageSize
     */
    @RequestMapping(value = "/storage/initStorageSize", method = RequestMethod.POST)
    String initStorageSize(@RequestParam("adminUid") String adminUid, @RequestParam("maxStorageSize") Long maxStorageSize);

    /**
     * 调整网盘容量大小
     * @param adminUid
     * @param maxStorageSize
     */
    @RequestMapping(value = "/storage/editStorageSize", method = RequestMethod.POST)
    String editStorageSize(@RequestParam("adminUid") String adminUid, @RequestParam("maxStorageSize") Long maxStorageSize);

    /**
     * 通过管理员uid列表获取存储信息
     * @param adminUidList
     * @return
     */
    @RequestMapping(value = "/storage/getStorageByAdminUid", method = RequestMethod.GET)
    String getStorageByAdminUid(@RequestParam("adminUidList") List<String> adminUidList);
}