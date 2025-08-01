package com.vinny.backend.search.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "검색 키워드 삭제 요청 DTO")
public record SearchLogDeleteRequest(

        @Schema(description = "삭제할 검색 키워드", example = "스트릿 패션")
        @NotBlank(message = "검색 키워드는 공백일 수 없습니다.")
        String keyword
) {}
