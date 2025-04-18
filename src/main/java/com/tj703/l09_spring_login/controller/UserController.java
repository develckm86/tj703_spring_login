package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.dto.LoginDto;
import com.tj703.l09_spring_login.dto.UserLoginValid;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.jwt.JwtUtil;
import com.tj703.l09_spring_login.security.CustomUserDetails;
import com.tj703.l09_spring_login.security.CustomUserDetailsService;
import com.tj703.l09_spring_login.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //로그인폼 -> jwt/login.do 요청 {id:경민,pw:1234} ->
    // 로그인이 되었다면 jwt 토큰 생성 후 응답 ->
    // 로그인 양식에서 jwt 토큰을 받아서 로컬에 저장

    @PostMapping("/jwt/login.do")
    public ResponseEntity<LoginDto> loginAction(
            @Valid @RequestBody UserLoginValid userLoginValid
            ) {
        User user=User.builder() //로그인할때 사용하는 user
                        .id(userLoginValid.getId())
                        .pw(userLoginValid.getPw()).build();

        System.out.println("loginAction 중!"+user);
        Optional<User> userOpt=userService.loginHash(user); //로그인 완료 후 가져오는 user
        if(userOpt.isPresent()) {
            LoginDto loginDto=new LoginDto();
            String jwt=jwtUtil.generateToken(user.getId());
            loginDto.setJwt(jwt);
            loginDto.setUser(userOpt.get());
            return ResponseEntity.ok(loginDto);
        }
        return ResponseEntity.status(403).build();
        //403 인증이 안됨 (유저가 없거나 비밀번호가 틀림)
    }
    // session
    // => session 만료,

    // security+session (/logout)
    // SecurityContextHolder.getContext().setAuthentication(null);

    // cookie+jwt => cookie 만료

    // localstorage + jwt => 브라우저 로컬저장소에 jwt 를 삭제
//    @GetMapping("/logout.do")
//    public String logoutAction(
//            @CookieValue(name = "jwt",required = false) Cookie jwtCookie,
//            HttpServletResponse response
//    ) {
//        if(jwtCookie!=null){
//            jwtCookie.setMaxAge(0);
//            jwtCookie.setPath("/");
//            response.addCookie(jwtCookie);
//        }
//        return "redirect:/";
//    }
}
