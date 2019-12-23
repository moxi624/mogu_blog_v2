//package com.moxi.mogublog.web.restapi;
//
//import io.swagger.annotations.Api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/login")
//@Api(value = "用户登录RestApi", tags = {"LoginRestApi"})
//public class LoginRestApi {
//
//    @A
//
//    // 创建授权request
//    AuthRequest authRequest = new AuthGiteeRequest(AuthConfig.builder()
//            .clientId("clientId")
//            .clientSecret("clientSecret")
//            .redirectUri("redirectUri")
//            .build());
//// 生成授权页面
//authRequest.authorize();
//// 授权登录后会返回一个code，用这个code进行登录
//authRequest.login("code");
//}
