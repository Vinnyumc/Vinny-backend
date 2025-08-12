package com.vinny.backend.Shop.repository;

import java.util.List;

public interface ShopQueryRepository {
    List<Long> findMatchedShopIdsRandomByUser(Long userId);
}
