package com.moxi.mougblog.base.exception;

import cn.hutool.core.collection.CollectionUtil;
import com.moxi.mougblog.base.exception.exceptionType.ApiInvalidParamException;
import com.moxi.mougblog.base.global.Constants;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

/**
 * 抛出异常工具类
 *
 * @author 陌溪
 * @date 2019年12月4日22:47:18
 */
public class ThrowableUtils {
    /**
     * 校验参数正确,拼装字段名和值到错误信息
     *
     * @param result
     */
    public static void checkParamArgument(BindingResult result) {
        if (result != null && result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();
            if (CollectionUtil.isNotEmpty(errors)) {
                FieldError error = errors.get(0);
                String rejectedValue = Objects.toString(error.getRejectedValue(), "");
                String defMsg = error.getDefaultMessage();
                // 排除类上面的注解提示
                if (rejectedValue.contains(Constants.DELIMITER_TO)) {
                    // 自己去确定错误字段
                    sb.append(defMsg);
                } else {
                    if (Constants.DELIMITER_COLON.contains(defMsg)) {
                        sb.append(error.getField()).append(" ").append(defMsg);
                    } else {
                        sb.append(error.getField()).append(" ").append(defMsg);
                    }
                }
            } else {
                String msg = result.getAllErrors().get(0).getDefaultMessage();
                sb.append(msg);
            }
            throw new ApiInvalidParamException(sb.toString());
        }

    }
}
