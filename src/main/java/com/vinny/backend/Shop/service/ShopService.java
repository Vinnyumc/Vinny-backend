package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.converter.ShopConverter;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.dto.ShopSearchResponseDto;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.VintageStyle;
import com.vinny.backend.User.repository.VintageStyleRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final VintageStyleRepository vintageStyleRepository;
    private final ShopConverter shopConverter;


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


    private static final int PAGE_SIZE = 5;  // 한 페이지당 5개씩 (임시)


    /**
     * 스타일별 가게 목록 조회 (페이징 포함)
     */
    public ShopResponseDto.ShopPreviewListDto getShopsByStyle(String style, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Shop> shopPage = shopRepository.findByShopVintageStyleList_VintageStyle_Name(style, pageable);
        return shopConverter.toShopPreviewListDto(shopPage);
    }

    /**
     * 가게 상세 조회
     */
    public ShopResponseDto.PreviewDto getShopsDetails(long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));
        return shopConverter.toPreviewDto(shop);
    }

}
