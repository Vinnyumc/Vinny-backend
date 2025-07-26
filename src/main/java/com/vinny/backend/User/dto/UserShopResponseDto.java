package com.vinny.backend.User.dto;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.ShopImage;
import com.vinny.backend.User.domain.enums.UserShopStatus;
import com.vinny.backend.User.domain.mapping.UserShop;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserShopResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PreviewDto {
        private Long userId;
        private Long shopId;
        private UserShopStatus status;
        private Integer visitCount;
    }

    /**
     * 변환 메서드: UserShop → PreviewDto
     */
    public static PreviewDto toPreviewDto(UserShop userShop) {
        return PreviewDto.builder()
                .userId(userShop.getUser().getId())
                .shopId(userShop.getShop().getId())
                .status(userShop.getStatus())
                .visitCount(userShop.getVisitCount())
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PreviewShopDetailDto {
        private Long shopId;
        private String name;
        private String address;
        private String region;
        private List<String> styles;
        private List<String> imageUrls;

        public static PreviewShopDetailDto from(UserShop userShop) {
            Shop shop = userShop.getShop();

            return PreviewShopDetailDto.builder()
                    .shopId(shop.getId())
                    .name(shop.getName())
                    .address(shop.getAddress())
                    .region(shop.getRegion() != null ? shop.getRegion().getName() : null)
                    .styles(shop.getShopVintageStyleList().stream()
                            .map(s -> s.getVintageStyle().getName())
                            .collect(Collectors.toList()))
                    .imageUrls(shop.getShopImages().stream()
                            .map(ShopImage::getImageUrl)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}

