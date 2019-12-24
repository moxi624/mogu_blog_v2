package com.moxi.mogublog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt生成接口
 *
 * @author xzx19950624@qq.com
 */
public class JwtUtil {

    private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    private final static int expiresSecond = 1000 * 60 * 2 * 60;//过期时间
    private static Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 解析jwt toke 获取数据
     *
     * @param jsonWebToken
     * @return
     */
    public static Claims parseJWT(String jsonWebToken) {
//	    	log.info("解析jwt==========="+ jsonWebToken);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 生成jwt token user
     *
     * @param userOpenId
     * @param userId
     * @param isUser
     * @param shopId
     * @return
     */
    public static String createJWT(String userOpenId, Long userId, boolean isUser, Long shopId) {
        log.info("userOpenId" + userOpenId + "userId" + userId + "isUser" + isUser + "shopId" + shopId);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("user_id", userId)
                .claim("shop_id", shopId)
                .claim("is_user", isUser)
                .claim("user_open_id", userOpenId)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (expiresSecond >= 0) {
            long expMillis = nowMillis + expiresSecond;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        String compact = builder.compact();
        log.info("生成jwt===========" + compact);
        return compact;

    }

    public static String createSysUserJWT(Long shopId, Long sysUserId, String loginUserName, String loginPassWord, boolean isShop) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("shop_id", shopId)
                .claim("sys_user_id", sysUserId)
                .claim("is_shop", isShop)
                .claim("login_username", loginUserName)
                .claim("login_password", loginPassWord)
//	                .claim("user_open_id", userOpenId)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (expiresSecond >= 0) {
            long expMillis = nowMillis + expiresSecond;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        String compact = builder.compact();
        log.info("生成jwt===========" + compact);
        return compact;
    }


    /**
     * 判断小程序是否登陆 isUser是不是用户登陆
     *
     * @param rd_session
     * @return
     */
    public static Map<String, Object> booleanLogin(String rd_session) {

        boolean flag = false;
        Map<String, Object> remap = new HashMap<>();
        Claims parseJWT = parseJWT(rd_session);

        long userId = 0L;
        long shopId = 0L;
        boolean isUser = true;
        String userOpenId = "";
        try {
            if (parseJWT != null) {
                userId = StringUtils.getLong(parseJWT.get("user_id").toString(), 0l);
                shopId = StringUtils.getLong(parseJWT.get("shop_id").toString(), 0l);
                isUser = (boolean) parseJWT.get("is_user");
                userOpenId = (String) parseJWT.get("user_open_id");
                log.info("user_open_id" + parseJWT.get("user_open_id"));
                log.info("userOpenId" + userOpenId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userId > 0 && shopId > 0) {
            flag = true;
        }
        remap.put("userId", userId);
        remap.put("shopId", shopId);
        remap.put("isUser", isUser);
        remap.put("userOpenId", userOpenId);
        remap.put("flag", flag);

        return remap;
    }

}
