//package com.moxi.mogublog.config.durid;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.bind.RelaxedPropertyResolver;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.DefaultResourceLoader;
//import org.springframework.core.io.support.ResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternUtils;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.sql.SQLException;
//
//@Configuration
//@EnableTransactionManagement
//public class DataBaseConfiguration implements EnvironmentAware {
//
//    private RelaxedPropertyResolver propertyResolver;
//
//    @Override
//    public void setEnvironment(Environment env) {
//        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
//    }
//
//    @Bean
//    public DataSource writeDataSource() {
//        System.out.println("注入druid！！！");
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(propertyResolver.getProperty("url"));
//        datasource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
//        datasource.setUsername(propertyResolver.getProperty("username"));
//        datasource.setPassword(propertyResolver.getProperty("password"));
//        datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initial-size")));
//        datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("min-idle")));
//        datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("max-wait")));
//        datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("max-active")));
//        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver.getProperty("min-evictable-idle-timemillis")));
//        try{
//            datasource.setFilters(propertyResolver.getProperty("filters"));
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return datasource;
//    }
//}
