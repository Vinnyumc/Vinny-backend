package com.vinny.backend.User.controller;


import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.User.service.UserShopForYouService;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home/shops/for-you")
public class UserShopForYouController {

    private final UserShopForYouService userWeeklyShopService;


    @Operation(
            summary = "홈 취향저격 가게 추천",
            description = "홈 취향저격 가게 추천 목록을 조회합니다. 저장된 데이터가 없으면 생성 후 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "OK, 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShopResponseDto.HomeForYouThumbnailDto.class)))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "limit", description = "반환 개수"),
            @Parameter(name = "userId", hidden = true)
    })
    @GetMapping
    public ResponseEntity<List<ShopResponseDto.HomeForYouThumbnailDto>> getThisWeek(
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {
        List<ShopResponseDto.HomeForYouThumbnailDto> body = userWeeklyShopService.getThisWeekForYou(userId);
        return ResponseEntity.ok(body);
    }
//
//    @Operation(
//            summary = "홈 취향저격 가게 추천 재생성",
//            description = "이번 주 추천 샵 데이터를 재생성한 뒤 목록을 반환합니다."
//    )
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                    responseCode = "200",
//                    description = "OK, 성공",
//                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShopResponseDto.HomeForYouThumbnailDto.class)))
//            ),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                    responseCode = "400",
//                    description = "유효하지 않은 요청",
//                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
//            )
//    })
//    @Parameters({
//            @Parameter(name = "limit", description = "재생성 후 반환 개수"),
//            @Parameter(name = "userId", hidden = true)
//    })
//    @PostMapping("/regenerate")
//    public ResponseEntity<List<ShopResponseDto.HomeForYouThumbnailDto>> regenerate(
//            @Parameter(hidden = true) @CurrentUser Long userId
//    ) {
//        List<ShopResponseDto.HomeForYouThumbnailDto> body =
//                userWeeklyShopService.regenerateThisWeek(userId);
//        return ResponseEntity.ok(body);
//    }

}

