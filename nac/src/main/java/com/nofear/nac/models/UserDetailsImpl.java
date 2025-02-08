package com.nofear.nac.models;

import com.nofear.nac.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

//Class nay cung cap thong tin user
public class UserDetailsImpl implements UserDetails {

    private final User user;

    //inject
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    //methof GrandtedAuthority cua Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority("ROLE_" + a.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public final User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserDetailsImpl{" +
                "user=" + user +
                '}';
    }
}
