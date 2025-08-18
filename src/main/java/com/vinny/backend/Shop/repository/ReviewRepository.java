package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Review;
import com.vinny.backend.Shop.domain.Shop;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select distinct r from Review r " +
            "join fetch r.user u " +
            "left join fetch r.images i " +
            "where r.shop = :shop")
    List<Review> findAllByShopWithUserAndImages(@Param("shop") Shop shop);

}
