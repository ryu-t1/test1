package com.no1project.reservation.config;

import com.no1project.reservation.security.JwtAuthenticationProvider;
import com.no1project.reservation.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ★ とりあえず平文用（卒業制作の後半で BCrypt に差し替える想定）
        return NoOpPasswordEncoder.getInstance();
        // ちゃんとやるなら:
        // return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Provider②: DaoAuthenticationProvider（ID/PW用）
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        // Provider①: JWT用カスタムProvider
        builder.authenticationProvider(jwtAuthenticationProvider);

        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(authenticationManager);

        http
                .cors(cors -> {})  // 既存の CorsConfig を利用
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ログインAPIは誰でもOK
                        .requestMatchers("/auth/login").permitAll()
                        // preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 開発中だけ /users, /reservations を一時的に解放したいならここでpermitAll
                        // .requestMatchers("/users/**", "/reservations/**").permitAll()
                        .anyRequest().authenticated()
                )
                // JWTフィルターを UsernamePasswordAuthenticationFilter より前に挿入
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
