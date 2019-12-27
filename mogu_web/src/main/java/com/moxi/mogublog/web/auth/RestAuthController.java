package com.moxi.mogublog.web.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.JwtUtil;
import com.moxi.mogublog.utils.MailUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/oauth")
@Api(value = "认证RestApi", tags = {"AuthRestApi"})
public class RestAuthController {

    @Autowired
    private UserService userService;
    @Value(value = "${justAuth.clientId.gitee}")
    private String giteeClienId;
    @Value(value = "${justAuth.clientSecret.gitee}")
    private String giteeClientSecret;
    @Value(value = "${justAuth.clientId.github}")
    private String githubClienId;
    @Value(value = "${justAuth.clientSecret.github}")
    private String githubClientSecret;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "获取认证", notes = "获取认证")
    @RequestMapping("/render")
    public String renderAuth(String source, HttpServletResponse response) throws IOException {
        System.out.println("进入render:" + source);
        AuthRequest authRequest = getAuthRequest(source);
        String token = AuthStateUtils.createState();
        String authorizeUrl = authRequest.authorize(token);
        System.out.println(authorizeUrl);

        stringRedisTemplate.opsForValue().set("token:" + token, token);

//        response.sendRedirect(authorizeUrl);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("url", authorizeUrl);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }


    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建gitee授权应用时的回调地址应为：http://127.0.0.1:8603/oauth/callback/gitee
     */
    @RequestMapping("/callback/{source}")
    public void login(@PathVariable("source") String source, AuthCallback callback, HttpServletResponse httpServletResponse) throws IOException {
        System.out.println("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        String result = JSONObject.toJSONString(response);
        System.out.println(JSONObject.toJSONString(response));

        Map<String, Object> map = JsonUtils.jsonToMap(result);
        Map<String, Object> data = JsonUtils.jsonToMap(JsonUtils.objectToJson(map.get("data")));
        Map<String, Object> token = JsonUtils.jsonToMap(JsonUtils.objectToJson(data.get("token")));
        String accessToken = token.get("accessToken").toString();
        User user = userService.insertUserInfo(result);

        if(user != null) {
            //将从数据库查询的数据缓存到redis中
//            stringRedisTemplate.opsForValue().set("source", user.getSource());
//            stringRedisTemplate.opsForValue().set("uuid", user.getUuid());

            stringRedisTemplate.opsForValue().set( accessToken, JsonUtils.objectToJson(user), 60*100, TimeUnit.SECONDS);
        }


//        return response;
        httpServletResponse.sendRedirect("http://localhost:9527/#/?token=" + accessToken);
    }

    @RequestMapping("/revoke/{source}/{token}")
    public Object revokeAuth(@PathVariable("source") String source, @PathVariable("token") String token) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    @RequestMapping("/refresh/{source}")
    public Object refreshAuth(@PathVariable("source") String source, String token) {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.refresh(AuthToken.builder().refreshToken(token).build());
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GetMapping("/verify/{accessToken}")
    public String verifyUser(@PathVariable("accessToken") String accessToken){
        String userInfo = stringRedisTemplate.opsForValue().get(accessToken);
        Map<String, Object> map = JsonUtils.jsonToMap(userInfo);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "删除accessToken", notes = "删除accessToken")
    @RequestMapping("/delete/{accessToken}")
    public void deleteUserAccessToken(@PathVariable("accessToken") String accessToken){
        stringRedisTemplate.delete(accessToken);
    }


    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubClienId)
                        .clientSecret(githubClientSecret)
                        .redirectUri("http://127.0.0.1:8603/oauth/callback/github")
                        .build());
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeClienId)
                        .clientSecret(giteeClientSecret)
                        .redirectUri("http://127.0.0.1:8603/oauth/callback/gitee")
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest){
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}
