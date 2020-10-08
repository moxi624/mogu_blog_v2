package com.moxi.mogublog.admin.config;

import com.moxi.mougblog.base.handler.HandlerExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局异常处理配置
 *
 * @author 陌溪
 * @date 2020年10月4日10:44:17
 */
@Configuration
public class GlobalExceptionConfig {

    @Bean
    public HandlerExceptionResolver getHandlerExceptionResolver() {
        return new HandlerExceptionResolver();
    }
}
