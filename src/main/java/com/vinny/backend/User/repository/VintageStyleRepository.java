package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.VintageStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VintageStyleRepository extends JpaRepository<VintageStyle, Long> {
    Optional<VintageStyle> findByName(String name);
    boolean existsByName(String name);
}
