package com.vinny.backend.Shop.dto;
import java.util.List;

public record ShopSearchResponseDto(
        List<ShopDto> shops
) {
    public record ShopDto(
            Long shopId,
            String name,
            String region,
            List<String> tags,
            String thumbnailUrl
    ) {}
}

