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
                AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

                // Provider②: DaoAuthenticationProvider（ID/PW用）
                builder
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());

                // Provider①: JWT用カスタムProvider
                builder.authenticationProvider(jwtAuthenticationProvider);

                return builder.build();
        }

        @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager) throws Exception {

        // ★ JWT フィルタを有効化
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(authenticationManager);

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // ログイン・新規登録は誰でもOK
                    .requestMatchers("/auth/login", "/auth/register/**").permitAll()

                    // 説明会一覧（GET）は未ログインOK
                    .requestMatchers(HttpMethod.GET, "/events", "/events/**").permitAll()

                    // 予約APIは学生のみ
                    .requestMatchers(HttpMethod.POST, "/events/*/reservations")
                        .hasRole("STUDENT")

                    // preflight
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    // それ以外は認証必須
                    .anyRequest().authenticated()
            )
            // ★ JWT フィルタを UsernamePasswordAuthenticationFilter の前に挿入
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
        }

}