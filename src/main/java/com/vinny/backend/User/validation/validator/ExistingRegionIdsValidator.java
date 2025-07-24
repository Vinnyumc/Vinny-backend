package com.vinny.backend.User.validation.validator;

import com.vinny.backend.User.repository.RegionRepository;
import com.vinny.backend.User.validation.annotation.ExistingRegionIds;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExistingRegionIdsValidator implements ConstraintValidator<ExistingRegionIds, List<Long>> {
    private final RegionRepository regionRepository;
    @Override
    public boolean isValid(List<Long> ids, ConstraintValidatorContext context) {
        if (ids == null || ids.isEmpty()) return true;
        Set<Long> uniqueIds = new HashSet<>(ids);
        return regionRepository.countByIdIn(uniqueIds) == uniqueIds.size();
    }
}
