package com.no1project.reservation.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final String token;

    // 認証前（JWT文字列だけ持ってる状態）
    public JwtAuthenticationToken(String token) {
        super(null);
        this.principal = null;
        this.token = token;
        setAuthenticated(false);
    }

    // 認証後
    public JwtAuthenticationToken(Object principal, String token,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }
}
