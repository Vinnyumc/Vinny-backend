package com.vinny.backend.Shop.dto;

import lombok.*;

import java.time.LocalTime;
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
        private String intro;
        private String description;
        private String status;
        private String openTime;
        private String closeTime;
        private String instagram;
        private String address;
        private Double latitude;
        private Double longitude;
        private String region;
        private String logoImage;
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
        private Double latitude;
        private Double longitude;
        private String region;
        private String logoImage;
        private List<ImageDto> images;
        private List<ShopVintageStyleDto> shopVintageStyleList;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeForYouThumbnailDto {
        // 필드 그대로 (String 유지)
        private Long id;
        private String name;
        private String openTime;
        private String closeTime;
        private String instagram;
        private String address;
        private String logoImage;
        private ImageDto images;
        private List<ShopVintageStyleDto> shopVintageStyleList;

        public HomeForYouThumbnailDto(Long id, String name,
                                      LocalTime openTime, LocalTime closeTime,
                                      String instagram, String address) {
            this.id = id;
            this.name = name;
            this.openTime = openTime != null ? openTime.toString() : null;
            this.closeTime = closeTime != null ? closeTime.toString() : null;
            this.instagram = instagram;
            this.address = address;
        }
    }

}
