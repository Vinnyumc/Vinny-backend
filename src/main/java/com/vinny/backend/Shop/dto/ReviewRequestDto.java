package com.vinny.backend.Shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
public class ReviewRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateDto {
        @NotBlank(message = "title은 비어있을 수 없습니다.")
        private String title;

        @NotBlank(message = "content는 비어있을 수 없습니다.")
        private String content;

        @NotNull(message = "imageUrls는 null일 수 없습니다.")
        private List<@NotBlank(message = "imageUrl는 비어있을 수 없습니다.") String> imageUrls;
    }
}
