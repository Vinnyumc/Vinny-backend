package com.vinny.backend.User.validation.annotation;

import com.vinny.backend.User.validation.validator.RegionExistValidator;
import com.vinny.backend.User.validation.validator.VintageStyleExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VintageStyleExistValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistVintageStyle {
    String message() default "존재하지 않는 스타일입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
