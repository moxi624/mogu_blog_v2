package com.moxi.mogublog.admin.security;

import com.moxi.mogublog.config.jwt.Audience;
import com.moxi.mogublog.config.jwt.JwtHelper;
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

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private Audience audience;

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

//	@Value(value="${base64Secret}")
//	private String base64Secret;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @Value(value = "${tokenHeader}")
    private String tokenHeader;

    @Value(value = "${audience.expiresSecond}")
    private Long expiresSecond;

    /**
     * Reserved claims（保留），它的含义就像是编程语言的保留字一样，属于JWT标准里面规定的一些claim。JWT标准里面定好的claim有：
     * iss(Issuser)：代表这个JWT的签发主体；
     * sub(Subject)：代表这个JWT的主体，即它的所有人；
     * aud(Audience)：代表这个JWT的接收对象；
     * exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；
     * nbf(Not Before)：是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
     * iat(Issued at)：是一个时间戳，代表这个JWT的签发时间；
     * jti(JWT ID)：是JWT的唯一标识。
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
//	@Override
//	public void doFilterInternal(final ServletRequest req, final ServletResponse res, final FilterChain chain)
//			throws IOException, ServletException {
//		final HttpServletRequest request = (HttpServletRequest) req;
//		final HttpServletResponse response = (HttpServletResponse) res;
//		//得到请求头信息authorization信息
//		final String authHeader = request.getHeader(tokenHeader);//设定为Authorization
//		if ("OPTIONS".equals(request.getMethod())) {
//			//OPTIONS请求旨在发送一种“探测”请求以确定针对某个目标地址的请求必须具有怎样的约束，然后根据其约束发送真正的请求。用于跨域
//			response.setStatus(HttpServletResponse.SC_OK);
//			chain.doFilter(req, res);
//		} else {
//			//请求头 'Authorization': tokenHead + token
//			if (authHeader == null || !authHeader.startsWith(tokenHead)) {
//				throw new LoginException("Login_error");
//			}
//			final String token = authHeader.substring(tokenHead.length());
//			
//			// 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
//            // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
//            // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
//            // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
//			String username = JwtHelper.getUsername(token,base64Secret);
//			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//			if(JwtHelper.validateToken(token,userDetails,base64Secret)) {
//				 UsernamePasswordAuthenticationToken authentication	= new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//				 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
//                         request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);//以后可以security中取得SecurityUser信息
//				}
//			}
//			
//			//获取token中的claims信息即载荷保存到request域中
//			if(audience == null){
//				BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
//				audience = (Audience) factory.getBean("audience");
//			}
//			final Claims claims = JwtHelper.parseJWT(token,audience.getBase64Secret());
//			if(claims == null){
//				throw new LoginException("Login_error");
//			}
//			request.setAttribute("claims", claims);
//		} catch (final Exception e) {
//			try {
//				throw new LoginException("Login_error");
//			} catch (LoginException e1) {
//				e1.printStackTrace();
//		}
//		chain.doFilter(req, res);
//	}
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

            log.error("传递过来的token为:" + authHeader);

            final String token = authHeader.substring(tokenHead.length());

            //判断token是否过期
            if (!jwtHelper.isExpiration(token, audience.getBase64Secret())) {
                //刷新token过期时间
                jwtHelper.refreshToken(token, audience.getBase64Secret(), expiresSecond);
                log.info("token未过期，刷新token");
            } else {
                chain.doFilter(request, response);
                return;
            }

            String username = jwtHelper.getUsername(token, audience.getBase64Secret());
            String adminUid = jwtHelper.getUserUid(token, audience.getBase64Secret());

            //把adminUid存储到request中
            request.setAttribute("adminUid", adminUid);
            logger.info("解析出来用户 : " + username);
            logger.info("解析出来的用户Uid : " + adminUid);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtHelper.validateToken(token, userDetails, audience.getBase64Secret())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));

                    logger.info("authenticated user " + username + ", setting security context");

                    //以后可以security中取得SecurityUser信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
		

