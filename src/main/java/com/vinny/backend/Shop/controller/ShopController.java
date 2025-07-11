package com.vinny.backend.Shop.controller;

import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.service.ShopCommandService;
import com.vinny.backend.User.validation.annotation.ExistVintageStyle;
import com.vinny.backend.common.validator.ValidPageParam;
import com.vinny.backend.error.ApiResponse;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vinny.backend.Shop.dto.ShopSearchResponseDto;
import com.vinny.backend.Shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop")
@Tag(name = "Shop", description = "빈티지 샵 관련 API")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ShopCommandService shopCommandService;

    @PostMapping
    public ResponseEntity<ShopResponseDto.PreviewDto> createShop(
            @RequestBody @Valid ShopRequestDto.CreateDto requestDto
    ) {
        ShopResponseDto.PreviewDto response = shopCommandService.createShop(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "샵 검색", description = "키워드로 빈티지샵을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchShops(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword
    ) {
        List<ShopSearchResponseDto.ShopDto> shops = shopService.searchShops(keyword);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "샵 검색 결과입니다.",
                        "status", 200,
                        "data", Map.of("shops", shops),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @GetMapping("/search/style")
    @Operation(summary = "스타일별 가게 목록 조회 API",
            description = "특정 스타일에 해당하는 가게 목록을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "name", description = "검색할 스타일 이름"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)")
    })
    public ResponseEntity<ApiResponse<ShopResponseDto.ShopPreviewListDto>> searchShopsByStyle(
            @ExistVintageStyle @RequestParam("name") String style,
            @RequestParam(name = "page", defaultValue = "0") @ValidPageParam Integer page
    ) {
        ShopResponseDto.ShopPreviewListDto shopPreviewListDto = shopService.getShopsByStyle(style, page);
        return ResponseEntity.ok(ApiResponse.onSuccess(shopPreviewListDto));
    }


}
