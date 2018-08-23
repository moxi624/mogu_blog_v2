package com.moxi.mogublog.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;


@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, //如果用了Hibernate
        DataSourceTransactionManagerAutoConfiguration.class
})
//@ComponentScan(basePackages = {"com.moxi.mogublog.admin","com.moxi.mogublog.xo","com.moxi.mogublog.base"})
@MapperScan(basePackages = {"com.moxi.mogublog.xo.dao","com.moxi.mogublog.base.dao"})
public class APP {
	
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(APP.class, args);
	}
	

//	@Bean(destroyMethod =  "close")
//	public DataSource dataSource() {
//		DruidDataSource dataSource = new DruidDataSource();
//		dataSource.setUrl(env.getProperty("spring.datasource.url"));
//		dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
//		dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
//		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//		dataSource.setInitialSize(2);//初始化时建立物理连接的个数
//		dataSource.setMaxActive(20);//最大连接池数量
//		dataSource.setMinIdle(0);//最小连接池数量
//		dataSource.setMaxWait(60000);//获取连接时最大等待时间，单位毫秒。
//		dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
//		dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
//		dataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
//		dataSource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
//		return dataSource;
//	}
}
