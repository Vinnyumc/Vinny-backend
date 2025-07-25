package com.vinny.backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "스타일 검색 요청 DTO")
public record StyleSearchRequest(
        @Schema(description = "스타일 타입", example = "VINTAGE")
        @NotNull(message = "스타일은 필수입니다.")
        String styleType,

        @Schema(description = "검색 키워드", example = "자켓")
        String keyword,

        @Schema(description = "지역명", example = "서울")
        String region
) {}
