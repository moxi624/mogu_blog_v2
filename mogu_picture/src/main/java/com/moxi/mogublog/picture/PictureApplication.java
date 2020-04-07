package com.moxi.mogublog.picture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableTransactionManagement
@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients("com.moxi.mogublog.commons.feign")
@ComponentScan(basePackages = {
        "com.moxi.mogublog.config.feign",
        "com.moxi.mogublog.picture.config",
        "com.moxi.mogublog.picture.util",
        "com.moxi.mogublog.picture.restapi",
        "com.moxi.mogublog.picture.service"})
public class PictureApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureApplication.class, args);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
