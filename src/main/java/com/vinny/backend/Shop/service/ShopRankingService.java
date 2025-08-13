// src/main/java/com/vinny/backend/Shop/service/ShopRankingService.java
package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.ShopImage;
import com.vinny.backend.Shop.domain.mapping.ShopVintageStyle;
import com.vinny.backend.Shop.dto.ShopRankingResponse;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.VintageStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopRankingService {

    private final ShopRepository shopRepository;

    public List<ShopRankingResponse> getRanking(String region, String style, int page, int size) {
        Page<Shop> pageData = shopRepository.searchRankedByVisit(region, style, PageRequest.of(page, size));
        return pageData.getContent().stream()
                .map(this::toShopRankingResponse)
                .toList();
    }

    /** Shop -> ShopRankingResponse 변환 전용 컨버터 */
    private ShopRankingResponse toShopRankingResponse(Shop s) {
        String regionName = (s.getRegion() != null) ? s.getRegion().getName() : null;

        List<String> tags = s.getShopVintageStyleList().stream()
                .map(ShopVintageStyle::getVintageStyle)
                .filter(Objects::nonNull)
                .map(VintageStyle::getName)
                .toList();

        String thumbnailUrl = s.getShopImages().stream()
                .sorted(Comparator.comparing(ShopImage::isMainImage).reversed()) // 메인 이미지 우선
                .map(ShopImage::getImageUrl)
                .findFirst()
                .orElse(null);

        return new ShopRankingResponse(
                s.getId(),
                s.getName(),
                s.getAddress(),
                regionName,
                tags,
                thumbnailUrl
        );
    }
}
