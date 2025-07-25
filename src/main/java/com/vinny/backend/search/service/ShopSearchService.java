package com.vinny.backend.search.service;


import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.search.dto.ShopResponse;
import com.vinny.backend.search.dto.StyleSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopSearchService {

    private final ShopRepository shopRepository;

    public Page<ShopResponse> searchByStyle(StyleSearchRequest request, Pageable pageable) {
        Page<Shop> shops;

        if (request.region() != null && !request.region().trim().isEmpty()) {
            shops = shopRepository.findByStyleAndRegion(
                    request.styleType(), request.region(), pageable);
        } else {
            shops = shopRepository.findByStyle(request.styleType(), pageable);
        }

        return shops.map(this::convertToResponse);
    }

    private ShopResponse convertToResponse(Shop shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .addressDetail(shop.getAddressDetail())
                .region(shop.getRegion() != null ? shop.getRegion().getName() : null)
                .styles(shop.getShopVintageStyleList().stream()
                        .map(style -> style.getVintageStyle().getName())
                        .collect(Collectors.toList()))
                .imageUrl(shop.getShopImages().isEmpty() ? null : shop.getShopImages().get(0).getImageUrl())
                .status(shop.getStatus().name())
                .build();
    }
}