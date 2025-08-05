package com.vinny.backend.User.dto;

public record UserProfileDto(
        Long userId,
        String nickname,
        String comment
) {}