package com.vinny.backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "검색어 추가 요청")
public class SearchLogCreateRequest {

    @NotBlank(message = "검색어는 필수입니다.")
    @Size(min = 1, max = 100, message = "검색어는 1자 이상 100자 이하여야 합니다.")
    @Schema(description = "검색 키워드", example = "빈티지 자켓", required = true)
    private String keyword;

    public SearchLogCreateRequest(String keyword) {
        this.keyword = keyword;
    }
}
