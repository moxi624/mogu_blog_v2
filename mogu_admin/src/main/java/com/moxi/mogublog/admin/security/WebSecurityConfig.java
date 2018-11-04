package com.moxi.mogublog.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.moxi.mogublog.config.jwt.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
		@Autowired
	    private JwtAuthenticationEntryPoint unauthorizedHandler;

	    // Spring会自动寻找同样类型的具体类注入，这里就是SecurityUserDetailsServiceImpl了
	    @Autowired
	    private UserDetailsService userDetailsService;

	    @Autowired
	    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	        authenticationManagerBuilder
	                // 设置UserDetailsService
	                .userDetailsService(this.userDetailsService)
	                // 使用BCrypt进行密码的hash
	                .passwordEncoder(passwordEncoder());
	    }
	    // 装载BCrypt密码编码器
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
	        return new JwtAuthenticationTokenFilter();
	    }

	    @Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	                // 由于使用的是JWT，我们这里不需要csrf
	                .csrf().disable()
	                
	                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

	                // 基于token，所以不需要session
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

	                .authorizeRequests()
	                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

	                // 允许对于网站静态资源的无授权访问
	                .antMatchers(
	                        HttpMethod.GET,
	                        "/",
	                        "/*.html",
	                        "/favicon.ico",
	                        "/**/*.html",
	                        "/**/*.css",
	                        "/**/*.js",
	                        "/static/**"
	                ).permitAll()
	                .antMatchers(
	                		"/v2/api-docs",
	                		"/configuration/ui",
	                		"/swagger-resources",
	                		"/configuration/security",
	                		"/swagger-ui.html", "/webjars/**",
	                		"/swagger-resources/configuration/ui",
	                		"/swagge‌​r-ui.html"
	                ).permitAll()
	                // 对于获取token的rest api要允许匿名访问
	                .antMatchers("/login/**",
	                			 "/auth/**",
	                			 "/creatCode/**"
	                		).permitAll()
	                // 除上面外的所有请求全部需要鉴权认证
	                .anyRequest().authenticated();
	        
	        // 添加JWT filter
	        httpSecurity
	                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

	        // 禁用缓存
	        httpSecurity.headers().cacheControl();
	    }
	}

