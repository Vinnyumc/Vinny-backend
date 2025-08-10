package com.vinny.backend.auth.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponseDto {

    private String status;       // "LOGIN", "ONBOARD", "HOME"
    private boolean needRefresh; // true면 accessToken 만료 → refresh 필요
}
