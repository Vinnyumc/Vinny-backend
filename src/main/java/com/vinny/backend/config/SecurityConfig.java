package com.vinny.backend.config;
//
//import com.vinny.backend.auth.filter.JwtFilter;
//import com.vinny.backend.auth.provider.JwtAuthenticationProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//    private final JwtAuthenticationProvider jwtAuthenticationProvider;
//
//    public SecurityConfig(JwtFilter jwtFilter, JwtAuthenticationProvider jwtAuthenticationProvider) {
//        this.jwtFilter = jwtFilter;
//        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, InMemoryClientRegistrationRepository clientRegistrationRepository) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/api/auth/**").permitAll()
//                .requestMatchers("/api/user/**").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login()
//                .clientRegistrationRepository(clientRegistrationRepository())
//                .and()
//                .addFilter(jwtFilter);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.kakaoClientRegistration());
//    }
//
//    private ClientRegistration kakaoClientRegistration() {
//        return ClientRegistration.withRegistrationId("kakao")
//                .clientId("YOUR_KAKAO_CLIENT_ID")
//                .clientSecret("YOUR_KAKAO_CLIENT_SECRET")
//                .scope("profile", "email")
//                .redirectUri("{baseUrl}/login/oauth2/code/kakao")
//                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
//                .tokenUri("https://kauth.kakao.com/oauth/token")
//                .userInfoUri("https://kapi.kakao.com/v2/user/me")
//                .clientName("Kakao")
//                .build();
//    }
//}

import com.vinny.backend.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll() // 로그인 및 H2 콘솔 경로는 모두 허용
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // H2 콘솔을 위한 헤더 설정
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}