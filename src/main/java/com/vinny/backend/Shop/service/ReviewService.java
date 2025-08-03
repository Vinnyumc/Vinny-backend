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
import com.vinny.backend.auth.jwt.JwtProvider;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.s3.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Service s3Service;


    /**
     * 가게 후기 작성
     */
    @Transactional
    public ReviewResponseDto.PreviewDto createReview(
            ReviewRequestDto.CreateDto dto,
            Long shopId,
            List<MultipartFile> imageFiles,
            Long userId
    ) throws IOException {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        List<String> imageUrls = s3Service.uploadFiles(imageFiles);

        Review review = Review.create(dto.getTitle(), dto.getContent(), shop, user, imageUrls);
        reviewRepository.save(review);

        return ReviewConverter.toPreviewDto(review, LocalDateTime.now());
    }

    /**
     * 가게 후기 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewResponseDto.PreviewDto> getReviewsByShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByShop(shop);

        return reviews.stream()
                .map(review -> ReviewConverter.toPreviewDto(review, LocalDateTime.now()))
                .collect(Collectors.toList());
    }


    /**
     * 후기 삭제
     */
    @Transactional
    public String deleteReview(Long shopId, Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_NOT_FOUND));

        // 리뷰가 해당 shopId에 속한 리뷰인지 검증
        if (!review.getShop().getId().equals(shopId)) {
            throw new GeneralException(ErrorStatus.REVIEW_SHOP_MISMATCH);
        }

        if (!review.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        // S3 이미지 삭제
        List<String> imageUrls = review.getImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        for (String url : imageUrls) {
            s3Service.deleteFile(url);
        }

        reviewRepository.delete(review);

        return String.format("후기 ID %d가 삭제되었습니다.", reviewId);
    }

}

