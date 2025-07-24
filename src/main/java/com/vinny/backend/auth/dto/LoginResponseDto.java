package com.vinny.backend.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Boolean isNewUser;
}
