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

    @Schema(name = "CreatePostRequest")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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

    //일단 전체 필드 수정 가능하도록
    @Schema(name = "UpdatePostRequest")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDto {

        @Schema(description = "게시글 제목")
        private String title;

        @Schema(description = "게시글 내용")
        private String content;

        @Schema(description = "스타일 ID 목록")
        private List<Long> styleIds;

        @Schema(description = "브랜드 ID 목록")
        private List<Long> brandIds;

        @Schema(description = "샵 ID")
        private Long shopId;
    }
}
