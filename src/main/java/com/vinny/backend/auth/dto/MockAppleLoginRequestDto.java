package com.vinny.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MockAppleLoginRequestDto {

    private String providerId; // Apple의 고유 사용자 식별자(sub)를 대신할 임의의 값
    private String email;
}
