package com.moxi.mogublog.xo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.moxi.mogublog.xo.controller",
        "com.moxi.mogublog.xo.service"})
public class APP {

	public static void main(String[] args) {
		SpringApplication.run(APP.class, args);
	}
}
