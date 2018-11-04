package com.moxi.mogublog.config.jwt;

import java.io.IOException;

import javax.security.auth.login.LoginException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.GenericFilter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationTokenFilter extends GenericFilter{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private Audience audience;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Value(value="${base64Secret}")
	private String base64Secret;
	
	@Value(value="${tokenHead}")
	private String tokenHead;
	
	@Value(value="${tokenHeader}")
	private String tokenHeader;
	/**
	 *  Reserved claims（保留），它的含义就像是编程语言的保留字一样，属于JWT标准里面规定的一些claim。JWT标准里面定好的claim有：
	 iss(Issuser)：代表这个JWT的签发主体；
	 sub(Subject)：代表这个JWT的主体，即它的所有人；
	 aud(Audience)：代表这个JWT的接收对象；
	 exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；
	 nbf(Not Before)：是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
	 iat(Issued at)：是一个时间戳，代表这个JWT的签发时间；
	 jti(JWT ID)：是JWT的唯一标识。
	 * @param req
	 * @param res
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		//得到请求头信息authorization信息
		final String authHeader = request.getHeader(tokenHeader);//设定为Authorization
		if ("OPTIONS".equals(request.getMethod())) {
			//OPTIONS请求旨在发送一种“探测”请求以确定针对某个目标地址的请求必须具有怎样的约束，然后根据其约束发送真正的请求。用于跨域
			response.setStatus(HttpServletResponse.SC_OK);
			chain.doFilter(req, res);
		} else {
			try {
			//请求头 'Authorization': tokenHead + token
			if (authHeader == null || !authHeader.startsWith(tokenHead)) {
				throw new LoginException("Login_error");
			}
			final String token = authHeader.substring(tokenHead.length());
			
			// 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
            // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
            // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
            // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
			String username = JwtHelper.getUsername(token,base64Secret);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if(JwtHelper.validateToken(token,userDetails,base64Secret)) {
				 UsernamePasswordAuthenticationToken authentication	= new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
				 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                         request));
                 SecurityContextHolder.getContext().setAuthentication(authentication);//以后可以security中取得SecurityUser信息
				}
			}
			
			//获取token中的claims信息即载荷保存到request域中
			if(audience == null){
				BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
				audience = (Audience) factory.getBean("audience");
			}
			final Claims claims = JwtHelper.parseJWT(token,audience.getBase64Secret());
			if(claims == null){
				throw new LoginException("Login_error");
			}
			request.setAttribute("claims", claims);
		} catch (final Exception e) {
			try {
				throw new LoginException("Login_error");
			} catch (LoginException e1) {
				e1.printStackTrace();
			}
		}
		chain.doFilter(req, res);
	}
}
	
	@Override
	public void destroy() {
		
	}
}

