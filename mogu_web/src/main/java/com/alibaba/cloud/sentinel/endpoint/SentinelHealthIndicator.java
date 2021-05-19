package com.alibaba.cloud.sentinel.endpoint;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.heartbeat.HeartbeatSenderProvider;
import com.alibaba.csp.sentinel.transport.HeartbeatSender;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问题描述
 * 启动后报：
 * 程序异常：org.springframework.web.util.NestedServletException: Handler dispatch failed; nested exception is java.lang.NoSuchMethodError: com.alibaba.csp.sentinel.transport.config.TransportConfig.getConsoleServer()Ljava/lang/String;
 * 健康检查报500错误
 * <p>
 * 问题分析
 * 查看’SentinelHealthIndicator’代码第47行
 * 查看sentinel1.8.0版本的TransportConfig的66行
 * Method由getConsoleServe()->getConsoleServerList()
 * 以上问题导致报上述BUG
 * 原文：https://www.freesion.com/article/87071247615/
 *
 * @author: 陌溪
 * @create: 2021-05-07-18:44
 */
public class SentinelHealthIndicator extends AbstractHealthIndicator {

    private DefaultListableBeanFactory beanFactory;

    private SentinelProperties sentinelProperties;

    public SentinelHealthIndicator(DefaultListableBeanFactory beanFactory,
                                   SentinelProperties sentinelProperties) {
        this.beanFactory = beanFactory;
        this.sentinelProperties = sentinelProperties;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Map<String, Object> detailMap = new HashMap<>();

        // If sentinel isn't enabled, set the status up and set the enabled to false in
        // detail
        if (!sentinelProperties.isEnabled()) {
            detailMap.put("enabled", false);
            builder.up().withDetails(detailMap);
            return;
        }

        detailMap.put("enabled", true);

        // Check health of Dashboard
        boolean dashboardUp = true;
        List<Tuple2<String, Integer>> consoleServer = TransportConfig.getConsoleServerList();
        if (consoleServer == null) {
            // If Dashboard isn't configured, it's OK and mark the status of Dashboard
            // with UNKNOWN.
            detailMap.put("dashboard",
                    new Status(Status.UNKNOWN.getCode(), "dashboard isn't configured"));
        } else {
            // If Dashboard is configured, send a heartbeat message to it and check the
            // result
            HeartbeatSender heartbeatSender = HeartbeatSenderProvider
                    .getHeartbeatSender();
            boolean result = heartbeatSender.sendHeartbeat();
            if (result) {
                detailMap.put("dashboard", Status.UP);
            } else {
                // If failed to send heartbeat message, means that the Dashboard is DOWN
                dashboardUp = false;
                detailMap.put("dashboard", new Status(Status.DOWN.getCode(),
                        consoleServer + " can't be connected"));
            }
        }

        // Check health of DataSource
        boolean dataSourceUp = true;
        Map<String, Object> dataSourceDetailMap = new HashMap<>();
        detailMap.put("dataSource", dataSourceDetailMap);

        // Get all DataSources and each call loadConfig to check if it's OK
        // If no Exception thrown, it's OK
        // Note:
        // Even if the dynamic config center is down, the loadConfig() might return
        // successfully
        // e.g. for Nacos client, it might retrieve from the local cache)
        // But in most circumstances it's okay
        Map<String, AbstractDataSource> dataSourceMap = beanFactory
                .getBeansOfType(AbstractDataSource.class);
        for (Map.Entry<String, AbstractDataSource> dataSourceMapEntry : dataSourceMap
                .entrySet()) {
            String dataSourceBeanName = dataSourceMapEntry.getKey();
            AbstractDataSource dataSource = dataSourceMapEntry.getValue();
            try {
                dataSource.loadConfig();
                dataSourceDetailMap.put(dataSourceBeanName, Status.UP);
            } catch (Exception e) {
                // If one DataSource failed to loadConfig, means that the DataSource is
                // DOWN
                dataSourceUp = false;
                dataSourceDetailMap.put(dataSourceBeanName,
                        new Status(Status.DOWN.getCode(), e.getMessage()));
            }
        }

        // If Dashboard and DataSource are both OK, the health status is UP
        if (dashboardUp && dataSourceUp) {
            builder.up().withDetails(detailMap);
        } else {
            builder.down().withDetails(detailMap);
        }
    }

}