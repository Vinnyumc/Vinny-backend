package com.vinny.backend.map.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.map.dto.MapResponseDto.MapListDto;
import com.vinny.backend.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/shops/all")
    public ApiResponse<List<MapListDto>> getAllShops() {
        List<MapListDto> shopList = mapService.getAllShopList();
        return ApiResponse.onSuccess("근처 빈티지샵 목록 조회에 성공하였습니다.", shopList);
    }
}
