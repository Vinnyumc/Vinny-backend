package com.vinny.backend.brand.controller;

import com.vinny.backend.brand.dto.BrandDto;
import com.vinny.backend.brand.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequiredArgsConstructor
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    @Operation(summary = "브랜드 검색", description = "키워드로 브랜드를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchBrands(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword
    ) {
        List<BrandDto> brands = brandService.searchBrands(keyword);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "브랜드 검색 결과입니다.",
                        "status", 200,
                        "data", Map.of("brands", brands),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

}