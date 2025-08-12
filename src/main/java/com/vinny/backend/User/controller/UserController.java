package com.vinny.backend.User.controller;

import com.vinny.backend.User.service.UserService;
import com.vinny.backend.User.dto.OnboardingRequestDto;
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
}
