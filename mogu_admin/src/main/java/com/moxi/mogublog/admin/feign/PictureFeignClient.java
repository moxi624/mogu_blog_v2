package com.moxi.mogublog.admin.feign;

import com.moxi.mogublog.admin.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * mogu_picture相关接口
 *
 * @author xzx19950624@qq.com
 */

@FeignClient(name = "mogu-picture", url = "http://localhost:8602/", configuration = FeignConfiguration.class)
public interface PictureFeignClient {


    /**
     *
     * @param fileIds
     * @param code
     * @return
     */
    @RequestMapping(value = "/file/getPicture", method = RequestMethod.GET)
    public String getPicture(@RequestParam("fileIds") String fileIds, @RequestParam("code") String code);

    @RequestMapping(value = "/file/hello", method = RequestMethod.GET)
    public String hello();
}
