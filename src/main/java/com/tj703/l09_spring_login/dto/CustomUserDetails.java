package com.tj703.l09_spring_login.dto;

import com.tj703.l09_spring_login.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPw();
    }
    //로그인시 사용하는 아이디
    @Override
    public String getUsername() {
        return user.getId();
    }
    public User getUser() {
        return user;
    }
}
