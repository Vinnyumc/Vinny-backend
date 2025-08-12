package com.vinny.backend.auth.service;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.Provider;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.auth.dto.LoginResponseDto;
import com.vinny.backend.auth.dto.SessionResponseDto;
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

    @Transactional(readOnly = true)
    public SessionResponseDto getSession(String accessToken) {
        // 0) 토큰 없음 → 로그인 화면
        if (accessToken == null || accessToken.isBlank()) {
            return SessionResponseDto.builder()
                    .status("LOGIN")
                    .needRefresh(false)
                    .build();
        }

        // 1) 유효 토큰 → 유저 상태 조회 후 분기
        if (jwtProvider.validateToken(accessToken)) {
            Long userId = jwtProvider.getUserIdFromToken(accessToken);
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                return SessionResponseDto.builder()
                        .status("LOGIN")
                        .needRefresh(false)
                        .build();
            }

            String status;
            if (user.getUserStatus() == UserStatus.ONBOARDING) {
                status = "ONBOARD";
            } else if (user.getUserStatus() == UserStatus.ACTIVE) {
                status = "HOME";
            } else {
                status = "LOGIN"; // INACTIVE, DELETED 등
            }

            return SessionResponseDto.builder()
                    .status(status)
                    .needRefresh(false)
                    .build();
        }

        // 2) 유효하지 않은 토큰 → 만료인지, 완전 무효인지 구분
        try {
            // parseClaims()가 ExpiredJwtException이면 claims 반환 가능
            Long userId = jwtProvider.getUserIdFromToken(accessToken);
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                // 토큰은 있지만 DB에 유저 없음 → 로그인 필요
                return SessionResponseDto.builder()
                        .status("LOGIN")
                        .needRefresh(false)
                        .build();
            }

            // accessToken만 만료된 경우 → 기존 화면 유지 + 재발급 필요
            String status;
            if (user.getUserStatus() == UserStatus.ONBOARDING) {
                status = "ONBOARD";
            } else if (user.getUserStatus() == UserStatus.ACTIVE) {
                status = "HOME";
            } else {
                status = "LOGIN";
            }

            return SessionResponseDto.builder()
                    .status(status)
                    .needRefresh(true) // 프런트에서 /reissue 호출 후 다시 /session
                    .build();

        } catch (Exception e) {
            // 형식 오류, 서명 불일치 등 → 완전 무효
            return SessionResponseDto.builder()
                    .status("LOGIN")
                    .needRefresh(false)
                    .build();
        }
    }

    @Transactional
    public void logout(Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자의 refreshToken을 null로 설정하여 로그아웃 처리
        user.updateRefreshToken(null);

        // DB에 변경 사항 저장
        userRepository.save(user);
    }
}
