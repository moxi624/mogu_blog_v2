package com.moxi.mougblog.base.holder;

import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
import com.moxi.mougblog.base.global.BaseMessageConf;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.global.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * RequestHolder
 *
 * @author 陌溪
 * @date 2020年2月27日08:44:28
 */
@Slf4j
public class RequestHolder {

    /**
     * 获取request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        log.debug("getRequest -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取Response
     *
     * @return HttpServletRequest
     */
    public static HttpServletResponse getResponse() {
        log.debug("getResponse -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return null;
        }
        return servletRequestAttributes.getResponse();
    }

    /**
     * 获取session
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        log.debug("getSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        HttpServletRequest request = null;
        if (null == (request = getRequest())) {
            return null;
        }
        return request.getSession();
    }

    /**
     * 获取session的Attribute
     *
     * @param name session的key
     * @return Object
     */
    public static Object getSession(String name) {
        log.debug("getSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return null;
        }
        return servletRequestAttributes.getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 添加session
     *
     * @param name
     * @param value
     */
    public static void setSession(String name, Object value) {
        log.debug("setSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return;
        }
        servletRequestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 清除指定session
     *
     * @param name
     * @return void
     */
    public static void removeSession(String name) {
        log.debug("removeSession -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return;
        }
        servletRequestAttributes.removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取所有session key
     *
     * @return String[]
     */
    public static String[] getSessionKeys() {
        log.debug("getSessionKeys -- Thread id :{}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == servletRequestAttributes) {
            return null;
        }
        return servletRequestAttributes.getAttributeNames(RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取AdminUid
     *
     * @return
     */
    public static String getAdminUid() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(BaseSysConf.ADMIN_UID) != null) {
            return request.getAttribute(BaseSysConf.ADMIN_UID).toString();
        } else {
            return null;
        }
    }

    /**
     * 获取AdminToken
     *
     * @return
     */
    public static String getAdminToken() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(BaseSysConf.TOKEN) != null) {
            return request.getAttribute(BaseSysConf.TOKEN).toString();
        } else {
            return null;
        }
    }

    /**
     * 检查当前用户是否登录【未登录操作将抛出QueryException异常】
     */
    public static String checkLogin() {
        if (StringUtils.isEmpty(getAdminUid())) {
            log.error("用户未登录");
            throw new QueryException(ErrorCode.INVALID_TOKEN, BaseMessageConf.INVALID_TOKEN);
        }
        return getAdminUid();
    }
}
