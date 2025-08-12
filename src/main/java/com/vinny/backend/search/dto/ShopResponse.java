package com.vinny.backend.search.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ShopResponse(
        Long id,
        String name,
        String address,
        String region,
        List<String> styles,
        String imageUrl,
        String status
) {}
