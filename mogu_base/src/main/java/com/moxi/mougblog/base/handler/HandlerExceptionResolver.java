package com.moxi.mougblog.base.handler;

import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mougblog.base.exception.ApiInvalidParamException;
import com.moxi.mougblog.base.exception.BusinessException;
import com.moxi.mougblog.base.exception.ErrorMessageUtil;
import com.moxi.mougblog.base.global.ErrorConstants;
import com.moxi.mougblog.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Author: 陌溪
 * @Date: 2019年12月4日22:48:08
 */
@Slf4j
public class HandlerExceptionResolver implements org.springframework.web.servlet.HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        log.error("系统统一异常处理：", exception);

        // 若响应已响应或已关闭，则不操作
        if (response.isCommitted()) {
            return new ModelAndView();
        }

        // 组装错误提示信息
        String errorCode = exception instanceof BusinessException ? ((BusinessException) exception).getCode() : ErrorConstants.OPERATION_FAIL;
        String message = ErrorMessageUtil.getErrorMessage(errorCode, null);
        if (exception instanceof ApiInvalidParamException) {
            //定义错误编码
            //errorCode = 10001;
            message = exception.getMessage();
        }
        // 响应类型设置
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // 响应结果输出
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JsonUtils.objectToJson(Result.createWithErrorMessage(message, errorCode)));
        } catch (Exception e) {
            log.error("响应输出失败！原因如下：", e);
        }
        return new ModelAndView();
    }
}