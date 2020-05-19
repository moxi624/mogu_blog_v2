package com.moxi.mogublog.admin.restapi;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 游客表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018-09-08
 */
@RestController
@RequestMapping("/visitor")
@Api(value = "游客相关接口", tags = {"游客相关接口"})
@Slf4j
public class VisitorRestApi {

}

