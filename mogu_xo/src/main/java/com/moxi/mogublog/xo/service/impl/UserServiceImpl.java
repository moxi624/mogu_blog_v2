package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.mapper.UserMapper;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User insertUserInfo(HttpServletRequest request, String response) {
        Map<String, Object> map = JsonUtils.jsonToMap(response);
        boolean exist = false;
        User user = new User();
        Map<String, Object> data = JsonUtils.jsonToMap(JsonUtils.objectToJson(map.get("data")));
        if (data.get("uuid") != null && data.get("source") != null) {
            if (getUserBySourceAnduuid(data.get("source").toString(), data.get("uuid").toString()) != null) {
                user = getUserBySourceAnduuid(data.get("source").toString(), data.get("uuid").toString());
                exist = true;
            }
        } else {
            System.out.println("未获取到uuid或source");
            return null;
        }

        if (data.get("email") != null) {
            user.setEmail(data.get("email").toString());
        }
        if (data.get("avatar") != null) {
            user.setAvatar(data.get("avatar").toString());
        }
        if (data.get("nickname") != null) {
            user.setNickName(data.get("nickname").toString());
        }
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(IpUtils.getIpAddr(request));
        if (exist) {
            user.updateById();
            System.out.println("updata");
        } else {
            /*初始化*/
            user.setUuid(data.get("uuid").toString());
            user.setSource(data.get("source").toString());
            user.setUserName("mg".concat(user.getSource()).concat(user.getUuid()));
            Integer randNum = (int) (Math.random() * (999999) + 1);//产生(0,999999]之间的随机数
            String workPassWord = String.format("%06d" , randNum);//进行六位数补全
            user.setPassWord(workPassWord);
            user.insert();
        }
        return user;
    }

    @Override
    public User getUserBySourceAnduuid(String source, String uuid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.UUID, uuid).eq(BaseSQLConf.SOURCE, source);
        return userService.getOne(queryWrapper);

    }

    @Override
    public Integer getUserCount(int status) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.STATUS, status);
        return userService.count(queryWrapper);
    }

    @Override
    public User serRequestInfo(User user) {
        HttpServletRequest request = RequestHolder.getRequest();
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get("OS");
        String browser = map.get("BROWSER");
        String ip = IpUtils.getIpAddr(request);
        user.setLastLoginIp(ip);
        user.setOs(os);
        user.setLastLoginTime(new Date());

        //从Redis中获取IP来源
        String jsonResult = stringRedisTemplate.opsForValue().get("IP_SOURCE:" + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses("ip=" + ip, "utf-8");
            if (StringUtils.isNotEmpty(addresses)) {
                user.setIpSource(addresses);
                stringRedisTemplate.opsForValue().set("IP_SOURCE" + BaseSysConf.REDIS_SEGMENTATION + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            user.setIpSource(jsonResult);
        }
        return user;
    }
}
