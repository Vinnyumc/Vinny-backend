package com.vinny.backend.search.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "최근 검색어 목록 응답")
public class RecentSearchListResponse {

    @Schema(description = "최근 검색어 목록")
    private List<RecentSearchResponse> recentSearches;

    @Schema(description = "총 검색어 개수", example = "5")
    private int totalCount;

    public static RecentSearchListResponse of(List<RecentSearchResponse> recentSearches) {
        return RecentSearchListResponse.builder()
                .recentSearches(recentSearches)
                .totalCount(recentSearches.size())
                .build();
    }

    public static RecentSearchListResponse empty() {
        return RecentSearchListResponse.builder()
                .recentSearches(List.of())
                .totalCount(0)
                .build();
    }
}
