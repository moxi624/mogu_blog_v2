package com.moxi.mogublog.admin.security;

import com.alibaba.fastjson.JSONObject;
import com.moxi.mougblog.base.global.ECode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to

//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

        response.setStatus(ECode.SUCCESS);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("code", ECode.UNAUTHORIZED);
        result.put("data", "token无效或过期");
        response.getWriter().write(JSONObject.toJSONString(result));

//    	response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        JSONObject result=new JSONObject();
//        JSONObject header=new JSONObject();
//        if(authException instanceof BadCredentialsException){
//
//            header.put("errorcode","8002");
//            header.put("errorinfo","用户名或密码错误，请重新输入！");
//            result.put("header",header);
//        }else{
//            header.put("errorcode","8001");
//            header.put("errorinfo","无效的token");
//            result.put("header",header);
//        }
//        response.getWriter().write(JSONObject.toJSONString(result));
    }
}

