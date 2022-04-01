package com.moxi.mogublog.web.annotion.requestLimit;

import com.moxi.mogublog.utils.AspectUtil;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.web.global.RedisConf;
import com.moxi.mougblog.base.global.ECode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * RequestLimitAspect 请求限制切面实现
 *
 * @author: 陌溪
 * @create: 2020-03-06-19:05
 */
@Aspect
@RefreshScope
@Component
@Slf4j
public class RequestLimitAspect {

    private final String POINT = "execution(* com.moxi.mogublog.web.restapi..*.*(..))";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RequestLimitConfig requestLimitConfig;

    @Pointcut(POINT)
    public void pointcut() {

    }

    /**
     * 方法前执行
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 判断是否开启了接口请求限制
        if (requestLimitConfig.getStart()) {
            ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = attribute.getRequest();

            //获取IP
            String ip = IpUtils.getIpAddr(request);

            //获取请求路径
            String url = request.getRequestURL().toString();

            //获取方法名称
            String methodName = point.getSignature().getName();

            String key = RedisConf.REQUEST_LIMIT + RedisConf.SEGMENTATION + ip + RedisConf.SEGMENTATION + methodName;

            Method currentMethod = AspectUtil.INSTANCE.getMethod(point);

            //查看接口是否有RequestLimit注解，如果没有则按yml的值全局验证
            if (currentMethod.isAnnotationPresent(RequestLimit.class)) {
                //获取注解
                RequestLimit requestLimit = currentMethod.getAnnotation(RequestLimit.class);
                boolean checkResult = checkWithRedis(requestLimit.amount(), requestLimit.time(), key);
                if (checkResult) {
                    log.info("requestLimited," + "[用户ip:{}],[访问地址:{}]超过了限定的次数[{}]次", ip, url, requestLimit.amount());
                    return ResultUtil.result(ECode.REQUEST_OVER_LIMIT, "接口请求过于频繁");
                }
                return point.proceed();
            }
            boolean checkResult = checkWithRedis(requestLimitConfig.getAmount(), requestLimitConfig.getTime(), key);
            if (checkResult) {
                log.info("requestLimited," + "[用户ip:{}],[访问地址:{}]超过了限定的次数[{}]次", ip, url, requestLimitConfig.getAmount());
                return ResultUtil.result(ECode.REQUEST_OVER_LIMIT, "接口请求过于频繁");
            }
        }
        return point.proceed();
    }

    /**
     * 以redis实现请求记录
     *
     * @param amount 请求次数
     * @param time   时间段
     * @param key
     * @return
     */
    private boolean checkWithRedis(int amount, long time, String key) {
        long count = redisUtil.incrBy(key, 1);
        if (count == 1) {
            redisUtil.expire(key, time, TimeUnit.MILLISECONDS);
        }
        if (count <= amount) {
            return false;
        }
        return true;
    }
}
