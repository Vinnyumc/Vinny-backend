package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.dto.ShopSearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    public List<ShopSearchResponseDto.ShopDto> searchShops(String keyword) {
        return List.of();
    }
}
