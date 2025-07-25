package com.vinny.backend.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
public class PostRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CreatePostRequest")
    public static class CreateDto {

        @Schema(description = "게시글 제목")
        private String title;

        @Schema(description = "게시글 내용")
        private String content;

        @Schema(description = "스타일 ID")
        private List<Long> styleIds;

        @Schema(description = "브랜드 ID")
        private List<Long> brandIds;

        @Schema(description = "샵 ID")
        private Long shopId;
    }
}
