package com.vinny.backend.Shop.dto;

import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.User.validation.annotation.ExistRegion;
import com.vinny.backend.User.validation.annotation.ExistVintageStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class ShopRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateDto {

        @NotBlank(message = "가게 이름은 필수입니다.")
        private String name;

        private String description;

        @NotNull(message = "상태는 필수 값입니다.")
        private Status status;

        private LocalTime openTime;

        private LocalTime closeTime;

        private String instagram;

        @NotBlank(message = "주소는 필수입니다.")
        private String address;

        private String addressDetail;

        @NotNull(message = "위도는 필수입니다.")
        private Double latitude;

        @NotNull(message = "경도는 필수입니다.")
        private Double longitude;

        @NotNull(message = "지역 ID는 필수입니다.")
        @ExistRegion(message = "유효하지 않은 지역입니다.")
        private Long regionId;

        @ExistVintageStyle(message = "유효하지 않은 빈티지 스타일입니다.")
        private List<Long> vintageStyleIds;

        @NotNull(message = "이미지 목록은 필수입니다.")
        private List<ShopImageDto> images;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ShopImageDto {

        @NotBlank(message = "이미지 URL은 필수입니다.")
        private String imageUrl;

        private boolean isMainImage;
    }
}