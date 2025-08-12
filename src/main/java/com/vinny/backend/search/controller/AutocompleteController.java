package com.vinny.backend.search.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import com.vinny.backend.search.service.AutocompleteSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autocomplete")
@RequiredArgsConstructor
public class AutocompleteController {

    private final AutocompleteSearchService autocompleteSearchService;

    @GetMapping("/brands")
    @Operation(
            summary = "브랜드 자동완성 검색",
            description = "키워드를 입력하면 브랜드명을 자동완성하여 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "자동완성 결과 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (keyword 파라미터가 누락 등)")
    })
    public ResponseEntity<ApiResponse<List<String>>> brandAutocomplete(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @Parameter(description = "자동완성에 사용할 키워드", example = "ni", required = true)
            @RequestParam String keyword
    ) {
        List<String> result = autocompleteSearchService.autocompleteBrandName(keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


    @GetMapping("/shops")
    @Operation(
            summary = "가게명 자동완성 검색",
            description = "키워드를 입력하면 가게명을 자동완성하여 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "자동완성 결과 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (keyword 파라미터가 누락 등)")
    })
    public ResponseEntity<ApiResponse<List<String>>> shopAutocomplete(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @Parameter(description = "자동완성에 사용할 키워드", example = "발발빈티지", required = true)
            @RequestParam String keyword
    ) {
        List<String> result = autocompleteSearchService.autocompleteShopName(keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }
}
