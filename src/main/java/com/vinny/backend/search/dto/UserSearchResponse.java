package com.vinny.backend.search.dto;

import lombok.Builder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 검색 결과 응답 DTO")
@Builder
public record UserSearchResponse(
        @Schema(description = "유저 ID", example = "123")
        Long id,

        @Schema(description = "닉네임", example = "vinny_dev")
        String nickname,

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.vinny.com/images/profile.png")
        String profileImage,

        @Schema(description = "한줄 소개/코멘트", example = "빈티지 스타일을 좋아합니다!")
        String comment
) {}
