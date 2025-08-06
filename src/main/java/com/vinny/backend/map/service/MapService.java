package com.vinny.backend.map.service;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.map.converter.MapConverter;
import com.vinny.backend.map.dto.MapResponseDto.MapListDto;
import com.vinny.backend.map.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;
    private final MapConverter mapConverter;

    public List<MapListDto> getAllShopList() {
        List<Shop> shopList = mapRepository.findAll();
        return mapConverter.toMapListDtos(shopList);
    }
}
