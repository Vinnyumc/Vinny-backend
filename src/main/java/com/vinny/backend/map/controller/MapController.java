package com.vinny.backend.map.controller;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.map.dto.MapResponseDto.MapListDto;
import com.vinny.backend.map.service.MapService;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
@Tag(name = "Map", description = "지도 기반 빈티지샵 조회 API")
public class MapController {

    private final MapService mapService;

    @Operation(summary = "전체 가게 목록 조회", description = "지도의 모든 빈티지샵 목록을 조회합니다. 로그인 없이 호출 가능합니다.")
    @GetMapping("/shops/all")
    public ApiResponse<List<MapListDto>> getAllShops() {
        List<MapListDto> shopList = mapService.getAllShopList();
        return ApiResponse.onSuccess("근처 빈티지샵 목록 조회에 성공하였습니다.", shopList);
    }

    @Operation(summary = "즐겨찾기한 가게 목록 조회", description = "현재 로그인한 사용자의 즐겨찾기 빈티지샵 목록을 조회합니다. JWT 액세스 토큰이 필요합니다.")
    @GetMapping("/shops/favorite")
    public ApiResponse<List<MapListDto>> getFavoriteShops() {
        List<MapListDto> favoriteShopList = mapService.getFavoriteShopList();
        return ApiResponse.onSuccess("즐겨찾기 샵 목록 조회에 성공하였습니다.", favoriteShopList);
    }
}
