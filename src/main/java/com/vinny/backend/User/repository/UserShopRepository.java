package com.vinny.backend.User.repository;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.UserShopStatus;
import com.vinny.backend.User.domain.mapping.UserShop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserShopRepository extends JpaRepository<UserShop, Long> {
    Optional<UserShop> findByUserAndShop(User user, Shop shop);
    List<UserShop> findAllByUserAndSaved(User user, boolean saved);

    int countByUserId(Long userId);

    List<UserShop> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"shop", "user"})
    Optional<UserShop> findByShop_idAndUser_id(Long shopId, Long userId);
}
