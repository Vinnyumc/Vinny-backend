package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 카카오 유저 아이디로 유저를 조회하는 메서드
//    Optional<User> findByKakaoUserId(Long kakaoUserId);

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    Optional<User> findByRefreshToken(String refreshToken);
}
