package com.vinny.backend.Shop.service;

import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;

public interface ShopCommandService {
    ShopResponseDto.PreviewDto createShop(ShopRequestDto.CreateDto requestDto);
}
