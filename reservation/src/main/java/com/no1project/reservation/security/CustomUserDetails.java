package com.no1project.reservation.security;

import com.no1project.reservation.model.SampleUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final SampleUser user;

    public CustomUserDetails(SampleUser user) {
        this.user = user;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public String getName() {
        return user.getName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ロールは今は使わない（必要なら後で追加）
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 「ユーザー名」は email を使う
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
