package com.vinny.backend.User.dto;

import java.time.LocalDateTime;

public record UserPostSummaryDto(
    Long postId,
    //String content,
    String imageUrl,
    LocalDateTime createdAt
) {}
