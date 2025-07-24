package com.vinny.backend.User.validation.validator;

import com.vinny.backend.User.repository.VintageItemRepository;
import com.vinny.backend.User.validation.annotation.ExistingVintageItemIds;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExistingVintageItemIdsValidator implements ConstraintValidator<ExistingVintageItemIds, List<Long>> {
    private final VintageItemRepository vintageItemRepository;
    @Override
    public boolean isValid(List<Long> ids, ConstraintValidatorContext context) {
        if (ids == null || ids.isEmpty()) return true;
        Set<Long> uniqueIds = new HashSet<>(ids);
        return vintageItemRepository.countByIdIn(uniqueIds) == uniqueIds.size();
    }
}
