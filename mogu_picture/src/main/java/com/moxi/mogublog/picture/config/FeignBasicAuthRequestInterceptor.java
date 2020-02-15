package com.moxi.mogublog.picture.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign请求拦截器（设置请求头，传递登录信息）
 *
 * @author: 陌溪
 * @create: 2020-01-21-22:34
 */
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取token，放入到feign的请求头
        String token = request.getParameter("token");

        // 如果带有？说明还带有其它参数，我们只截取到token即可
        if(token.indexOf("?") != -1) {
            String [] params = token.split("\\?url=");
            token = params[0];
        }
        requestTemplate.header("pictureToken", token);
    }
}
