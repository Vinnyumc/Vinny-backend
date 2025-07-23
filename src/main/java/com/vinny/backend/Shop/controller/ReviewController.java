package com.vinny.backend.Shop.controller;

import com.vinny.backend.Shop.dto.ReviewRequestDto;
import com.vinny.backend.Shop.dto.ReviewResponseDto;
import com.vinny.backend.Shop.service.ReviewService;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Reivew", description = "빈티지 샵 후기 관련 API")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{shopId}/reviews")
    @Operation(summary = "리뷰 생성", description = "지정된 샵에 리뷰를 작성합니다.")
    public ResponseEntity<ReviewResponseDto.PreviewDto> createReview(
            @PathVariable Long shopId,
            @RequestBody ReviewRequestDto.CreateDto dto,
            @CurrentUser Long userId
    ) {
        return ResponseEntity.ok(reviewService.createReview(dto, shopId, userId));
    }

}
