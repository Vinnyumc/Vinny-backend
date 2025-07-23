package com.vinny.backend.Shop.dto;

import lombok.*;
import java.util.List;


@Getter
public class ReviewResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewDto {
        private Long reviewId;
        private String title;
        private String content;
        private String userName;
        private String elapsedTime;
        private List<String> imageUrls;
    }
}

