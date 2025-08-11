package com.vinny.backend.Mypage.dto;

public record MypageUserProfileDto (
        Long userId,
        String nickname,
        String profileImage,
        String comment
)
{}
