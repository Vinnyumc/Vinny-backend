package com.vinny.backend.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class UserShopRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeDto {
        @NotNull(message = "ShopId는 필수입니다.")
        private Long shopId;
    }
}
