package com.moxi.mogublog.admin.log;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.moxi.mogublog.config.security.SecurityUser;
import com.moxi.mogublog.utils.AopUtils;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.ExceptionLog;
import com.moxi.mogublog.xo.entity.SysLog;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mogublog.xo.service.SysLogService;
import com.moxi.mougblog.base.global.BaseSysConf;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日志切面
 */
@Aspect
@Component
@Slf4j
public class LoggerAspect {

    private SysLog sysLog;

    private ExceptionLog exceptionLog;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 接口请求开始时间
     */
    private Date startTime;


    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ExceptionLogService exceptionLogService;

    @Autowired
    private HttpServletRequest request;


    @Pointcut(value = "@annotation(operationLogger)")
    public void pointcut(OperationLogger operationLogger) {

    }

    /**
     * 前置通知
     *
     * @param joinPoint
     * @param operationLogger
     */
    @Before(value = "pointcut(operationLogger)")
    public void doBefore(JoinPoint joinPoint, OperationLogger operationLogger) {
        sysLog = new SysLog();

        // 设置接口开始请求时间
        startTime = new Date();

        try {
            String classType = joinPoint.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(classType);
            String clazzName = clazz.getName();

            //获取方法名称
            String methodName = joinPoint.getSignature().getName();

            Object[] args = joinPoint.getArgs();

            // 获取参数名称和值
            StringBuffer sb = AopUtils.getNameAndArgs(this.getClass(), clazzName, methodName, args);
            sysLog.setParams(sb.toString());

        } catch (Exception e) {
            log.error(e.getMessage());
        }


        //获取ip地址
        String ip = IpUtils.getIpAddr(request);

        //从Redis中获取IP来源
        String jsonResult = stringRedisTemplate.opsForValue().get("IP_SOURCE:" + ip);
        if(StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses("ip=" + ip, "utf-8");
            if(StringUtils.isNotEmpty(addresses)) {
                sysLog.setIpSource(addresses);
                stringRedisTemplate.opsForValue().set("IP_SOURCE" + BaseSysConf.REDIS_SEGMENTATION + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            sysLog.setIpSource(jsonResult);
        }

        //设置请求信息
        sysLog.setIp(ip);

        //设置调用的类
        sysLog.setClassPath(joinPoint.getTarget().getClass().getName());
        //设置调用的方法
        sysLog.setMethod(joinPoint.getSignature().getName());
        //设置Request的请求方式 GET POST
        sysLog.setType(request.getMethod());

        sysLog.setUrl(request.getRequestURI().toString());

        sysLog.setOperation(operationLogger.value());
        sysLog.setCreateTime(new Date());
        sysLog.setUpdateTime(new Date());
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysLog.setUserName(securityUser.getUsername());
        sysLog.setAdminUid(securityUser.getUid());


    }

    @AfterReturning(value = "pointcut(operationLogger)")
    public void doAfterReturning(OperationLogger operationLogger) {
        Date endTime = new Date();
        Long spendTime = DateUtil.between(startTime, endTime, DateUnit.MS);
        // 计算请求接口花费的时间，单位毫秒
        sysLog.setSpendTime(spendTime);
        sysLogService.save(sysLog);
    }

    @AfterThrowing(value = "pointcut(operationLogger)", throwing = "e")
    public void doAfterThrowing(OperationLogger operationLogger, Throwable e) {
        exceptionLog = new ExceptionLog();
        //设置异常信息
        exceptionLog.setCreateTime(new Date());
        exceptionLog.setExceptionJson(JSON.toJSONString(e,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));
        exceptionLog.setExceptionMessage(e.getMessage());

        if (sysLog != null) {
            exceptionLog.setIp(sysLog.getIp());
            exceptionLog.setIpSource(sysLog.getIpSource());
            exceptionLog.setMethod(sysLog.getMethod());
            exceptionLog.setParams(sysLog.getParams());
            exceptionLog.setOperation(sysLog.getOperation());
        }

        //保存异常日志信息
        exceptionLogService.save(exceptionLog);
    }

}
