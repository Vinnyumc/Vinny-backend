// src/main/java/com/vinny/backend/Shop/dto/ShopRankingResponse.java
package com.vinny.backend.Shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "방문수 기준 샵 랭킹 응답")
public record ShopRankingResponse(
        @Schema(description = "샵 ID") Long shopId,
        @Schema(description = "샵 이름") String name,
        @Schema(description = "주소") String address,
        @Schema(description = "지역명") String region,
        @Schema(description = "스타일 태그") List<String> tags,
        @Schema(description = "대표 이미지 URL") String thumbnailUrl
) {}
