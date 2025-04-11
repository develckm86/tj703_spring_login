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
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //로그인 폼에서 로그인 요청하면 loadUserByUsername 이 호출됨
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("로그인 호출");
        Optional<User> loginUserOpt =userRepository.findById(username);
//        if(loginUserOpt.isPresent()){
//            User loginUser = loginUserOpt.get();
//            UserDetails userDetails= new CustomUserDetails(loginUser);
//            return userDetails;
//        }
//        new UsernameNotFoundException("could not find user");
//        return null;
        //username 유저를 찾은 후에 userPassword 를 입력한 패스워드와 같은지
        //어떤 암호화를 이용할건지 정의
        return loginUserOpt
                .map(CustomUserDetails::new)
                //.orElse(null);
                .orElseThrow(() -> new UsernameNotFoundException(username));

    }
}
