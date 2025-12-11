package com.no1project.reservation.security;

import com.no1project.reservation.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
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

    public String getRole() {
        return user.getRole(); // "student" / "normal_admin" など
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // DBには "student" / "normal_admin" を入れておき、
        // Spring Security用に "ROLE_STUDENT" のようにして返す
        String roleName = user.getRole().toUpperCase(); // 例: "STUDENT"
        String authorityName = roleName.startsWith("ROLE_")//ROLE_の後ろ側は大文字にしないとhasRoleメソッドが使えない
                ? roleName  
                : "ROLE_" + roleName;

        return List.of(new SimpleGrantedAuthority(authorityName));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // username は email を使う
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
