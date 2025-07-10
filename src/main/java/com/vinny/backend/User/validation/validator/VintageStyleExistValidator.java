package com.vinny.backend.User.validation.validator;


import com.vinny.backend.User.repository.RegionRepository;
import com.vinny.backend.User.repository.VintageStyleRepository;
import com.vinny.backend.User.validation.annotation.ExistRegion;
import com.vinny.backend.User.validation.annotation.ExistVintageStyle;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VintageStyleExistValidator implements ConstraintValidator<ExistVintageStyle, List<Long>> {

    private final VintageStyleRepository vintageStyleRepository;

    @Override
    public boolean isValid(List<Long> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return true;
        }

        return values.stream()
                .allMatch(vintageStyleRepository::existsById);
    }
}

