package com.vinny.backend.post.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        Long postId,
        AuthorDto author,
        String content,
        List<String> images,
        LocalDateTime createdAt,
        int likesCount,
        boolean isLikedByMe,
        ShopDto shop,
        StyleDto style,
        BrandDto brand
) {}
