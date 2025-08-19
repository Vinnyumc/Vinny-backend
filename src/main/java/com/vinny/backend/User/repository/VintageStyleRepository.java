package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.VintageStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface VintageStyleRepository extends JpaRepository<VintageStyle, Long> {
    Optional<VintageStyle> findByName(String name);

    @Query("SELECT v FROM VintageStyle v WHERE v.name LIKE CONCAT('%', :name)")
    Optional<VintageStyle> findByNameSuffix(@Param("name") String name);

    boolean existsByName(String name);

    long countByIdIn(Set<Long> ids);
}
