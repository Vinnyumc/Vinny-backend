package com.vinny.backend.Mypage.dto;

import jakarta.validation.constraints.NotBlank;

public record MypageUpdateProfileImageRequest (
        @NotBlank(message = "이미지 URL은 필수입니다.")
        String imageUrl
) {}
