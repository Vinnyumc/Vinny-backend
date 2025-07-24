package com.vinny.backend.auth.service;

import com.vinny.backend.User.domain.enums.Provider;
import com.vinny.backend.auth.dto.KakaoUserResponse;
import com.vinny.backend.auth.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final AuthService authService;
    private final WebClient webClient = WebClient.create();

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    public LoginResponseDto processKakaoLogin(String kakaoAccessToken) {
        // 1. 전달받은 액세스 토큰으로 사용자 정보 요청
        KakaoUserResponse userResponse = getKakaoUserInfo(kakaoAccessToken).block();

        // 2. 받은 정보로 우리 서비스에 로그인/회원가입 처리
        Provider provider = Provider.KAKAO;
        String providerId = String.valueOf(userResponse.getId());
        String email = userResponse.getKakaoAccount().getEmail();

        return authService.socialLogin(provider, providerId, email);
    }

    private Mono<KakaoUserResponse> getKakaoUserInfo(String accessToken) {
        return webClient.get()
                .uri(userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserResponse.class);
    }

}
