package com.vinny.backend.post.converter;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.PostImage;
import com.vinny.backend.post.domain.mapping.PostBrandHashtag;
import com.vinny.backend.post.domain.mapping.PostShopHashtag;
import com.vinny.backend.post.domain.mapping.PostStyleHashtag;
import com.vinny.backend.post.dto.PostResponseDto.*;

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
                .content(post.getContent())
                .images(post.getImages().stream()
                        .map(PostImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .likesCount(post.getLikes().size())
                .isLikedByMe(post.getLikes().stream()
                        .anyMatch(like -> like.getUser().getId().equals(currentUserId)))
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
}