package com.vinny.backend.User.controller;

import com.vinny.backend.User.dto.*;
import com.vinny.backend.User.service.UserService;
import com.vinny.backend.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "온보딩", description = "온보딩")
    @PostMapping("/me/onboard")
    public ApiResponse<String> completeOnboarding(Authentication authentication,
                                                  @Valid @RequestBody OnboardingRequestDto requestDto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.completeOnboarding(userId, requestDto);

        return ApiResponse.onSuccess("온보딩이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "취향 재설정 - VintageStyle", description = "취향 재설정 - VintageStyle")
    @PostMapping("/me/reset/vintage-style")
    public ApiResponse<String> resetVintageStyle(Authentication authentication,
                                                  @Valid @RequestBody UserVintageStyleRequestDto requestDto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.resetVintageStyle(userId, requestDto);

        return ApiResponse.onSuccess("취향 재설정(VintageStyle)이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "취향 재설정 - Brand", description = "취향 재설정 - Brand")
    @PostMapping("/me/reset/brand")
    public ApiResponse<String> resetBrand(Authentication authentication,
                                                 @Valid @RequestBody UserBrandRequestDto requestDto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.resetBrand(userId, requestDto);

        return ApiResponse.onSuccess("취향 재설정(Brand)이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "취향 재설정 - VintageItem", description = "취향 재설정 - VintageItem")
    @PostMapping("/me/reset/vintage-item")
    public ApiResponse<String> resetVintageItem(Authentication authentication,
                                                 @Valid @RequestBody UserVintageItemRequestDto requestDto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.resetVintageItem(userId, requestDto);

        return ApiResponse.onSuccess("취향 재설정(VintageItem)이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "취향 재설정 - Region", description = "취향 재설정 - Region")
    @PostMapping("/me/reset/region")
    public ApiResponse<String> resetRegion(Authentication authentication,
                                                @Valid @RequestBody UserRegionRequestDto requestDto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.resetRegion(userId, requestDto);

        return ApiResponse.onSuccess("취향 재설정(Region)이 성공적으로 완료되었습니다.");
    }
}
