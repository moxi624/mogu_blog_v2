package com.moxi.mogublog.web.annotion.log;

import com.moxi.mogublog.commons.entity.WebVisit;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.RedisConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.holder.AbstractRequestAwareRunnable;

import java.util.concurrent.TimeUnit;

/**
 * 异步记录日志
 *
 * @author: 陌溪
 * @create: 2020-03-05-8:59
 */

public class SysLogHandle extends AbstractRequestAwareRunnable {

    /**
     * 模块UID
     */
    String moduleUid;
    /**
     * 其它数据
     */
    String otherData;
    /**
     * Redis工具类对象
     */
    private RedisUtil redisUtil;
    /**
     * 用户UID
     */
    private String userUid;
    /**
     * 用户行为
     */
    private String behavior;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 构造函数
     *
     * @param userUid
     * @param ip
     * @param os
     * @param browser
     * @param behavior
     * @param moduleUid
     * @param otherData
     * @param redisUtil
     */
    public SysLogHandle(String userUid, String ip, String os, String browser, String behavior, String moduleUid, String otherData, RedisUtil redisUtil) {
        this.userUid = userUid;
        this.ip = ip;
        this.os = os;
        this.browser = browser;
        this.behavior = behavior;
        this.moduleUid = moduleUid;
        this.otherData = otherData;
        this.redisUtil = redisUtil;
    }


    /**
     * 遇见语录：Request请求结束后，异步线程拿主进程里数据会出现空指针异常【因此不能在子线程里操作Request对象】
     * 这个问题不一定每一次都出现，可能100次中有4~5次出现空指针异常，也就是说当 异步线程执行比主线程快 是没问题的
     * 这里为了避免这种情况，将避免在异步线程中操作Request对象
     */
    @Override
    protected void onRun() {
        System.out.println("============" + Thread.currentThread().getName());
        WebVisit webVisit = new WebVisit();
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
