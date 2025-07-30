package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Review;
import com.vinny.backend.Shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByShop(Shop shop);

}
