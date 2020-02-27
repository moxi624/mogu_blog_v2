package com.moxi.mogublog.web.log;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.moxi.mogublog.config.security.SecurityUser;
import com.moxi.mogublog.utils.AopUtils;
import com.moxi.mogublog.utils.AspectUtil;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.xo.entity.ExceptionLog;
import com.moxi.mogublog.xo.entity.SysLog;
import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mogublog.xo.service.SysLogService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.PlatformEnum;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.util.RequestUtil;
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
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
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
    private WebVisitService webVisitService;


    @Pointcut(value = "@annotation(bussinessLog)")
    public void pointcut(BussinessLog bussinessLog) {

    }

    @Around(value = "pointcut(bussinessLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, BussinessLog bussinessLog) throws Throwable {

        System.out.println("进来了");

        //先执行业务
        Object result = joinPoint.proceed();

        try {
            // 日志手机
            handle(joinPoint);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void handle(ProceedingJoinPoint point) throws Exception {

        HttpServletRequest request = RequestHolder.getRequest();

        Method currentMethod = AspectUtil.INSTANCE.getMethod(point);
        //获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);

        boolean save = annotation.save();

        EBehavior behavior = annotation.behavior();

        String bussinessName = AspectUtil.INSTANCE.parseParams(point.getArgs(), annotation.value());

        String ua = RequestUtil.getUa();

        log.info("{} | {} - {} {} - {}", bussinessName, IpUtils.getIpAddr(request), RequestUtil.getMethod(), RequestUtil.getRequestUrl(), ua);
        if (!save) {
            return;
        }


        String classType = point.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();

        //获取方法名称
        String methodName = point.getSignature().getName();

        Object[] args = point.getArgs();

        // 获取参数名称和值
        Map<String, Object> nameAndArgsMap = AopUtils.getNameAndArgsMap(this.getClass(), clazzName, methodName, args);

        Map<String, String> result = EBehavior.getModuleAndOtherData(behavior, nameAndArgsMap, bussinessName);

        if(result != null) {
            String userUid = "";
            if(request.getAttribute(SysConf.USER_UID) != null) {
                userUid = request.getAttribute(SysConf.USER_UID).toString();
            }
            webVisitService.addWebVisit(userUid, request, behavior.getBehavior(), result.get(SysConf.MODULE_UID), result.get(SysConf.OTHER_DATA));
        }
    }
}
