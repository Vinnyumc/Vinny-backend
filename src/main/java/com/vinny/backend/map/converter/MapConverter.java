package com.vinny.backend.map.converter;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.mapping.ShopVintageStyle;
import com.vinny.backend.User.domain.VintageStyle;
import com.vinny.backend.map.dto.MapResponseDto.VintageStyleDto;
import com.vinny.backend.map.dto.MapResponseDto.MapListDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapConverter {

    public List<MapListDto> toMapListDtos(List<Shop> shops) {
        return shops.stream()
                .map(this::toMapListDto)
                .collect(Collectors.toList());
    }

    private MapListDto toMapListDto(Shop shop) {
        return MapListDto.builder()
                .id(shop.getId())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .vintageStyleList(toVintageStyleDtos(shop.getShopVintageStyleList()))
                .mainVintageStyle(toMainVintageStyleDto(shop.getMainVintageStyle()))
                .build();
    }

    private List<VintageStyleDto> toVintageStyleDtos(List<ShopVintageStyle> mappings) {
        return mappings.stream()
                .map(mapping -> VintageStyleDto.builder()
                        .id(mapping.getVintageStyle().getId())
                        .vintageStyleName(mapping.getVintageStyle().getName())
                        .build())
                .collect(Collectors.toList());
    }

    private VintageStyleDto toMainVintageStyleDto(VintageStyle mainStyle) {
        if (mainStyle == null) {
            return null;
        }
        return VintageStyleDto.builder()
                .id(mainStyle.getId())
                .vintageStyleName(mainStyle.getName())
                .build();
    }
}
