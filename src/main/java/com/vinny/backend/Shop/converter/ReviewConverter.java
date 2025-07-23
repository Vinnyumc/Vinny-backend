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
        return ReviewResponseDto.PreviewDto.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .userName(review.getUser().getNickname())
                .elapsedTime(calculateElapsedTime(review.getCreatedAt(), now))
                .imageUrls(
                        review.getImages() != null ?
                                review.getImages().stream()
                                        .map(ReviewImage::getImageUrl)
                                        .collect(Collectors.toList()) :
                                List.of()
                )
                .build();
    }

    public static List<ReviewResponseDto.PreviewDto> toPreviewDtoList(List<Review> reviews) {
        LocalDateTime now = LocalDateTime.now();
        return reviews.stream()
                .map(r -> toPreviewDto(r, now))
                .collect(Collectors.toList());
    }

    private static String calculateElapsedTime(LocalDateTime createdAt, LocalDateTime now) {
        if (createdAt == null) return "알 수 없음";

        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) return "방금 전";
        if (duration.toMinutes() < 60) return duration.toMinutes() + "분 전";
        if (duration.toHours() < 24) return duration.toHours() + "시간 전";
        if (duration.toDays() < 7) return duration.toDays() + "일 전";
        return createdAt.toLocalDate().toString();
    }
}
