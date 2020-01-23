package com.moxi.mogublog.picture.feign;

import com.moxi.mogublog.picture.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * mogu_admin相关接口
 *
 * @author 陌溪
 * @date 2020年1月21日22:19:10
 */

@FeignClient(name = "mogu-admin", url = "http://localhost:8601/", configuration = FeignConfiguration.class)
public interface AdminFeignClient {


    /**
     * 获取系统配置信息
     */
    @RequestMapping(value = "/systemConfig/getSystemConfig", method = RequestMethod.GET)
    public String getSystemConfig();

}