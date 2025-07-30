package com.vinny.backend.search.dto;

import com.vinny.backend.search.domain.SearchLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "최근 검색어 응답")
public class RecentSearchResponse {

    @Schema(description = "검색 로그 ID", example = "1")
    private Long searchLogId;

    @Schema(description = "검색 키워드", example = "망원빈티지")
    private String keyword;

    public static RecentSearchResponse from(SearchLog searchLog) {
        return RecentSearchResponse.builder()
                .searchLogId(searchLog.getId())
                .keyword(searchLog.getKeyword())
                .build();
    }
}
