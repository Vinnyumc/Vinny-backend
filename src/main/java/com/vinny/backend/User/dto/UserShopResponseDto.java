package com.vinny.backend.User.dto;

import com.vinny.backend.User.domain.enums.UserShopStatus;
import com.vinny.backend.User.domain.mapping.UserShop;
import lombok.*;

@Getter
public class UserShopResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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
}

