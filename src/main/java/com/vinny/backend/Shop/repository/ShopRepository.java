package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
