package com.vinny.backend.Shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinny.backend.Shop.dto.ReviewRequestDto;
import com.vinny.backend.Shop.dto.ReviewResponseDto;
import com.vinny.backend.Shop.service.ReviewService;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Reivew", description = "빈티지 샵 후기 관련 API")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

//    @PostMapping(value = "/{shopId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(summary = "리뷰 생성", description = "지정된 샵에 리뷰를 작성합니다. 이미지 파일도 함께 업로드합니다.")
//    public ResponseEntity<ReviewResponseDto.PreviewDto> createReview(
//            @PathVariable Long shopId,
//            @RequestPart("dto") ReviewRequestDto.CreateDto dto, // JSON 본문
//            @RequestPart(value = "images", required = false) List<MultipartFile> images, // 이미지 파일 리스트
//            @CurrentUser Long userId
//    ) throws IOException {
//        ReviewResponseDto.PreviewDto result = reviewService.createReview(dto, shopId, userId, images);
//        return ResponseEntity.ok(result);
//    }
        @PostMapping(value = "/{shopId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @Operation(summary = "리뷰 생성", description = "지정된 샵에 리뷰를 작성합니다.")
        public ResponseEntity<ReviewResponseDto.PreviewDto> createReview(
                @PathVariable Long shopId,
                @RequestPart("dto") String dtoJson,
                @RequestPart(value = "images", required = false) List<MultipartFile> images,
                @CurrentUser Long userId
        ) throws IOException {

            // JSON 문자열을 수동으로 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ReviewRequestDto.CreateDto dto = objectMapper.readValue(dtoJson, ReviewRequestDto.CreateDto.class);

            return ResponseEntity.ok(reviewService.createReview(dto, shopId, userId, images));
        }

}
