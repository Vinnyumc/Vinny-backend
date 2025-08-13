package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopRankingQueryRepository {
    Page<Shop> searchRankedByVisit(String regionKeyword, String styleName, Pageable pageable);
}
