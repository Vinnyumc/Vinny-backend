package com.vinny.backend.Shop.controller;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop")
@Tag(name = "Shop", description = "빈티지 샵 관련 API")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;


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
}
