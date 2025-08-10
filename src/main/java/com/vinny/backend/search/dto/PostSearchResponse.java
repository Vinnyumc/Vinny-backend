package com.vinny.backend.search.dto;

import java.util.List;

// package 및 클래스는 기존 위치 유지
public class PostSearchResponse {

    public static record PostImagesDto(
            Long id,
            List<String> imageUrls
    ) {}
}
