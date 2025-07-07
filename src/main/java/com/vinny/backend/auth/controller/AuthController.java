package com.vinny.backend.auth.controller;

import com.vinny.backend.auth.dto.*;
import com.vinny.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/kakao")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestBody KakaoLoginRequestDto requestDto) {
        TokenDto tokenDto = authService.kakaoLogin(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/me") // Test 코드
    public ResponseEntity<String> getMyInfo(Authentication authentication) {
        String kakaoUserId = authentication.getName();
        return ResponseEntity.ok("Your Kakao User ID is: " + kakaoUserId);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto requestDto) {
        return ResponseEntity.ok(authService.reissue(requestDto));
    }
}
