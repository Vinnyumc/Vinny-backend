package com.vinny.backend.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;

    @Builder
    public TokenDto(String grantType, String accessToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
    }
}
