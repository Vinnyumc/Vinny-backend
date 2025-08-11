package com.vinny.backend.Mypage.dto;

import java.util.List;

/** 저장(좋아요)한 게시글별 이미지 목록 */
public record MypageSavePostImagesResponse(
        Long postId,
        List<String> images
) {}
