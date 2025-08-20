package com.vinny.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleTokenRequestDto {

    private String authorizationCode;
    private String identityToken; // 클라이언트(iOS)가 받은 identityToken
}
