package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.dto.ShopSearchResponseDto;
import com.vinny.backend.Shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public List<ShopSearchResponseDto.ShopDto> searchShops(String keyword) {
        List<Shop> shops = shopRepository.searchByNameOrStyleOrRegion(keyword);

        return shops.stream()
                .map(shop -> new ShopSearchResponseDto.ShopDto(
                        shop.getId(),
                        shop.getName(),
                        shop.getRegion() != null ? shop.getRegion().getName() : null,
                        shop.getShopVintageStyleList().stream()
                                .map(svs -> svs.getVintageStyle().getName())
                                .toList(),
                        shop.getShopImages().isEmpty() ? null : shop.getShopImages().get(0).getImageUrl()
                ))
                .toList();
    }
}
