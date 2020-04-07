package com.moxi.mogublog.web.config;

import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.SysConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 拦截器
 */
@Component
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //得到请求头信息authorization信息
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null) {

            log.error("传递过来的token为:" + accessToken);

            //从Redis中获取内容
            String userInfo = stringRedisTemplate.opsForValue().get(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + accessToken);
            if (!StringUtils.isEmpty(userInfo)) {
                Map<String, Object> map = JsonUtils.jsonToMap(userInfo);
                //把userUid存储到 request中
                request.setAttribute(SysConf.TOKEN, accessToken);
                request.setAttribute(SysConf.USER_UID, map.get("uid"));
                request.setAttribute(SysConf.USER_NAME, map.get("nickName"));
            }
        }
        chain.doFilter(request, response);
    }
}
		

