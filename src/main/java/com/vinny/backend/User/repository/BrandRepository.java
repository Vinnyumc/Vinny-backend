package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    long countByIdIn(Set<Long> ids);

    @Query("select b.name from Brand b")
    List<String> findAllBrandNames();

    List<Brand> findByNameContainingIgnoreCaseOrderByNameAsc(String name, Pageable pageable);

}
