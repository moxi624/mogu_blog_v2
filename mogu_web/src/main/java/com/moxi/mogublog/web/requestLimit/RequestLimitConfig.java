package com.moxi.mogublog.web.requestLimit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * RequestLimitConfig
 *
 * @author: 陌溪
 * @create: 2020-03-06-18:58
 */
@RefreshScope
@ConfigurationProperties(prefix = "request-limit")
@Component
@Data
public class RequestLimitConfig {

    /**
     * 允许访问的数量
     */
    public int amount;
    /**
     * 时间段
     */
    public long time;
}
