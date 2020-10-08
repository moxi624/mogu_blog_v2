package com.moxi.mogublog.commons.handler;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.moxi.mogublog.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局Sentinel异常处理器
 *
 * @author: 陌溪
 * @date: 2020-10-03-21:56
 */
@Component
@Slf4j
public class FeignBlockHandler implements UrlBlockHandler {

    @Override
    public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        String message = null;
        if (e instanceof FlowException) {
            message = "请求接口被限流";
        } else if (e instanceof DegradeException) {
            message = "请求接口被降级";
        } else if (e instanceof ParamFlowException) {
            message = "请求接口被热点限流";
        } else if (e instanceof AuthorityException) {
            message = "请求接口被授权规则限制访问";
        } else if (e instanceof SystemBlockException) {
            message = "请求接口被系统规则限制";
        }
        log.error("{}, 请求路径为:{}", message, requestURL);
        String result = ResultUtil.errorWithMessage(message);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(result);
    }
}
