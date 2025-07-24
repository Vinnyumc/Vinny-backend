package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.converter.ReviewConverter;
import com.vinny.backend.Shop.domain.Review;
import com.vinny.backend.Shop.domain.ReviewImage;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.dto.ReviewRequestDto;
import com.vinny.backend.Shop.dto.ReviewResponseDto;
import com.vinny.backend.Shop.repository.ReviewRepository;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    /**
    가게 후기 작성 api
    */
    @Transactional
    public ReviewResponseDto.PreviewDto createReview(ReviewRequestDto.CreateDto dto, Long shopId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        Review review = Review.create(
                dto.getTitle(),
                dto.getContent(),
                shop,
                user,
                dto.getImageUrls()
        );
        reviewRepository.save(review);

        LocalDateTime now = LocalDateTime.now();
        return ReviewConverter.toPreviewDto(review, now);
    }


}
