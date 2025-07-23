package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RegionRepository extends JpaRepository<Region, Long> {
    long countByIdIn(Set<Long> ids);

}
