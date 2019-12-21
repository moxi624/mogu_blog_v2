package com.moxi.mogublog.admin.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.moxi.mogublog.config.security.SecurityUser;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.xo.entity.ExceptionLog;
import com.moxi.mogublog.xo.entity.SysLog;
import com.moxi.mogublog.xo.service.ExceptionLogService;
import com.moxi.mogublog.xo.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 日志切面
 */
@Aspect
@Component
public class LoggerAspect {

    private SysLog sysLog;

    private ExceptionLog exceptionLog;

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

        //获取切入点参数

        //获取ip地址
        String ip = IpUtils.getIpAddr(request);
        //设置请求信息
        sysLog.setIp(ip);
        //设置调用的类
        sysLog.setClassPath(joinPoint.getTarget().getClass().getName());
        //设置调用的方法
        sysLog.setMethod(joinPoint.getSignature().getName());
        //设置Request的请求方式 GET POST
        sysLog.setType(request.getMethod());
        Object[] o = joinPoint.getArgs();
        String params = "";
        for (int a = 0; a < o.length; a++) {
            params = params + "参数" + (a + 1) + ":" + o[a] + ", ";
        }
        sysLog.setParams(params);

        sysLog.setUrl(request.getRequestURI().toString());

        sysLog.setOperation(operationLogger.value());
    }

    @AfterReturning(value = "pointcut(operationLogger)")
    public void doAfterReturning(OperationLogger operationLogger) {
        sysLog.setCreateTime(new Date());
        sysLog.setUpdateTime(new Date());
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysLog.setUserName(securityUser.getUsername());
        sysLog.setAdminUid(securityUser.getUid());
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

        //保存异常日志信息
        exceptionLogService.save(exceptionLog);
    }


//    @Autowired
//    private SysLogService SysLogService;
//
//    @Pointcut("@annotation(com.moxi.mogublog.admin.log.Logger)")
//    public void pointcut(){}
//
//    @Around("pointcut()")
//    public void around(ProceedingJoinPoint joinPoint){
//        try {
//            joinPoint.proceed(); // 执行方法
//        }catch (Throwable e){
//        }
//        saveSysLog(joinPoint);
//    }
//
//    private void saveSysLog(ProceedingJoinPoint joinPoint){
//        SysLog sysLog = getSysLog(joinPoint);
//        SysLogService.save(sysLog);
//    }
//
//    private SysLog getSysLog(ProceedingJoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        // 方法
//        Method method = methodSignature.getMethod();
//        // 方法上的注解
//        Logger loggerAnnotation = method.getAnnotation(Logger.class);
//
//        SysLog sysLog = new SysLog();
//
//        if (loggerAnnotation != null){
//            // 注解上的描述
//            sysLog.setOperation(loggerAnnotation.value());
//        }
//        // 方法名
//        String className = joinPoint.getTarget().getClass().getName();
//        String methonName = methodSignature.getName();
//        sysLog.setMethod(className + "." + methonName + "()");
//
//        // 方法参数值
//        Object[] args = joinPoint.getArgs();
//        // 方法参数名
//        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
//        String[] paramNames = discoverer.getParameterNames(method);
//        if (args != null && paramNames != null){
//            String params = "";
//            for (int i = 0; i < args.length; i++){
//                params += " " + paramNames[i] + ": " + args[i];
//            }
//            sysLog.setParams(params);
//        }
//        sysLog.setUserName("admin");
//        sysLog.setLogDate(new Date());
//        return sysLog;
//    }


}
