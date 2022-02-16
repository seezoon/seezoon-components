package com.seezoon.security;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author hdf
 */
@RequiredArgsConstructor
public class SeezoonUserDetails<T> implements UserDetails {

    private static final long serialVersionUID = -1;

    private final T userInfo;
    private final String username;
    /**
     * 针对没有密码场景的登录，如微信，利用username 即可以，password 固定放置 {@link PasswordEncoder#NONE_PASSWORD}
     */
    private final String passowrd;
    private boolean locked;

    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return passowrd;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public T getUserInfo() {
        return userInfo;
    }

}
