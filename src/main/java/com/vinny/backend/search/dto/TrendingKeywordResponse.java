package com.vinny.backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추천/트렌딩 검색어")
public record TrendingKeywordResponse(
        @Schema(description = "검색어") String keyword,
        @Schema(description = "점수/건수") double score
) {
}
