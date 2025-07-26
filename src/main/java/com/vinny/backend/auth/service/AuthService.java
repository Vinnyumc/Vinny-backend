package com.vinny.backend.auth.service;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.Provider;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.auth.dto.LoginResponseDto;
import com.vinny.backend.auth.dto.TokenDto;
import com.vinny.backend.auth.dto.TokenRequestDto;
import com.vinny.backend.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponseDto socialLogin(Provider provider, String providerId, String email) {

        Optional<User> userOptional = userRepository.findByProviderAndProviderId(provider, providerId);

        User user;
        boolean isNewUser = false;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            // 신규 유저
            isNewUser = true;

            User newUser = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .email(email)
                    .userStatus(UserStatus.ONBOARDING)
                    .nickname("임시유저" + providerId)
                    .build();
            user = userRepository.save(newUser);
        }

        // JWT 생성
        TokenDto tokenDto = jwtProvider.generateTokens(user);
        user.updateRefreshToken(tokenDto.getRefreshToken());

        return LoginResponseDto.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .isNewUser(isNewUser)
                .build();
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto requestDto) {
        // 1. Refresh Token 유효성 검증
        if (!jwtProvider.validateToken(requestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token에서 사용자 정보 추출
        Authentication authentication = jwtProvider.getAuthentication(requestDto.getAccessToken());
        Long userId = Long.parseLong(authentication.getName());

        // 3. DB에서 사용자 조회 및 Refresh Token 일치 여부 확인
        User user = userRepository.findById(userId)
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
