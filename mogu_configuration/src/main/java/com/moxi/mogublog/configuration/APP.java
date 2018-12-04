package com.moxi.mogublog.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class APP {

    public static void main(String[] args){
        SpringApplication.run(APP.class,args);
    }
    
}  
