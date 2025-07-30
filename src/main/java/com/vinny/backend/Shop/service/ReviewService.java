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
import com.vinny.backend.s3.service.S3Service;
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
    가게 후기 작성 api
    */
    @Transactional
    public ReviewResponseDto.PreviewDto createReview(
            ReviewRequestDto.CreateDto dto,
            Long shopId,
            List<MultipartFile> imageFiles
    ) throws IOException {

        // 현재 로그인된 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        Long userId;
        try {
            userId = Long.parseLong(authentication.getName()); // authentication.getName() == userId
        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        List<String> imageUrls = s3Service.uploadFiles(imageFiles);

        Review review = Review.create(
                dto.getTitle(),
                dto.getContent(),
                shop,
                user,
                imageUrls
        );
        reviewRepository.save(review);

        LocalDateTime now = LocalDateTime.now();
        return ReviewConverter.toPreviewDto(review, now);
    }



}
