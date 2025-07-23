package com.vinny.backend.search.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(
        Long id,
        String title,
        String content,
        UserSearchResponse author,
        List<String> images,
        List<String> styles,
        LocalDateTime createdAt,
        Integer totalImageCount,
        Integer likeCount
) {}