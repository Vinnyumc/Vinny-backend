package com.vinny.backend.User.validation.validator;

import com.vinny.backend.User.repository.VintageStyleRepository;
import com.vinny.backend.User.validation.annotation.ExistingVintageStyleIds;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExistingVintageStyleIdsValidator implements ConstraintValidator<ExistingVintageStyleIds, List<Long>> {
    private final VintageStyleRepository vintageStyleRepository;
    @Override
    public boolean isValid(List<Long> ids, ConstraintValidatorContext context) {
        if (ids == null || ids.isEmpty()) return true;
        Set<Long> uniqueIds = new HashSet<>(ids);
        return vintageStyleRepository.countByIdIn(uniqueIds) == uniqueIds.size();
    }
}