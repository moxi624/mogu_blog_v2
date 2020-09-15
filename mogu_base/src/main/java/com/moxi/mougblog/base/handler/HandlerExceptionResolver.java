package com.moxi.mougblog.base.handler;

import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mougblog.base.exception.ErrorMessageUtil;
import com.moxi.mougblog.base.exception.exceptionType.*;
import com.moxi.mougblog.base.global.BaseMessageConf;
import com.moxi.mougblog.base.global.ErrorCode;
import com.moxi.mougblog.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 异常处理类
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
//        String errorCode = exception instanceof BusinessException ? ((BusinessException) exception).getCode() : ErrorCode.OPERATION_FAIL;
//        String message = ErrorMessageUtil.getErrorMessage(errorCode, null);

        String errorCode = ErrorCode.OPERATION_FAIL;
        String message = BaseMessageConf.OPERATION_FAIL;

        // 自定义业务相关异常
        if (exception instanceof BusinessException) {
            errorCode = ((BusinessException) exception).getCode();
            message = exception.getMessage();
        }

        // 自定义参数校验相关的异常
        if (exception instanceof ApiInvalidParamException) {
            message = ErrorMessageUtil.getErrorMessage(errorCode, null);
        }

        // 自定义登录相关的异常
        if(exception instanceof LoginException) {
            errorCode = ((LoginException) exception).getCode();
            message = exception.getMessage();
        }

        // 自定义查询相关的异常
        if(exception instanceof QueryException) {
            errorCode = ((QueryException) exception).getCode();
            message = exception.getMessage();
        }

        // 自定义更新操作相关的异常
        if(exception instanceof UpdateException) {
            errorCode = ((UpdateException) exception).getCode();
            message = exception.getMessage();
        }

        // 自定义新增操作相关的异常
        if(exception instanceof AddException) {
            errorCode = ((AddException) exception).getCode();
            message = exception.getMessage();
        }

        // 自定义删除操作相关的异常
        if(exception instanceof DeleteException) {
            errorCode = ((DeleteException) exception).getCode();
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