package com.vinny.backend.Shop.converter;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.ShopImage;
import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.dto.ShopVintageStyleDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.vinny.backend.Shop.domain.QShop.shop;

@Component
public class ShopConverter {

    public static Shop toEntity(ShopRequestDto.CreateDto dto) {
        return Shop.builder()
                .name(dto.getName())
                .intro(dto.getIntro())
                .description(dto.getDescription())
                .status(Status.valueOf(String.valueOf(dto.getStatus())))
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .instagram(dto.getInstagram())
                .address(dto.getAddress())
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

        List<ShopVintageStyleDto> vintageStyleDtos = shop.getShopVintageStyleList().stream()
                .map(style -> ShopVintageStyleDto.builder()
                        .id(style.getId())
                        .vintageStyleName(style.getVintageStyle() != null ? style.getVintageStyle().getName() : null)
                        .build())
                .collect(Collectors.toList());


        return ShopResponseDto.PreviewDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .intro(shop.getIntro())
                .description(shop.getDescription())
                .status(shop.getStatus().name())
                .openTime(shop.getOpenTime() != null ? shop.getOpenTime().toString() : null)
                .closeTime(shop.getCloseTime() != null ? shop.getCloseTime().toString() : null)
                .instagram(shop.getInstagram())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .logoImage(shop.getLogoImage())
                .region(shop.getRegion() != null ? shop.getRegion().getName() : null)
                .images(images)
                .shopVintageStyleList(vintageStyleDtos)
                .build();
    }

    public static ShopResponseDto.MapThumbnailDto toMapThumbnailDto(Shop shop) {
        List<ShopResponseDto.ImageDto> images = shop.getShopImages().stream()
                .map(img -> new ShopResponseDto.ImageDto(img.getImageUrl(), img.isMainImage()))
                .collect(Collectors.toList());

        List<ShopVintageStyleDto> vintageStyleDtos = shop.getShopVintageStyleList().stream()
                .map(style -> ShopVintageStyleDto.builder()
                        .id(style.getId())
                        .vintageStyleName(style.getVintageStyle() != null ? style.getVintageStyle().getName() : null)
                        .build())
                .collect(Collectors.toList());


        return ShopResponseDto.MapThumbnailDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .openTime(shop.getOpenTime() != null ? shop.getOpenTime().toString() : null)
                .closeTime(shop.getCloseTime() != null ? shop.getCloseTime().toString() : null)
                .instagram(shop.getInstagram())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .logoImage(shop.getLogoImage())
                .region(shop.getRegion() != null ? shop.getRegion().getName() : null)
                .images(images)
                .shopVintageStyleList(vintageStyleDtos)
                .build();
    }

    /**
     * Shop -> HomeForYouThumbnailDto
     */
    public ShopResponseDto.HomeForYouThumbnailDto toHomeForYouThumbnailDto(Shop s) {
        if (s == null) return null;

        // 대표 이미지: isMainImage == true 중 첫 번째
        ShopResponseDto.ImageDto mainImage = s.getShopImages().stream()
                .filter(ShopImage::isMainImage)
                .findFirst()
                .map(img -> new ShopResponseDto.ImageDto(img.getImageUrl(), img.isMainImage()))
                .orElse(null);

        // 빈티지 스타일 변환
        List<ShopVintageStyleDto> vintageStyleDtos = s.getShopVintageStyleList().stream()
                .map(style -> ShopVintageStyleDto.builder()
                        .id(style.getId())
                        .vintageStyleName(style.getVintageStyle() != null ? style.getVintageStyle().getName() : null)
                        .build())
                .collect(Collectors.toList());

        return ShopResponseDto.HomeForYouThumbnailDto.builder()
                .id(s.getId())
                .name(s.getName())
                .openTime(s.getOpenTime() != null ? s.getOpenTime().toString() : null)
                .closeTime(s.getCloseTime() != null ? s.getCloseTime().toString() : null)
                .instagram(s.getInstagram())
                .address(s.getAddress())
                .logoImage(s.getLogoImage())
                .images(mainImage) // 대표 이미지 하나만
                .shopVintageStyleList(vintageStyleDtos)
                .build();
    }

}
