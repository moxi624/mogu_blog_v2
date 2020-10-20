//package com.moxi.mogublog.picture.config;
//
//import io.minio.MinioClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Minio配置类
// *
// * @author: 陌溪
// * @create: 2020-10-19-9:36
// */
//@Configuration
//public class MinioConfig {
//    @Value("${minio.endpoint}")
//    private String endpoint;
//
//    @Value("${minio.accessKey}")
//    private String accessKey;
//
//    @Value("${minio.secretKey}")
//    private String secretKey;
//
//    @Bean
//    public MinioClient minioClient() throws Exception{
//        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
//        MinioClient minioClient = new MinioClient(endpoint, accessKey, secretKey);
//        return minioClient;
//    }
//}