package com.vinny.backend.auth.controller;

import com.vinny.backend.auth.dto.*;
import com.vinny.backend.auth.service.AuthService;
import com.vinny.backend.auth.service.KakaoAuthService;
import com.vinny.backend.error.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/login/kakao")
    public ApiResponse<LoginResponseDto> kakaoMobileLogin(@RequestBody KakaoTokenRequestDto requestDto) {
        LoginResponseDto responseDto = kakaoAuthService.processKakaoLogin(requestDto.getAccessToken());
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenDto> reissue(@RequestBody TokenRequestDto requestDto) {
        TokenDto tokenDto = authService.reissue(requestDto);
        return ApiResponse.onSuccess(tokenDto);
    }

    @GetMapping("/me")
    public ApiResponse<String> getMyInfo(Authentication authentication) {
        String userId = authentication.getName();
        String resultMessage = "Your User ID is: " + userId;
        return ApiResponse.onSuccess(resultMessage);
    }
}
