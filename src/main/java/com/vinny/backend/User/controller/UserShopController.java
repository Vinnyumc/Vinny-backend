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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/shops/favorites")
@RequiredArgsConstructor
public class UserShopController {
    private final UserShopService userShopService;

    @Operation(summary = "가게 즐겨찾기 추가", description = "사용자가 특정 가게를 즐겨찾기 목록에 추가합니다.")
    @PostMapping
    public ApiResponse<UserShopResponseDto.PreviewDto> addFavoriteShop(
            @RequestBody @Valid UserShopRequestDto.LikeDto requestDto
    ) {
        UserShopResponseDto.PreviewDto response = userShopService.addFavoriteShop(requestDto);
        return ApiResponse.onSuccess(response);
    }

}
