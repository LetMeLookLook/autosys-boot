package com.autosys.common.core.util.validator;

import com.autosys.common.core.annotation.EnumValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 参数枚举校验
 */
public class EnumConstraintValidator implements ConstraintValidator<EnumValid,String> {
    private String[] verification;

    @Override
    public void initialize(EnumValid anEnum) {
        verification = anEnum.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(verification==null){
            return true;
        }
        for(String c:verification){
            if(value.equals(c)){
                return true;
            }
        }
        return false;
    }
}
