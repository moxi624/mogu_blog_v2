package com.moxi.mougblog.base.validator.constraint;

import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.validator.annotion.Numeric;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 判断是否为数字【校验器】
 *
 * @author 陌溪
 * @date 2019年12月4日13:16:36
 */
public class NumericValidator implements ConstraintValidator<Numeric, String> {
    @Override
    public void initialize(Numeric constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || StringUtils.isBlank(value)) {
            return false;
        }
        if (!StringUtils.isNumeric(value)) {
            return false;
        }
        return true;
    }
}
