package com.vinny.backend.search.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import com.vinny.backend.search.dto.SearchLogCreateRequest;
import com.vinny.backend.search.dto.SearchLogResponse;
import com.vinny.backend.search.service.SearchLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search-logs")
@RequiredArgsConstructor
@Tag(name = "SearchLog", description = "검색 로그 관리 API")
public class SearchLogController {

    private final SearchLogService searchLogService;

    @Operation(summary = "검색어 추가", description = "사용자의 검색어를 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색어 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<SearchLogResponse>> addSearchLog(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @Valid @RequestBody SearchLogCreateRequest request) {

        SearchLogResponse response = searchLogService.addSearchLog(userId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
