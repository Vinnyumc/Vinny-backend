package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.mapping.UserWeeklyShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserShopForYouRepository extends JpaRepository<UserWeeklyShop, Long> {
    void deleteByUser_IdAndWeekStart(Long userId, LocalDate weekStart);
    List<UserWeeklyShop> findByUser_IdAndWeekStart(Long userId, LocalDate weekStart);

    void deleteByUser_IdAndWeekStartLessThan(Long userId, LocalDate weekStart);
}
