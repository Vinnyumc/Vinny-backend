package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopRankingQueryRepository {
    Page<Shop> searchRankedByVisit(List<String> regionKeyword, List<String > styleName, Pageable pageable);
}
