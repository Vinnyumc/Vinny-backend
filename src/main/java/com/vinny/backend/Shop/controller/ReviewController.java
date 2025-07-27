package com.vinny.backend.Shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinny.backend.Shop.dto.ReviewRequestDto;
import com.vinny.backend.Shop.dto.ReviewResponseDto;
import com.vinny.backend.Shop.service.ReviewService;
import com.vinny.backend.User.domain.User;
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
                @RequestPart(value = "images", required = false) List<MultipartFile> images
        ) throws IOException {
            // JSON 문자열을 수동으로 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ReviewRequestDto.CreateDto dto = objectMapper.readValue(dtoJson, ReviewRequestDto.CreateDto.class);

            return ResponseEntity.ok(reviewService.createReview(dto, shopId, images));
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


}
