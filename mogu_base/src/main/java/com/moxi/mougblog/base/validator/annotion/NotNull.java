package com.moxi.mougblog.base.validator.annotion;

import com.moxi.mougblog.base.validator.constraint.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 这里有些冗余了，其实面对控制器的VO对象，应该全为String类型。
 * 作为后端程序员，不应该相信前端传递的任何参数，所以字符串类型也应该被识别。
 *
 * @author 陌溪
 * @date 2019年12月4日13:13:51
 */
@Target({TYPE, ANNOTATION_TYPE, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IdValidator.class})
public @interface NotNull {

    boolean required() default true;

    String message() default "";

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
