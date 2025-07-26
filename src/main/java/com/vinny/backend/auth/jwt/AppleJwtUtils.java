package com.vinny.backend.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

//@Component
@RequiredArgsConstructor
public class AppleJwtUtils {

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;
    @Value("${apple.team-id}")
    private String teamId;
    @Value("${apple.key-id}")
    private String keyId;
    @Value("${apple.private-key-path}")
    private String privateKeyPath;

    public String createClientSecret() throws Exception {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600 * 1000); // 1시간 유효

        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() throws Exception {
        ClassPathResource resource = new ClassPathResource(privateKeyPath);
        InputStream inputStream = resource.getInputStream();
        String privateKeyString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        String key = privateKeyString.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }
}