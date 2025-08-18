package com.vinny.backend.Shop.converter;

import com.vinny.backend.Shop.domain.Review;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.dto.ReviewRequestDto;
import com.vinny.backend.Shop.dto.ReviewResponseDto;
import com.vinny.backend.User.domain.User;
import org.springframework.stereotype.Component;
import java.util.List;
import com.vinny.backend.Shop.domain.ReviewImage;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class ReviewConverter {

    public static Review toEntity(ReviewRequestDto.CreateDto dto, Shop shop, User user) {
        return Review.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .shop(shop)
                .user(user)
                .build();
    }

    public static ReviewResponseDto.PreviewDto toPreviewDto(Review review, LocalDateTime now) {
        return toPreviewDto(review, now, null);
    }

    // 새로운 오버로드: currentUserId로 isMyPost 계산
    public static ReviewResponseDto.PreviewDto toPreviewDto(Review review, LocalDateTime now, Long currentUserId) {
        User author = (review != null) ? review.getUser() : null;

        Long authorId = (author != null) ? author.getId() : null;
        boolean isMyPost = (currentUserId != null && authorId != null && authorId.equals(currentUserId));

        String profileImage = (author != null) ? author.getProfileImage() : null;
        String userComment  = (author != null) ? author.getComment() : null;
        String userName     = (author != null && author.getNickname() != null)
                ? author.getNickname()
                : "알 수 없음";

        List<String> imageUrls =
                (review != null && review.getImages() != null)
                        ? review.getImages().stream()
                        .map(ReviewImage::getImageUrl)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList())
                        : List.of();

        return ReviewResponseDto.PreviewDto.builder()
                .reviewId((review != null) ? review.getId() : null)
                .title((review != null) ? review.getTitle() : null)
                .content((review != null) ? review.getContent() : null)
                .userName(userName)
                .elapsedTime((review != null) ? calculateElapsedTime(review.getCreatedAt(), now) : "알 수 없음")
                .imageUrls(imageUrls)
                .userProfileImage(profileImage)
                .userComment(userComment)
                .isMyPost(isMyPost)
                .build();
    }

    private static String calculateElapsedTime(LocalDateTime createdAt, LocalDateTime now) {
        if (createdAt == null || now == null) return "알 수 없음";

        Duration duration = Duration.between(createdAt, now);
        if (duration.toMinutes() < 1)  return "방금 전";
        if (duration.toMinutes() < 60) return duration.toMinutes() + "분 전";
        if (duration.toHours() < 24)   return duration.toHours() + "시간 전";
        if (duration.toDays() < 7)     return duration.toDays() + "일 전";
        return createdAt.toLocalDate().toString();
    }

}
