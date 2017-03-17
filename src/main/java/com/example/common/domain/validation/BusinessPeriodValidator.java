package com.example.common.domain.validation;

import com.example.common.domain.model.BusinessPeriod;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BusinessPeriodValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return BusinessPeriod.class.equals(clazz);
    }

    public void validate(Object object, Errors errors) {
        BusinessPeriod period = (BusinessPeriod) object;
    }
}
