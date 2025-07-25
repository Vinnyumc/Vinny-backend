package com.vinny.backend.Shop.converter;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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


    public static ShopResponseDto.ShopPreviewListDto toShopPreviewListDto(Page<Shop> shopPage) {
        List<ShopResponseDto.PreviewDto> previews = shopPage.getContent().stream()
                .map(ShopConverter::toPreviewDto)
                .collect(Collectors.toList());

        return new ShopResponseDto.ShopPreviewListDto(
                previews,
                shopPage.getTotalPages(),
                shopPage.getTotalElements()
        );
    }

    public static ShopResponseDto.PreviewDto toPreviewDto(Shop shop) {
        List<ShopResponseDto.ImageDto> images = shop.getShopImages().stream()
                .map(img -> new ShopResponseDto.ImageDto(img.getImageUrl(), img.isMainImage()))
                .collect(Collectors.toList());

        return ShopResponseDto.PreviewDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .status(shop.getStatus().name())
                .openTime(shop.getOpenTime() != null ? shop.getOpenTime().toString() : null)
                .closeTime(shop.getCloseTime() != null ? shop.getCloseTime().toString() : null)
                .instagram(shop.getInstagram())
                .address(shop.getAddress())
                .addressDetail(shop.getAddressDetail())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .region(shop.getRegion() != null ? shop.getRegion().getName() : null)
                .images(images)
                .build();
    }


    public static ShopResponseDto.PreviewDto toResponseDto(Shop shop) {
        List<ShopResponseDto.ImageDto> images = shop.getShopImages().stream()
                .map(img -> new ShopResponseDto.ImageDto(img.getImageUrl(), img.isMainImage()))
                .collect(Collectors.toList());

        return ShopResponseDto.PreviewDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .status(shop.getStatus().name())
                .openTime(shop.getOpenTime() != null ? shop.getOpenTime().toString() : null)
                .closeTime(shop.getCloseTime() != null ? shop.getCloseTime().toString() : null)
                .instagram(shop.getInstagram())
                .address(shop.getAddress())
                .addressDetail(shop.getAddressDetail())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .region(shop.getRegion() != null ? shop.getRegion().getName() : null)
                .images(images)
                .build();
    }
}
