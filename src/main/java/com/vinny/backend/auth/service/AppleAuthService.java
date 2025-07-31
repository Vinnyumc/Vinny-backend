package com.vinny.backend.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinny.backend.User.domain.enums.Provider;
import com.vinny.backend.auth.dto.*;
import com.vinny.backend.auth.jwt.AppleJwtUtils;
import com.vinny.backend.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AppleAuthService {

    private final AuthService authService;
    private final AppleJwtUtils appleJwtUtils;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.provider.apple.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.apple.jwk-set-uri}")
    private String applePublicKeyUri;

    public LoginResponseDto processAppleLogin(AppleTokenRequestDto requestDto) throws Exception {
        // 1. client_secret 생성
        String clientSecret = appleJwtUtils.createClientSecret();

        // 2. authorizationCode로 애플 서버에서 토큰(id_token 포함) 받아오기
        AppleTokenResponse tokenResponse = getAppleToken(requestDto.getAuthorizationCode(), clientSecret).block();
        String serverIdentityToken = tokenResponse.getIdToken();

        // 3. 서버가 직접 받은 id_token의 서명을 검증하고 payload 추출
        AppleUserPayload serverPayload = verifyAndDecodeIdentityToken(serverIdentityToken);

        // 4. 클라이언트가 전달한 id_token의 payload 추출 (서명 검증 X)
        AppleUserPayload clientPayload = decodeIdentityTokenPayload(requestDto.getIdentityToken());

        // 5. [교차 검증] 두 토큰의 사용자(sub)가 일치하는지 확인
        if (!serverPayload.getSub().equals(clientPayload.getSub())) {
            throw new RuntimeException("Apple identity tokens do not match.");
        }

        // 6. 검증 완료 후, 우리 서비스에 로그인/회원가입 처리
        Provider provider = Provider.APPLE;
        String providerId = serverPayload.getSub();
        String email = serverPayload.getEmail();

        return authService.socialLogin(provider, providerId, email);
    }

    private Mono<AppleTokenResponse> getAppleToken(String code, String clientSecret) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");

        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AppleTokenResponse.class);
    }

    private AppleUserPayload verifyAndDecodeIdentityToken(String identityToken) {
        try {
            Map<String, String> header = objectMapper.readValue(new String(Base64.getUrlDecoder().decode(identityToken.substring(0, identityToken.indexOf('.')))), Map.class);
            String kid = header.get("kid");

            ApplePublicKeyResponse.ApplePublicKey publicKey = getMatchingPublicKey(kid).block();
            if (publicKey == null) throw new RuntimeException("Apple Public Key not found for kid: " + kid);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(createPublicKey(publicKey))
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();

            return objectMapper.convertValue(claims, AppleUserPayload.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify and decode Apple identity token", e);
        }
    }

    private AppleUserPayload decodeIdentityTokenPayload(String identityToken) {
        try {
            String payloadJson = new String(Base64.getUrlDecoder().decode(identityToken.split("\\.")[1]));
            return objectMapper.readValue(payloadJson, AppleUserPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to decode apple identity token payload", e);
        }
    }

    private Mono<ApplePublicKeyResponse.ApplePublicKey> getMatchingPublicKey(String kid) {
        return webClient.get()
                .uri(applePublicKeyUri)
                .retrieve()
                .bodyToMono(ApplePublicKeyResponse.class)
                .flatMap(keyResponse -> Mono.justOrEmpty(keyResponse.getKeys().stream().filter(key -> key.getKid().equals(kid)).findFirst()));
    }

    private PublicKey createPublicKey(ApplePublicKeyResponse.ApplePublicKey applePublicKey) throws Exception {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());
        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
        return keyFactory.generatePublic(publicKeySpec);
    }

}
