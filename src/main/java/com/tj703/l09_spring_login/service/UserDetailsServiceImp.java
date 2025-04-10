package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.dto.CustomUserDetails;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;
    //id로 찾아와서 spring에서 pw를 입력한 값과 비교(자동으로 구현됨)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt=userRepository.findById(username);
        System.out.println("로그인 시도");
        if(userOpt.isPresent()){
            User user=userOpt.get();
            System.out.println(user);
        }
        return userOpt
                .map(CustomUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException(username+": 아이디 없음"));
    }
}
