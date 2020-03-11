package com.moxi.mogublog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * jwt生成接口
 *
 * @author xzx19950624@qq.com
 */
@Slf4j
public class JwtUtil {

    private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    //过期时间
    private final static int expiresSecond = 1000 * 60 * 2 * 60;

    /**
     * 解析jwt toke 获取数据
     *
     * @param jsonWebToken
     * @return
     */
    public static Claims parseJWT(String jsonWebToken) {
        log.info("解析jwt===========" + jsonWebToken);
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

}
