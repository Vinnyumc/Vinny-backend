package com.vinny.backend.Shop.dto;

import lombok.*;

import java.util.List;

@Getter
public class ShopResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewDto {
        private Long id;
        private String name;
        private String description;
        private String status;
        private String openTime;
        private String closeTime;
        private String instagram;
        private String address;
        private String addressDetail;
        private Double latitude;
        private Double longitude;
        private String region;
        private List<ImageDto> images;
        private List<ShopVintageStyleDto> shopVintageStyleList;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageDto {
        private String url;
        private boolean isMain;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShopPreviewListDto {
        private List<PreviewDto> shops;
        private int totalPages;
        private long totalElements;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MapThumbnailDto {
        private Long id;
        private String name;
        private String openTime;
        private String closeTime;
        private String instagram;
        private String address;
        private String addressDetail;
        private Double latitude;
        private Double longitude;
        private String region;
        private List<ImageDto> images;
        private List<ShopVintageStyleDto> shopVintageStyleList;
    }
}
