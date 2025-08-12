package com.vinny.backend.Mypage.dto;

public record MypageUserBackgroundDto (
        Long userId,
        String nickname,
        String profileImage,
        String comment
)
{}