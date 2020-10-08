package com.moxi.mogublog.web.log;

import com.moxi.mogublog.commons.entity.WebVisit;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.RedisConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.holder.AbstractRequestAwareRunnable;
import com.moxi.mougblog.base.holder.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 异步记录日志
 *
 * @author: 陌溪
 * @create: 2020-03-05-8:59
 */
@Component("WebSysLogHandle")
public class SysLogHandle extends AbstractRequestAwareRunnable {

    @Autowired
    RedisUtil redisUtil;
    /**
     * 模块UID
     */
    String moduleUid;
    /**
     * 其它数据
     */
    String otherData;
    /**
     * 用户UID
     */
    private String userUid;
    /**
     * 用户行为
     */
    private String behavior;

    /**
     * 构造方法，用于初始化成员变量
     */
    public void setSysLogHandle(String userUid, String behavior, String moduleUid, String otherData) {
        this.userUid = userUid;
        this.behavior = behavior;
        this.moduleUid = moduleUid;
        this.otherData = otherData;
    }

    @Override
    protected void onRun() {
        HttpServletRequest request = RequestHolder.getRequest();
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get(SysConf.OS);
        String browser = map.get(SysConf.BROWSER);
        WebVisit webVisit = new WebVisit();
        String ip = IpUtils.getIpAddr(request);
        webVisit.setIp(ip);

        //从Redis中获取IP来源
        String jsonResult = redisUtil.get(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses(SysConf.IP + SysConf.EQUAL_TO + ip, SysConf.UTF_8);
            if (StringUtils.isNotEmpty(addresses)) {
                webVisit.setIpSource(addresses);
                redisUtil.setEx(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            webVisit.setIpSource(jsonResult);
        }
        webVisit.setOs(os);
        webVisit.setBrowser(browser);
        webVisit.setUserUid(userUid);
        webVisit.setBehavior(behavior);
        webVisit.setModuleUid(moduleUid);
        webVisit.setOtherData(otherData);
        webVisit.insert();
    }
}
