package com.vinny.backend.User.validation.annotation;

import com.vinny.backend.User.validation.validator.ExistingVintageStyleIdsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingVintageStyleIdsValidator.class) // 검증기 이름 변경
public @interface ExistingVintageStyleIds {
    String message() default "존재하지 않는 빈티지 스타일 ID가 포함되어 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}