package com.moxi.mogublog.commons.handler;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import com.moxi.mogublog.utils.ResultUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局feign异常处理
 * @author: 陌溪
 * @date: 2020-10-03-21:56
 */
@Component
public class FeignBlockHandler implements UrlBlockHandler {

    @Override
    public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String message = null;
        if (e instanceof FlowException) {
            message = "接口被限流了";
        } else if (e instanceof DegradeException) {
            message = "接口被降级了";
        } else if (e instanceof ParamFlowException) {
            message = "接口被热点限流了";
        } else if (e instanceof AuthorityException) {
            message = "接口被授权规则限制访问了";
        } else if (e instanceof SystemBlockException) {
            message = "接口被系统规则限制了";
        }
        String result = ResultUtil.result("error", message);
        httpServletResponse.getWriter().write(result);
    }
}
