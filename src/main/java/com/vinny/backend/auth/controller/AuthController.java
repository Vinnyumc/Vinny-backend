package com.vinny.backend.auth.controller;

import com.vinny.backend.auth.dto.*;
import com.vinny.backend.auth.jwt.JwtProvider;
import com.vinny.backend.auth.service.AuthService;
import com.vinny.backend.auth.service.KakaoAuthService;
import com.vinny.backend.error.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증/인가 관련 API")
public class AuthController {

    private final AuthService authService;
    private final KakaoAuthService kakaoAuthService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login/kakao")
    @Operation(
            summary = "카카오 소셜 로그인",
            description = """
            카카오에서 발급받은 AccessToken으로 로그인합니다.
            - 신규 유저: User를 생성하고 UserStatus=ONBOARDING으로 저장
            - 기존 유저: 기존 User로 토큰 재발급
            응답으로 우리 서버의 JWT(access/refresh)를 반환합니다.
            """
    )
    public ApiResponse<LoginResponseDto> kakaoMobileLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "카카오 AccessToken",
                    content = @Content(
                            schema = @Schema(implementation = KakaoTokenRequestDto.class),
                            examples = @ExampleObject(value = """
                        { "accessToken": "kakao-access-token" }
                    """)
                    )
            )
            @RequestBody KakaoTokenRequestDto requestDto
    ) {
        LoginResponseDto responseDto = kakaoAuthService.processKakaoLogin(requestDto.getAccessToken());
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/reissue")
    @Operation(
            summary = "AccessToken 재발급",
            description = """
            RefreshToken으로 AccessToken을 재발급합니다.
            - 성공 시 access/refresh 모두 재발급(로테이션) 권장
            - 실패 시 클라이언트는 소셜 로그인 화면으로 이동
            """
    )
    public ApiResponse<TokenDto> reissue(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "재발급 요청 바디(RefreshToken)",
                    content = @Content(
                            schema = @Schema(implementation = TokenRequestDto.class),
                            examples = @ExampleObject(value = """
                        { "refreshToken": "eyJhbGciOi..." }
                    """)
                    )
            )
            @RequestBody TokenRequestDto requestDto
    ) {
        TokenDto tokenDto = authService.reissue(requestDto);
        return ApiResponse.onSuccess(tokenDto);
    }

    @GetMapping("/me")
    @Operation(
            summary = "내 인증 정보(userId) 확인",
            description = "JWT 기반 인증 후 현재 사용자 식별자(userId)를 반환합니다."
    )
    public ApiResponse<String> getMyInfo(
            @Parameter(hidden = true) // Swagger에서 Authentication 객체 숨김
            org.springframework.security.core.Authentication authentication
    ) {
        String userId = authentication.getName();
        return ApiResponse.onSuccess(userId);
    }

    @GetMapping("/session")
    @Operation(
            summary = "앱 부팅 세션 점검",
            description = """
            앱 시작 시 화면 분기 및 accessToken 재발급 필요 여부를 판단합니다.
            - Authorization 헤더의 Bearer accessToken이 없거나 만료여도 호출 가능합니다.
            - 응답:
              - status: LOGIN | ONBOARD | HOME
              - needRefresh: true(만료) / false(정상)
            """
    )
    public ApiResponse<SessionResponseDto> getSession(HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request);
        SessionResponseDto response = authService.getSession(accessToken);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "로그아웃 요청 시, 해당 사용자의 refreshToken을 null로 설정하여 로그아웃 처리합니다."
    )
    public ApiResponse<String> logout(HttpServletRequest request) {

        String accessToken = jwtProvider.resolveToken(request);

        if (accessToken == null || !jwtProvider.validateToken(accessToken)) {
            return ApiResponse.onFailure("Invalid or expired access token.", "로그인 상태가 아닙니다.", null);
        }

        Long userId = jwtProvider.getUserIdFromToken(accessToken);

        // 로그아웃 처리: 해당 사용자의 refreshToken을 null로 설정
        try {
            authService.logout(userId);
            return ApiResponse.onSuccess("Logout successful.", "로그아웃이 정상적으로 처리되었습니다.");
        } catch (Exception e) {
            return ApiResponse.onFailure("Logout Error", "로그아웃 처리 중 오류가 발생했습니다.", null);
        }
    }
}
