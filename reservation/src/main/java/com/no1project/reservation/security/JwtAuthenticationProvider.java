package com.no1project.reservation.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(JwtUtil jwtUtil,
                                     UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        String token = (String) jwtAuth.getCredentials();

        if (!jwtUtil.validateToken(token)) {
            return null; // 認証失敗
        }

        String username = jwtUtil.getUsername(token);
        var userDetails = userDetailsService.loadUserByUsername(username);

        return new JwtAuthenticationToken(
                userDetails,
                token,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
