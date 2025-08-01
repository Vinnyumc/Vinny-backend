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
        String refreshToken = requestDto.getRefreshToken();

        // 1. Refresh Token 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. DB에서 Refresh Token으로 사용자 조회
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Refresh Token 입니다."));

        // 3. 새로운 토큰 생성 (Refresh Token 순환)
        TokenDto newTokens = jwtProvider.generateTokens(user);

        // 4. DB에 새로운 Refresh Token 업데이트
        user.updateRefreshToken(newTokens.getRefreshToken());

        return newTokens;
    }
}
