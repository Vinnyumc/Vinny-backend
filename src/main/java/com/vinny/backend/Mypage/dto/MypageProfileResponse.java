package com.vinny.backend.Mypage.dto;

public record MypageProfileResponse(
        Long userId,
        String nickname,
        String profileImage,
        String comment,
        int postCount,
        int likedShopCount,
        int savedCount
) {}
