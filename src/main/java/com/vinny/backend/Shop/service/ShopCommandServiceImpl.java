package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.converter.ShopConverter;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.ShopImage;
import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.Shop.domain.mapping.ShopVintageStyle;
import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.repository.ShopImageRepository;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.Region;
import com.vinny.backend.User.domain.VintageStyle;
import com.vinny.backend.User.repository.RegionRepository;
import com.vinny.backend.User.repository.VintageStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopCommandServiceImpl implements ShopCommandService{

    private final RegionRepository regionRepository;
    private final ShopRepository shopRepository;
    private final VintageStyleRepository vintageStyleRepository;

    /**
    가게 생성
    * */
    @Transactional
    @Override
    public ShopResponseDto.PreviewDto createShop(ShopRequestDto.CreateDto dto) {

        Region region = regionRepository.getReferenceById(dto.getRegionId());

        List<VintageStyle> styles = dto.getVintageStyleIds().stream()
                .map(vintageStyleRepository::getReferenceById)
                .toList();


        // Shop 엔티티 생성 (region, styles 등 Service에서 세팅)
        Shop shop = Shop.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(Status.valueOf(String.valueOf(dto.getStatus())))
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .instagram(dto.getInstagram())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .region(region)
                .build();

        // Styles 연관관계 세팅
        styles.forEach(style -> {
            ShopVintageStyle shopStyle = new ShopVintageStyle(shop, style);
            shop.getShopVintageStyleList().add(shopStyle);
        });

        // Images 연관관계 세팅 (Shop 생성 시 바로 생성 + 컬렉션 add)
        dto.getImages().forEach(imgDto -> {
            ShopImage image = new ShopImage(shop, imgDto.getImageUrl(), imgDto.isMainImage());
            shop.getShopImages().add(image);
        });


        shopRepository.save(shop);

        return ShopConverter.toPreviewDto(shop);
    }



}
