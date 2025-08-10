package com.vinny.backend.brand.dto;

import com.vinny.backend.User.domain.Brand;

public record BrandDto(Long brandId, String brandName, String brandImage) {
    public static BrandDto from(Brand brand) {
        return new BrandDto(
                brand.getId(),
                brand.getName(),
                brand.getBrandImage()
        );
    }
}
