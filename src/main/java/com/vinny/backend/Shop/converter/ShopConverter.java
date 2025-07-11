package com.vinny.backend.Shop.converter;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;

public class ShopConverter {

    public static Shop toEntity(ShopRequestDto.CreateDto dto) {
        return Shop.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(Status.valueOf(String.valueOf(dto.getStatus())))
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .instagram(dto.getInstagram())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }


    public static ShopResponseDto.PreviewDto toResponseDto(Shop shop) {
        return new ShopResponseDto.PreviewDto(shop.getId(),shop.getName());
    }

}
