package com.moxi.mogublog.commons.feign;

import com.moxi.mogublog.config.feign.FeignConfiguration;
import com.moxi.mougblog.base.vo.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * mogu_picture相关接口
 *
 * @author xzx19950624@qq.com
 */
@FeignClient(name = "mogu-picture", configuration = FeignConfiguration.class)
public interface PictureFeignClient {


    /**
     * 获取文件的信息接口
     *
     * @return
     * @ApiImplicitParam(name = "fileIds", value = "fileIds", required = false, dataType = "String"),
     * @ApiImplicitParam(name = "code", value = "分割符", required = false, dataType = "String")
     */
    @RequestMapping(value = "/file/getPicture", method = RequestMethod.GET)
    public String getPicture(@RequestParam("fileIds") String fileIds, @RequestParam("code") String code);

    /**
     * 通过URL List上传图片
     *
     * @param fileVO
     * @return
     */
    @RequestMapping(value = "/file/uploadPicsByUrl2", method = RequestMethod.POST)
    public String uploadPicsByUrl(FileVO fileVO);

}