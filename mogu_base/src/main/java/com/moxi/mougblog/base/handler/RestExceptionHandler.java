package com.moxi.mougblog.base.handler;

import com.moxi.mougblog.base.global.ErrorCode;
import com.moxi.mougblog.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: RestApi参数校验异常处理
 * @author: 陌溪
 * @date: 2019年12月4日22:48:18
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public Result requestNotReadable(HttpMessageNotReadableException ex) {
        log.error("异常类 HttpMessageNotReadableException {},", ex.getMessage());
        return Result.createWithErrorMessage("参数异常", ErrorCode.PARAM_INCORRECT);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public Result requestTypeMismatch(TypeMismatchException ex) {
        log.error("异常类 TypeMismatchException {},", ex.getMessage());
        return Result.createWithErrorMessage("参数异常", ErrorCode.PARAM_INCORRECT);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public Result requestMissingServletRequest(MissingServletRequestParameterException ex) {
        log.error("异常类 MissingServletRequestParameterException {},", ex.getMessage());
        return Result.createWithErrorMessage("参数异常", ErrorCode.PARAM_INCORRECT);
    }

    /**
     * 405错误
     *
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Result request405() {
        log.error("异常类 HttpRequestMethodNotSupportedException ");
        return Result.createWithErrorMessage("参数异常", ErrorCode.PARAM_INCORRECT);
    }

    /**
     * 415错误
     *
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public Result request415(HttpMediaTypeNotSupportedException ex) {
        log.error("异常类 HttpMediaTypeNotSupportedException {}", ex.getMessage());
        return Result.createWithErrorMessage("参数异常", ErrorCode.PARAM_INCORRECT);
    }
}
