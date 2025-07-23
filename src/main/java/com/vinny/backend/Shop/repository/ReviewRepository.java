package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
