package com.moxi.mougblog.base.validator.annotion;

import com.moxi.mougblog.base.validator.Messages;
import com.moxi.mougblog.base.validator.constraint.NumericValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 判断是否为数字【注解】
 *
 * @author 陌溪
 * @date 2019年12月4日13:14:24
 */
@Target({TYPE, ANNOTATION_TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NumericValidator.class})
public @interface Numeric {

    String message() default Messages.CK_NUMERIC_DEFAULT;

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
