package com.vinny.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplePublicKeyResponse {

    private List<ApplePublicKey> keys;

    @Getter
    @Setter
    public static class ApplePublicKey {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }
}
