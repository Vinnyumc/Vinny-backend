package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.VintageItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface VintageItemRepository extends JpaRepository<VintageItem, Long> {
    long countByIdIn(Set<Long> ids);

}
