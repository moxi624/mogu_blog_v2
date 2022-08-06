package org.springframework.boot.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 定位bean 的位置
 * 目前支持 类注解 @Component @Configuration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional({CustomOnPropertyCondition.class})
public @interface CustomConditionalOnProperty {
    String[] value() default {};

    String prefix() default "spring.bean.register";

    String[] name() default {"basePackages"};

    String havingValue() default "mogublog";

    boolean matchIfMissing() default false;
}
