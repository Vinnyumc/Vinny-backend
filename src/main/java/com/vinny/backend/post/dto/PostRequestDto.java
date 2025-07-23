package com.vinny.backend.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class PostRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePostRequest {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 15, message = "제목은 최대 15자까지 가능해요")
        private String title;
        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 100, message = "나만의 멋진 내용을 적어주세요!")
        private String content;
        @Size(max = 5, message = "이미지는 최대 5개까지 업로드할 수 있습니다.")
        private List<String> imageUrls;

        private List<Long> styleId;

        private Long brandId;

        private Long shopId;
    }

}
