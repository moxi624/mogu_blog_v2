package com.moxi.mogublog.admin.annotion.OperationLogger;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.moxi.mogublog.admin.global.RedisConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.commons.config.security.SecurityUser;
import com.moxi.mogublog.commons.entity.SysLog;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.holder.AbstractRequestAwareRunnable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 异步记录日志
 *
 * @author: 陌溪
 * @create: 2020-03-05-8:59
 */
public class SysLogHandle extends AbstractRequestAwareRunnable {

    /**
     * Redis工具列
     */
    RedisUtil redisUtil;

    /**
     * 参数列表
     */
    private String paramsJson;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法请求时间
     */
    private Date startTime;

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 请求类型
     */
    private String type;

    /**
     * 请求URL
     */
    private String requestUrl;

    private SecurityUser securityUser;

    /**
     * 构造函数
     *
     * @param ip
     * @param type
     * @param requestUrl
     * @param securityUser
     * @param paramsJson
     * @param classPath
     * @param methodName
     * @param operationName
     * @param startTime
     * @param redisUtil
     */
    public SysLogHandle(String ip, String type, String requestUrl, SecurityUser securityUser,
                        String paramsJson, String classPath,
                        String methodName, String operationName,
                        Date startTime, RedisUtil redisUtil) {
        this.ip = ip;
        this.type = type;
        this.requestUrl = requestUrl;
        this.securityUser = securityUser;
        this.paramsJson = paramsJson;
        this.classPath = classPath;
        this.methodName = methodName;
        this.operationName = operationName;
        this.startTime = startTime;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void onRun() {
        SysLog sysLog = new SysLog();
        //从Redis中获取IP来源
        String jsonResult = redisUtil.get(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses(SysConf.IP + SysConf.EQUAL_TO + ip, SysConf.UTF_8);
            if (StringUtils.isNotEmpty(addresses)) {
                sysLog.setIpSource(addresses);
                redisUtil.setEx(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            sysLog.setIpSource(jsonResult);
        }

        //设置请求信息
        sysLog.setIp(ip);

        //设置调用的类
        sysLog.setClassPath(classPath);

        //设置调用的方法
        sysLog.setMethod(methodName);

        //设置Request的请求方式 GET POST
        sysLog.setType(type);
        sysLog.setUrl(requestUrl);

        sysLog.setOperation(operationName);
        sysLog.setCreateTime(new Date());
        sysLog.setUpdateTime(new Date());

        sysLog.setUserName(securityUser.getUsername());
        sysLog.setAdminUid(securityUser.getUid());
        sysLog.setParams(paramsJson);
        Date endTime = new Date();
        Long spendTime = DateUtil.between(startTime, endTime, DateUnit.MS);
        // 计算请求接口花费的时间，单位毫秒
        sysLog.setSpendTime(spendTime);
        sysLog.insert();
    }
}
