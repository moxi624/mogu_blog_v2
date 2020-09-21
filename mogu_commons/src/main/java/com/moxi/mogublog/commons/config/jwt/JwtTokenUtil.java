package com.moxi.mogublog.commons.config.jwt;

import com.moxi.mogublog.commons.config.security.SecurityUser;
import com.moxi.mougblog.base.global.BaseSysConf;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * JWT工具类
 *
 * @author 陌溪
 * @date 2020/6/1 18:18
 */
@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * 解析jwt
     */
    public Claims parseJWT(String token, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 构建jwt
     *
     * @param userName       账户名
     * @param adminUid       账户id
     * @param roleName       账户拥有角色名
     * @param audience       代表这个Jwt的接受对象
     * @param issuer         代表这个Jwt的签发主题
     * @param TTLMillis      jwt有效时间
     * @param base64Security 加密方式
     * @return
     */
    public String createJWT(String userName, String adminUid, String roleName,
                            String audience, String issuer, long TTLMillis, String base64Security) {
        // HS256是一种对称算法, 双方之间仅共享一个 密钥
        // 由于使用相同的密钥生成签名和验证签名, 因此必须注意确保密钥不被泄密
        // 也可以改成RS256: 非对称加密算法，使用私钥进行加密，使用公钥来验证Token的有效性
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim(BaseSysConf.ADMIN_UID, adminUid)
                .claim(BaseSysConf.ROLE, roleName)
                .claim(BaseSysConf.CREATE_TIME, now)
                .setSubject(userName)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @param base64Security
     * @return
     */
    public boolean isExpiration(String token, String base64Security) {
        if (parseJWT(token, base64Security) == null) {
            return true;
        } else {
            return parseJWT(token, base64Security).getExpiration().before(new Date());
        }
    }


    /**
     * 效验token
     *
     * @param token
     * @param userDetails
     * @param base64Security
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails, String base64Security) {
        SecurityUser SecurityUser = (SecurityUser) userDetails;
        final String username = getUsername(token, base64Security);
        final boolean expiration = isExpiration(token, base64Security);
        return (
                username.equals(SecurityUser.getUsername())
                        && !expiration);
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @param base64Security
     * @return
     */
    public String getUsername(String token, String base64Security) {
        return parseJWT(token, base64Security).getSubject();
    }

    /**
     * 从token中获取用户UID
     *
     * @param token
     * @param base64Security
     * @return
     */
    public String getUserUid(String token, String base64Security) {
        return parseJWT(token, base64Security).get(BaseSysConf.ADMIN_UID, String.class);
    }

    /**
     * 从token中获取audience
     *
     * @param token
     * @param base64Security
     * @return
     */
    public String getAudience(String token, String base64Security) {
        return parseJWT(token, base64Security).getAudience();
    }

    /**
     * 从token中获取issuer
     *
     * @param token
     * @param base64Security
     * @return
     */
    public String getIssuer(String token, String base64Security) {
        return parseJWT(token, base64Security).getIssuer();
    }

    /**
     * 从token中获取过期时间
     *
     * @param token
     * @param base64Security
     * @return
     */
    public Date getExpiration(String token, String base64Security) {
        return parseJWT(token, base64Security).getExpiration();
    }

    /**
     * token是否可以更新
     *
     * @param token
     * @param base64Security
     * @return
     */
    public Boolean canTokenBeRefreshed(String token, String base64Security) {
        return !isExpiration(token, base64Security);
    }

    /**
     * 更新token
     *
     * @param token
     * @param base64Security
     * @param TTLMillis
     * @return refreshedToken 返回更新后的token，需要客户端进行更新
     */
    public String refreshToken(String token, String base64Security, long TTLMillis) {
        String refreshedToken;
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            // 生成签名密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            final Claims claims = parseJWT(token, base64Security);
            claims.put("creatDate", new Date());
            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                    .setClaims(claims)
                    .setSubject(getUsername(token, base64Security))
                    .setIssuer(getIssuer(token, base64Security))
                    .setAudience(getAudience(token, base64Security))
                    .signWith(signatureAlgorithm, signingKey);
            //添加Token过期时间
            if (TTLMillis >= 0) {
                long expMillis = nowMillis + TTLMillis;
                Date exp = new Date(expMillis);
                builder.setExpiration(exp).setNotBefore(now);
            }
            refreshedToken = builder.compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        log.info("刷新后的token: {}", refreshedToken);
        return refreshedToken;
    }
}
