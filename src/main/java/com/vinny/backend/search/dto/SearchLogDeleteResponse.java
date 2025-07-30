package com.vinny.backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "검색어 삭제 응답")
public class SearchLogDeleteResponse {

    @Schema(description = "삭제된 검색어 ID", example = "1")
    private Long deletedSearchLogId;

    @Schema(description = "삭제 성공 여부", example = "true")
    private boolean deleted;

    @Schema(description = "메시지", example = "검색어가 삭제되었습니다.")
    private String message;

    public static SearchLogDeleteResponse success(Long searchLogId) {
        return SearchLogDeleteResponse.builder()
                .deletedSearchLogId(searchLogId)
                .deleted(true)
                .message("검색어가 삭제되었습니다.")
                .build();
    }
}