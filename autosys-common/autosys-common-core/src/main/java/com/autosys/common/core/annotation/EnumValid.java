package com.autosys.common.core.annotation;

import com.autosys.common.core.util.validator.EnumConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数枚举校验
 * @Author：jingqiu.wang
 * @Date：2022年10月28日 15点09分
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumConstraintValidator.class)
public @interface EnumValid {
    String[] value();
    String message() default "对应值非枚举字符属性";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
