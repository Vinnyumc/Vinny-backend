package com.vinny.backend.search.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import com.vinny.backend.search.dto.*;
import com.vinny.backend.search.service.SearchLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "최근 검색어 목록 조회",
            description = "사용자의 최근 검색어 목록을 조회합니다. (최대 10개)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<RecentSearchListResponse>> getRecentSearches(
            @Parameter(hidden = true) @CurrentUser Long userId) {

        RecentSearchListResponse response = searchLogService.getRecentSearches(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }



    @Operation(summary = "검색어 개별 삭제",
            description = "특정 검색어를 삭제합니다. (X 버튼 기능)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "검색 로그를 찾을 수 없음")
    })
    @DeleteMapping("/keyword")
    public ResponseEntity<ApiResponse<SearchLogDeleteResponse>> deleteSearchLog(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @RequestBody @Valid SearchLogDeleteRequest request) {

        Long searchLogId = searchLogService.getSearchLogByKeyword(userId, request.keyword());
        SearchLogDeleteResponse response = searchLogService.deleteSearchLog(userId, searchLogId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }


    @Operation(summary = "모든 검색어 삭제",
            description = "사용자의 모든 검색어를 삭제합니다. (모두 삭제 버튼 기능)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> deleteAllSearchLogs(
            @Parameter(hidden = true) @CurrentUser Long userId) {

        searchLogService.deleteAllSearchLogs(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }


}
