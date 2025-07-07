package com.vinny.backend.auth.service;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.auth.dto.KakaoLoginRequestDto;
import com.vinny.backend.auth.dto.TokenDto;
import com.vinny.backend.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
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

        return jwtProvider.generateTokenDto(user);
    }
}
