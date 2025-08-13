package com.vinny.backend.Shop.controller;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.dto.ShopRankingResponse;
import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.service.ShopCommandService;
import com.vinny.backend.Shop.service.ShopRankingService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Shop", description = "빈티지 샵 관련 API")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ShopCommandService shopCommandService;
    private final ShopRankingService shopRankingService;

//    @PostMapping
//    public ResponseEntity<ShopResponseDto.PreviewDto> createShop(
//            @RequestBody @Valid ShopRequestDto.CreateDto requestDto
//    ) {
//        ShopResponseDto.PreviewDto response = shopCommandService.createShop(requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }


    @GetMapping("/shop/search/style")
    @Operation(summary = "스타일별 가게 목록 조회",
            description = "피그마 검색/메인에 있는 카테고리로 모아보기입니다. 스타일별 가게 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "name", description = "스타일 이름"),
            @Parameter(name = "page", description = "페이지 번호 (1부터 시작)")
    })
    public ResponseEntity<ApiResponse<ShopResponseDto.ShopPreviewListDto>> searchShopsByStyle(
            @ExistVintageStyle @RequestParam("name") String style,
            @ValidPageParam Integer page
    ) {
        ShopResponseDto.ShopPreviewListDto shopPreviewListDto = shopService.getShopsByStyle(style, page);
        return ResponseEntity.ok(ApiResponse.onSuccess(shopPreviewListDto));
    }


    @Operation(summary = "가게 상세 조회", description = "특정 가게의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponseDto.PreviewDto>> getShopDetails(
            @Parameter(description = "가게 ID", required = true) @PathVariable Long shopId
    ) {
        Optional<Shop> findedShop = shopService.getShop(shopId);
        findedShop.ifPresent(shop -> {
            Integer shopcounts =  shop.getVisitCount();
            shopcounts = shopcounts + 1;
        });
        ShopResponseDto.PreviewDto shopDetails = shopService.getShopsDetails(shopId);
        return ResponseEntity.ok(ApiResponse.onSuccess(shopDetails));
    }

    @Operation(summary = "지도 가게 썸네일 조회", description = "특정 가게의 지도용 썸네일 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/map/shops/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponseDto.MapThumbnailDto>> getMapThumbnail(
            @Parameter(description = "가게 ID", required = true) @PathVariable Long shopId
    ) {
        ShopResponseDto.MapThumbnailDto shopDetails = shopService.getMapThumbnail(shopId);
        return ResponseEntity.ok(ApiResponse.onSuccess(shopDetails));
    }

    @Operation(
            summary = "방문수 랭킹 조회 (QueryDSL)",
            description = """
                visitCount 내림차순으로 샵을 반환합니다.
                - region: (선택) '홍대','성수','강남' 등 키워드 (Region.name/주소 부분매치)
                - style:  (선택) '밀리터리','아메카지' 등 VintageStyle 이름
                - page/size: 페이징
                """
    )
    @GetMapping("/shops/ranking")
    public ResponseEntity<ApiResponse<List<ShopRankingResponse>>> ranking(
            @Parameter(description = "0-base 페이지", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "지역 필터", example = "홍대")
            @RequestParam(required = false) String region,
            @Parameter(description = "스타일 필터", example = "밀리터리")
            @RequestParam(required = false) String style
    ) {
        // 선택적 매핑 (원하면 여기서 '홍대/성수/강남' 등의 별칭 정규화)
        if (region != null && region.isBlank()) region = null;
        if (style != null && style.isBlank()) style = null;

        List<ShopRankingResponse> result = shopRankingService.getRanking(region, style, page, size);
        return ResponseEntity.ok(ApiResponse.onSuccess("방문수 랭킹 조회 성공", result));
    }


}
