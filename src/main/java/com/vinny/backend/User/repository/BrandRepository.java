package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    long countByIdIn(Set<Long> ids);

}
