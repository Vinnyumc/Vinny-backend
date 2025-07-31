package com.vinny.backend.Shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinny.backend.Shop.dto.ReviewRequestDto;
import com.vinny.backend.Shop.dto.ReviewResponseDto;
import com.vinny.backend.Shop.service.ReviewService;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Reivew", description = "빈티지 샵 후기 관련 API")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
    후기 작성 스웨거 용
    **/
        @PostMapping(value = "/{shopId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @Operation(summary = "후기 생성", description = "지정된 가게에 후기를 작성합니다.")
        public ResponseEntity<ReviewResponseDto.PreviewDto> createReview(
                @PathVariable Long shopId,
                @Parameter(
                        name = "dto",
                        description = "JSON 문자열 입력 예시:\n{\n  \"title\": \"리뷰 제목\",\n  \"content\": \"리뷰 내용\"\n}",
                        required = true
                )
                @RequestPart("dto") String dtoJson,
                @RequestPart(value = "images", required = false) List<MultipartFile> images,
                @Parameter(hidden = true) @CurrentUser Long userId
        ) throws IOException {
            List<MultipartFile> imageList = (images != null) ? images : Collections.emptyList();

            // JSON 문자열을 수동으로 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ReviewRequestDto.CreateDto dto = objectMapper.readValue(dtoJson, ReviewRequestDto.CreateDto.class);

            return ResponseEntity.ok(reviewService.createReview(dto, shopId, imageList, userId));
        }

    /**
     후기 작성 운영용
     **/
//        @PostMapping(value = "/{shopId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//        @Operation(summary = "후기 생성", description = "지정된 가게에 후기를 작성합니다.")
//        public ResponseEntity<ReviewResponseDto.PreviewDto> createReview(
//                @PathVariable Long shopId,
//                @RequestPart("dto") ReviewRequestDto.CreateDto dto,
//                @RequestPart(value = "images", required = false) List<MultipartFile> images
//        ) throws IOException {
//            return ResponseEntity.ok(reviewService.createReview(dto, shopId, images));
//        }


        @Operation(summary = "가게 후기 목록 조회", description = "특정 가게의 전체 후기 목록을 조회합니다.")
        @GetMapping("/{shopId}/reviews")
        public ApiResponse<List<ReviewResponseDto.PreviewDto>> getReviewsByShop(
                @Parameter(description = "후기를 조회할 가게 ID", required = true)
                @PathVariable Long shopId
        ) {
            List<ReviewResponseDto.PreviewDto> response = reviewService.getReviewsByShop(shopId);
            return ApiResponse.onSuccess(response);
        }

        @Operation(summary = "가게 후기 삭제", description = "작성한 가게 후기를 삭제합니다. (본인만 삭제 가능)")
        @DeleteMapping("/{shopId}/reviews/{reviewId}")
        public ApiResponse<String> deleteReview(
                @Parameter(description = "후기를 삭제할 가게 ID", required = true)
                @PathVariable Long shopId,

                @Parameter(description = "삭제할 후기 ID", required = true)
                @PathVariable Long reviewId,
                @Parameter(hidden = true) @CurrentUser Long userId
        ) {
            String result = reviewService.deleteReview(shopId, reviewId, userId);
            return ApiResponse.onSuccess(result);
        }
}
