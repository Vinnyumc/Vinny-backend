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
            Long userId,
            List<MultipartFile> imageFiles // 이미지 파일 파라미터 추가
    ) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        // 이미지 파일들을 S3에 업로드하고 URL 리스트 획득
        List<String> imageUrls = s3Service.uploadFiles(imageFiles);

        Review review = Review.create(
                dto.getTitle(),
                dto.getContent(),
                shop,
                user,
                imageUrls // URL 리스트 저장
        );
        reviewRepository.save(review);

        LocalDateTime now = LocalDateTime.now();
        return ReviewConverter.toPreviewDto(review, now);
    }



}
