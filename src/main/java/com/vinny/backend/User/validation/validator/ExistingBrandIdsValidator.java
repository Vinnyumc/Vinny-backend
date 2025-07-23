package com.vinny.backend.User.validation.validator;

import com.vinny.backend.User.repository.BrandRepository;
import com.vinny.backend.User.validation.annotation.ExistingBrandIds;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExistingBrandIdsValidator implements ConstraintValidator<ExistingBrandIds, List<Long>> {
    private final BrandRepository brandRepository;
    @Override
    public boolean isValid(List<Long> ids, ConstraintValidatorContext context) {
        if (ids == null || ids.isEmpty()) return true;
        Set<Long> uniqueIds = new HashSet<>(ids);
        return brandRepository.countByIdIn(uniqueIds) == uniqueIds.size();
    }
}
