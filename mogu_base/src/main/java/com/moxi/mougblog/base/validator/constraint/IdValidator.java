package com.moxi.mougblog.base.validator.constraint;

import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.validator.annotion.NotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 这里有些冗余了，其实面对控制器的VO对象，应该全为String类型。
 * 作为后端程序员，不应该相信前端传递的任何参数，所以字符串类型也应该被识别。
 *
 * @author Andy Chen
 * @date 9/30/19 1:24 PM
 */
public class IdValidator implements ConstraintValidator<NotNull, String> {

    @Override
    public void initialize(NotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return true;
    }
}
