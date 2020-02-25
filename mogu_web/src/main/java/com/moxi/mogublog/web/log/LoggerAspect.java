package com.moxi.mogublog.web.log;

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
import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mogublog.xo.service.SysLogService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.global.BaseSysConf;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
    private HttpServletRequest request;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ExceptionLogService exceptionLogService;

    @Autowired
    private WebVisitService webVisitService;


    @Pointcut(value = "@annotation(userOperationLogger)")
    public void pointcut(UserOperationLogger userOperationLogger) {

    }

    @Around(value = "pointcut(userOperationLogger)")
    public Object doAround(ProceedingJoinPoint joinPoint, UserOperationLogger userOperationLogger) throws Throwable {

        System.out.println("进来了");

//        webVisitService.addWebVisit();
        //先执行业务
        Object result = joinPoint.proceed();

        return result;
    }



    @AfterThrowing(value = "pointcut(userOperationLogger)", throwing = "e")
    public void doAfterThrowing(UserOperationLogger userOperationLogger, Throwable e) {
//        exceptionLog = new ExceptionLog();
//        //设置异常信息
//        exceptionLog.setCreateTime(new Date());
//        exceptionLog.setExceptionJson(JSON.toJSONString(e,
//                SerializerFeature.DisableCircularReferenceDetect,
//                SerializerFeature.WriteMapNullValue));
//        exceptionLog.setExceptionMessage(e.getMessage());
//
//        if (sysLog != null) {
//            exceptionLog.setIp(sysLog.getIp());
//            exceptionLog.setIpSource(sysLog.getIpSource());
//            exceptionLog.setMethod(sysLog.getMethod());
//            exceptionLog.setParams(sysLog.getParams());
//            exceptionLog.setOperation(sysLog.getOperation());
//        }
//
//        //保存异常日志信息
//        exceptionLogService.save(exceptionLog);
    }

}
