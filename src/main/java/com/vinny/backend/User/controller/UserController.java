package com.vinny.backend.User.controller;

import com.vinny.backend.User.service.UserService;
import com.vinny.backend.User.dto.OnboardingRequestDto;
import com.vinny.backend.error.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/me/onboard")
    public ApiResponse<String> completeOnboarding(Authentication authentication, @RequestBody OnboardingRequestDto requestDto) {
        Long userId = Long.parseLong(authentication.getName());
        userService.completeOnboarding(userId, requestDto);

        return ApiResponse.onSuccess("온보딩이 성공적으로 완료되었습니다.");
    }
}
