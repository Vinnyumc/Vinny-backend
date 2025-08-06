package com.vinny.backend.map.dto;

import com.vinny.backend.Shop.domain.mapping.ShopVintageStyle;
import lombok.*;

import java.util.List;

@Getter
public class MapResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MapListDto {

        private Long id;
        private Double latitude;
        private Double longitude;
        private List<VintageStyleDto> vintageStyleList;
    }

    @Getter
    @Setter
    @Builder
    public static class VintageStyleDto {
        private Long id;
        private String vintageStyleName;
    }
}
