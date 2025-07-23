package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    long countByIdIn(Set<Long> ids);

}
