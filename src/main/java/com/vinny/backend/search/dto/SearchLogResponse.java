package com.vinny.backend.search.dto;

import com.vinny.backend.search.domain.SearchLog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "검색어 응답")
public record SearchLogResponse(
        @Schema(description = "검색 로그 ID", example = "1")
        Long id,

        @Schema(description = "검색 키워드", example = "빈티지 자켓")
        String keyword,

        @Schema(description = "검색 시간", example = "2024-07-20T10:30:00")
        LocalDateTime searchedAt
) {

    public static SearchLogResponse from(SearchLog searchLog) {
        return new SearchLogResponse(
                searchLog.getId(),
                searchLog.getKeyword(),
                searchLog.getSearchedAt()
        );
    }
}