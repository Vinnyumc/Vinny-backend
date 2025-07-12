package com.vinny.backend.post.converter;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.PostImage;
import com.vinny.backend.post.dto.PostResponseDto;
import com.vinny.backend.post.dto.PostResponseDto.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDto toResponse(Page<Post> postPage, Long currentUserId) {
        List<PostDto> postDtos = postPage.getContent().stream()
                .map(post -> toPostDto(post, currentUserId))
                .collect(Collectors.toList());

        return PostResponseDto.builder()
                .posts(postDtos)
                .pageInfo(PageInfoDto.builder()
                        .page(postPage.getNumber())
                        .size(postPage.getSize())
                        .totalPages(postPage.getTotalPages())
                        .totalElements(postPage.getTotalElements())
                        .build())
                .build();
    }

    private static PostDto toPostDto(Post post, Long currentUserId) {
        return PostDto.builder()
                .postId(post.getId())
                .author(AuthorDto.builder()
                        .userId(post.getUser().getId())
                        .nickname(post.getUser().getNickname())
                        .profileImageUrl(post.getUser().getProfileImage())
                        .build())
                .content(post.getContent())
                .images(post.getPostImages().stream()
                        .map(PostImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .likesCount(post.getLikeCount())
                .isLikedByMe(post.isLikedBy(currentUserId))
                .shop(ShopDto.builder()
                        .shopId(post.getShop().getId())
                        .shopName(post.getShop().getName())
                        .build())
                .style(StyleDto.builder()
                        .styleId(post.getStyle().getId())
                        .styleName(post.getStyle().getName())
                        .build())
                .brand(post.getBrand() != null ? BrandDto.builder()
                        .brandId(post.getBrand().getId())
                        .brandName(post.getBrand().getName())
                        .build() : null)
                .build();
    }
}