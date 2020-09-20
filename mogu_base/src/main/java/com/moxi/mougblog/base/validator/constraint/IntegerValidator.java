package com.moxi.mougblog.base.validator.constraint;

import com.moxi.mougblog.base.validator.annotion.IntegerNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 判断Integer是否为空【校验器】
 *
 * @author 陌溪
 * @date 2019年12月4日13:16:06
 */
public class IntegerValidator implements ConstraintValidator<IntegerNotNull, Integer> {

    @Override
    public void initialize(IntegerNotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return true;
    }
}
