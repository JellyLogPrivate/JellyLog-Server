package com.saram.jellylog.global.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final Long userCode;

    public CustomUserDetails(Long userCode) {
        this.userCode = userCode;
    }

    public Long getUserCode() {
        return userCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public String getPassword() { return null; }
    @Override public String getUsername() { return String.valueOf(userCode); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}