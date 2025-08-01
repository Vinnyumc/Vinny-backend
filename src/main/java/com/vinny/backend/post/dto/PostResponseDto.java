package com.vinny.backend.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private List<PostDto> posts;
    private PageInfoDto pageInfo;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostDto {
        private Long postId;
        private AuthorDto author;
        private String content;
        private List<String> images;
        private LocalDateTime createdAt;
        private int likesCount;
        private boolean isLikedByMe;
        private ShopDto shop;
        private StyleDto style;
        private BrandDto brand; // nullable
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorDto {
        private Long userId;
        private String nickname;
        private String profileImageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShopDto {
        private Long shopId;
        private String shopName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StyleDto {
        private Long styleId;
        private String styleName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandDto {
        private Long brandId;
        private String brandName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageInfoDto {
        private int page;
        private int size;
        private int totalPages;
        private long totalElements;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePostResponse {
        private Long postId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostDetailResponseDto {
        private Long postId;
        private AuthorDto author;
        private String content;
        private List<String> images;
        private LocalDateTime createdAt;
        private int likesCount;
        private boolean isLikedByMe;
        private ShopDto shop;
        private List<StyleDto> styles;
        private BrandDto brand;
    }
}