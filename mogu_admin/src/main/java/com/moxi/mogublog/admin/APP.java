package com.moxi.mogublog.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.moxi.mogublog.admin.config",
        "com.moxi.mogublog.admin.controller",
        "com.moxi.mogublog.xo.service"})
public class APP {

	public static void main(String[] args) {
		SpringApplication.run(APP.class, args);
	}
	

}
