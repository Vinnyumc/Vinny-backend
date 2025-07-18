package com.vinny.backend.auth.service;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.auth.dto.KakaoLoginRequestDto;
import com.vinny.backend.auth.dto.TokenDto;
import com.vinny.backend.auth.dto.TokenRequestDto;
import com.vinny.backend.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenDto kakaoLogin(KakaoLoginRequestDto requestDto) {
        Long kakaoUserId = requestDto.getKakaoUserId();

        User user = userRepository.findByKakaoUserId(kakaoUserId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .kakaoUserId(kakaoUserId)
                            .nickname("유저" + kakaoUserId)
                            .email(kakaoUserId + "@vinnystage.com")
                            .userStatus(UserStatus.ACTIVE)
                            .build();
                    return userRepository.save(newUser);
                });

        TokenDto tokenDto = jwtProvider.generateTokens(user);

        user.updateRefreshToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto requestDto) {
        // 1. Refresh Token 유효성 검증
        if (!jwtProvider.validateToken(requestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token에서 사용자 정보 (kakaoUserId) 추출
        Authentication authentication = jwtProvider.getAuthentication(requestDto.getAccessToken());
        Long kakaoUserId = Long.parseLong(authentication.getName());

        // 3. DB에서 사용자 조회 및 Refresh Token 일치 여부 확인
        User user = userRepository.findByKakaoUserId(kakaoUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!user.getRefreshToken().equals(requestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 4. 새로운 토큰 생성
        TokenDto newTokens = jwtProvider.generateTokens(user);

        // 5. DB에 새로운 Refresh Token 업데이트
        user.updateRefreshToken(newTokens.getRefreshToken());

        return newTokens;
    }
}
