package com.vinny.backend.search.controller;

import com.vinny.backend.search.service.BrandSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands/autocomplete")
@RequiredArgsConstructor
public class AutocompleteController {

    private final BrandSearchService brandSearchService;

    @GetMapping("/brands/autocomplete")
    @Operation(
            summary = "브랜드 자동완성 검색",
            description = "키워드를 입력하면 브랜드명을 자동완성하여 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자동완성 결과 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (keyword 파라미터가 누락 등)")
    })
    public ResponseEntity<List<String>> autocomplete(
            @Parameter(description = "자동완성에 사용할 키워드", example = "ni", required = true)
            @RequestParam String keyword
    ) {
        List<String> result = brandSearchService.autocomplete(keyword);
        return ResponseEntity.ok(result);
    }

}
