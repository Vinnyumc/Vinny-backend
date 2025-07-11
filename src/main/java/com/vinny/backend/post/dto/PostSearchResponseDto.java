package com.vinny.backend.post.dto;
import java.util.List;

public record PostSearchResponseDto(
        List<PostDto> posts
) {
    public record PostDto(
            Long postId,
            String title,
            String imageUrl,
            Integer likeCount,
            Boolean isLiked,
            List<String> tags,
            String author
    ) {}
}
