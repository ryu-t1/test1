package com.no1project.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {})  // CORSを有効化（実体はCorsConfigに任せる）
            .csrf(csrf -> csrf.disable())  // 開発中はCSRF無効
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 全リクエスト許可
        return http.build();
    }
}
