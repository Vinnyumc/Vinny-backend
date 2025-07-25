package com.vinny.backend.search.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.dto.PostResponse;
import com.vinny.backend.search.dto.ShopResponse;
import com.vinny.backend.search.dto.StyleSearchRequest;
import com.vinny.backend.search.service.PostSearchService;
import com.vinny.backend.search.service.ShopSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "스타일별 게시글/매장 검색 API")
public class SearchController {

    private final PostSearchService postSearchService;
    private final ShopSearchService shopSearchService;

    // ========== POST 검색 ==========
    @Operation(summary = "스타일별 게시물 검색", description = "스타일 타입/키워드/지역 등으로 게시물을 검색합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/posts/style")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> searchPostsByStyle(
            @Valid @RequestBody StyleSearchRequest request,
            @Parameter(hidden = true) @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        Page<PostResponse> results = postSearchService.searchByStyle(request, pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(
                request.styleType() + " 스타일 게시물 검색 완료", results));
    }


    // ========== SHOP 검색 ==========
    @Operation(summary = "스타일별 매장 검색", description = "스타일 타입/키워드/지역 등으로 매장을 검색합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/shops/style")
    public ResponseEntity<ApiResponse<Page<ShopResponse>>> searchShopsByStyle(
            @Valid @RequestBody StyleSearchRequest request,
            @Parameter(hidden = true) @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        Page<ShopResponse> results = shopSearchService.searchByStyle(request, pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(
                request.styleType() + " 스타일 매장 검색 완료", results));
    }
}
