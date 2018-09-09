package com.moxi.mogublog.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableTransactionManagement
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {
        "com.moxi.mogublog.admin.config",
        "com.moxi.mogublog.admin.controller",
        "com.moxi.mogublog.xo.service"})
public class APP {

	public static void main(String[] args) {
		SpringApplication.run(APP.class, args);
	}
	

}
