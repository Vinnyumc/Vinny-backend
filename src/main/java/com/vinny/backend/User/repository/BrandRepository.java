package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
