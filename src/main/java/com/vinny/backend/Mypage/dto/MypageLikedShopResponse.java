package com.vinny.backend.Mypage.dto;


import java.util.List;

public record MypageLikedShopResponse(
        Long shopId,
        String name,
        String regionName,
        String address,
        String thumbnailUrl,
        List<String> vintageStyles
) {}

