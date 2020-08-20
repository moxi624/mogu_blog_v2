package com.moxi.mogublog.admin.security;

import com.moxi.mogublog.admin.global.RedisConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.commons.config.jwt.Audience;
import com.moxi.mogublog.commons.config.jwt.JwtHelper;
import com.moxi.mogublog.utils.CookieUtils;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private Audience audience;

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @Value(value = "${tokenHeader}")
    private String tokenHeader;

    /**
     * token过期的时间
     */
    @Value(value = "${audience.expiresSecond}")
    private Long expiresSecond;

    /**
     * token刷新的时间
     */
    @Value(value = "${audience.refreshSecond}")
    private Long refreshSecond;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //得到请求头信息authorization信息
        String authHeader = request.getHeader(tokenHeader);

        // 从picture服务传递过来的token，如果有说明执行了上传操作
        final String pictureToken = request.getHeader("pictureToken");
        if (StringUtils.isNotEmpty(pictureToken)) {
            authHeader = pictureToken;
        }

        //请求头 'Authorization': tokenHead + token
        if (authHeader != null && authHeader.startsWith(tokenHead)) {

            log.error("传递过来的token为: {}", authHeader);

            final String token = authHeader.substring(tokenHead.length());

            // 获取在线的管理员信息
            String onlineAdmin = redisUtil.get(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + authHeader);

            if (StringUtils.isNotEmpty(onlineAdmin) && !jwtHelper.isExpiration(token, audience.getBase64Secret())) {
                /**
                 * 得到过期时间
                 */
                Date expirationDate = jwtHelper.getExpiration(token, audience.getBase64Secret());
                long nowMillis = System.currentTimeMillis();
                Date nowDate = new Date(nowMillis);
                // 得到两个日期相差的间隔，秒
                Integer second = DateUtils.getSecondByTwoDay(expirationDate, nowDate);
                // 如果小于5分钟，那么更新过期时间
                if (second < refreshSecond) {
                    // 生成一个新的Token
                    String newToken = tokenHead + jwtHelper.refreshToken(token, audience.getBase64Secret(), expiresSecond * 1000);
                    // 生成新的token，发送到客户端
                    CookieUtils.setCookie("Admin-Token", newToken, expiresSecond.intValue());
                    // 重新更新Redis中的过期时间
                    redisUtil.setEx(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + newToken, onlineAdmin, expiresSecond, TimeUnit.SECONDS);
                }
            } else {
                chain.doFilter(request, response);
                return;
            }

            String username = jwtHelper.getUsername(token, audience.getBase64Secret());
            String adminUid = jwtHelper.getUserUid(token, audience.getBase64Secret());

            //把adminUid存储到request中
            request.setAttribute(SysConf.ADMIN_UID, adminUid);
            request.setAttribute(SysConf.USER_NAME, username);
            request.setAttribute(SysConf.TOKEN, authHeader);
            log.info("解析出来用户: {}", username);
            log.info("解析出来的用户Uid: {}", adminUid);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtHelper.validateToken(token, userDetails, audience.getBase64Secret())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));

                    //以后可以security中取得SecurityUser信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
		

