package com.vinny.backend.Shop.controller;

import com.vinny.backend.Shop.dto.ShopRequestDto;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.service.ShopCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopCommandService shopCommandService;

    @PostMapping
    public ResponseEntity<ShopResponseDto.PreviewDto> createShop(
            @RequestBody @Valid ShopRequestDto.CreateDto requestDto
    ) {
        ShopResponseDto.PreviewDto response = shopCommandService.createShop(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

