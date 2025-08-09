package com.vinny.backend.User.controller;

import com.vinny.backend.User.dto.UserShopRequestDto;
import com.vinny.backend.User.dto.UserShopResponseDto;
import com.vinny.backend.User.service.UserShopService;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserShopController {

    private final UserShopService userShopService;

    @Operation(summary = "가게 찜 추가", description = "사용자가 특정 가게를 찜 목록에 추가합니다.")
    @PostMapping("/shops/{shopId}/favorite")
    public ApiResponse<UserShopResponseDto.PreviewDto> addFavoriteShop(
            @Parameter(description = "찜할 가게 ID", required = true)
            @PathVariable Long shopId,
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {
        UserShopRequestDto.LikeDto requestDto = new UserShopRequestDto.LikeDto(shopId);
        UserShopResponseDto.PreviewDto response = userShopService.addFavoriteShop(userId, requestDto);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "가게 찜 삭제", description = "사용자가 특정 가게를 찜 목록에서 삭제합니다.")
    @PatchMapping("/shops/{shopId}/favorite")
    public ApiResponse<String> removeFavoriteShop(
            @Parameter(description = "삭제할 가게 ID", required = true)
            @PathVariable Long shopId,
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {
        String message = userShopService.removeFavoriteShop(userId, shopId);
        return ApiResponse.onSuccess(message);
    }

    @Operation(summary = "가게 찜 조회 (다른 사용자)", description = "지정된 사용자의 찜 가게 목록을 조회합니다.")
    @GetMapping("/users/{userId}/shops/favorites")
    public ApiResponse<List<UserShopResponseDto.PreviewShopDetailDto>> getUserFavoriteShops(
            @Parameter(description = "조회할 사용자 ID", required = true)
            @PathVariable Long userId
    ) {
        List<UserShopResponseDto.PreviewShopDetailDto> response = userShopService.getFavoriteShops(userId);
        return ApiResponse.onSuccess(response);
    }
}
