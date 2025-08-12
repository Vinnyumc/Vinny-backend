package com.vinny.backend.post.converter;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.PostImage;
import com.vinny.backend.post.domain.mapping.PostBrandHashtag;
import com.vinny.backend.post.domain.mapping.PostShopHashtag;
import com.vinny.backend.post.domain.mapping.PostStyleHashtag;
import com.vinny.backend.post.dto.PostResponseDto.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostDto toDto(Post post, Long currentUserId) {
        return PostDto.builder()
                .postId(post.getId())
                .author(AuthorDto.builder()
                        .userId(post.getUser().getId())
                        .nickname(post.getUser().getNickname())
                        .profileImageUrl(post.getUser().getProfileImage())
                        .build())
                .title(post.getTitle())
                .content(post.getContent())
                .images(post.getImages().stream()
                        .map(PostImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .createdAtRelative(getRelativeTime(post.getCreatedAt()))
                .likesCount(post.getLikes().size())
                .isLikedByMe(post.getLikes().stream()
                        .anyMatch(like -> like.getUser().getId().equals(currentUserId)))
                .bookmarkedByMe(post.getBookmarks().stream()
                        .anyMatch(bookmark -> bookmark.getUser().getId().equals(currentUserId)))
                .shop(post.getShopHashtags().stream()
                        .findFirst()
                        .map(PostShopHashtag::getShop)
                        .map(shop -> ShopDto.builder()
                                .shopId(shop.getId())
                                .shopName(shop.getName())
                                .build())
                        .orElse(null))
                .style(post.getStyleHashtags().stream()
                        .findFirst()
                        .map(PostStyleHashtag::getVintageStyle)
                        .map(style -> StyleDto.builder()
                                .styleId(style.getId())
                                .styleName(style.getName())
                                .build())
                        .orElse(null))
                .brand(post.getBrandHashtags().stream()
                        .findFirst()
                        .map(PostBrandHashtag::getBrand)
                        .map(brand -> BrandDto.builder()
                                .brandId(brand.getId())
                                .brandName(brand.getName())
                                .build())
                        .orElse(null))
                .build();
    }

    public static PostDetailResponseDto toDetailDto(Post post, boolean isLikedByMe, int likesCount, boolean isBookmarkedByMe) {
        return PostDetailResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .author(AuthorDto.builder()
                        .userId(post.getUser().getId())
                        .nickname(post.getUser().getNickname())
                        .profileImageUrl(post.getUser().getProfileImage())
                        .build())
                .content(post.getContent())
                .images(post.getImages().stream()
                        .map(PostImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .createdAtRelative(getRelativeTime(post.getCreatedAt()))
                .likesCount(likesCount)
                .isLikedByMe(isLikedByMe)
                .bookmarkedByMe(isBookmarkedByMe)
                .shop(post.getShopHashtags().stream()
                        .findFirst()
                        .map(PostShopHashtag::getShop)
                        .map(shop -> ShopDto.builder()
                                .shopId(shop.getId())
                                .shopName(shop.getName())
                                .build())
                        .orElse(null))
                .styles(post.getStyleHashtags().stream()
                        .map(PostStyleHashtag::getVintageStyle)
                        .map(style -> StyleDto.builder()
                                .styleId(style.getId())
                                .styleName(style.getName())
                                .build())
                        .collect(Collectors.toList()))
                .brand(post.getBrandHashtags().stream()
                        .findFirst()
                        .map(PostBrandHashtag::getBrand)
                        .map(brand -> BrandDto.builder()
                                .brandId(brand.getId())
                                .brandName(brand.getName())
                                .build())
                        .orElse(null))
                .build();
    }
    private static String getRelativeTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + "초 전";
        }
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = hours / 24;
        if (days < 30) {
            return days + "일 전";
        }
        long months = days / 30;
        if (months < 12) {
            return months + "개월 전";
        }
        long years = months / 12;
        return years + "년 전";
    }
}