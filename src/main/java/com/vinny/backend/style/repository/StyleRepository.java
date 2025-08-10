package com.vinny.backend.style.repository;

import com.vinny.backend.User.domain.VintageStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StyleRepository extends JpaRepository<VintageStyle, Long> {
    List<VintageStyle> findAllByOrderByNameAsc();
}
