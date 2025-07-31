package com.vinny.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleUserPayload {

    private String sub; // 사용자의 고유 식별자
    private String email;
    private Boolean email_verified;
    private Boolean is_private_email;
}
