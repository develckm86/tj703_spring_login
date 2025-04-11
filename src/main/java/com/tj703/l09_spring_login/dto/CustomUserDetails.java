package com.tj703.l09_spring_login.dto;

import com.tj703.l09_spring_login.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
//세션으로 저장될 유저
//유저를 이용해 이증 인가를 진행
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user; //데이터 베이스에 접속하는 유저
    //getAuthorities : 인증할 role 은 머냐?
    //ADMIN -> ROLE_ADMIN
    //USER -> ROLE_USER
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
    }
    //로그인 구현시 비밀번호가 머냐?
    @Override
    public String getPassword() {
        return user.getPw();
    }
    //UserName == id : 로그인 시 아이디가 머냐?
    @Override
    public String getUsername() {
        return user.getId();
    }
    //user의 필드가 필요해서 정의 => principal.user
    public User getUser() {
        return user;
    }

}
