package com.moxi.mogublog.picture.feign;

import com.moxi.mogublog.picture.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * mogu_admin相关接口
 *
 * @author 陌溪
 * @date 2020年1月21日22:19:10
 */

@FeignClient(name = "mogu-web", url = "http://localhost:8603/", configuration = FeignConfiguration.class)
public interface WebFeignClient {

    /**
     * 获取系统配置信息
     */
    @RequestMapping(value = "/oauth/getSystemConfig", method = RequestMethod.GET)
    public String getSystemConfig(@RequestParam("token") String token);

}