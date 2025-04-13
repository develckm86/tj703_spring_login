package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.dto.CustomUserDetails;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@AllArgsConstructor
@RequestMapping("/user/api")

public class UserLoginController {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final Logger logger = Logger.getLogger(UserLoginController.class.getName());


    @PostMapping("/login.do")
    public ResponseEntity<CustomUserDetails> login(
            @RequestBody User user
            , HttpServletResponse response
    ) {
        UserDetails userDetails =  userDetailsService.loadUserByUsername(user.getId());

        System.out.println(userDetails);
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                userDetails,                // Principal (인증된 사용자 정보)
                user.getPw(),                       // Credential (보통은 비밀번호인데, 인증 후엔 null로 처리)
                userDetails.getAuthorities()); // 권한 목록 (ROLE_USER, ROLE_ADMIN 등)
        //인증 검사에 사용할 정보로 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);
        String jwt=jwtUtil.generateToken(userDetails.getUsername());
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setPath("/");
        //jwtCookie.setHttpOnly(true);
        //jwtCookie.setSecure(true);
        jwtCookie.setMaxAge(1000*60*30);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok((CustomUserDetails) userDetails);
    }

    @GetMapping("/check.do")
    public ResponseEntity<CustomUserDetails> check(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails);
        return ResponseEntity.ok((CustomUserDetails) userDetails);
    }



}
