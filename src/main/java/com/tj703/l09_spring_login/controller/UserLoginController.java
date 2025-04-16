package com.tj703.l09_spring_login.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tj703.l09_spring_login.dto.CustomUserDetails;
import com.tj703.l09_spring_login.dto.GoogleUser;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.service.UserService;
import com.tj703.l09_spring_login.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@AllArgsConstructor
@RequestMapping("/user/api")
@CrossOrigin(origins ={ "http://localhost:5000"})
public class UserLoginController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final Logger logger = Logger.getLogger(UserLoginController.class.getName());

    @Getter@Setter
    public class LoginDto{
        private String jwt;
        private User user;
    }

    @PostMapping("/login.do")
    public ResponseEntity<Map<String,String>> login(
            @RequestBody User user
            , HttpServletResponse response
    ) throws JsonProcessingException {
        UserDetails userDetails =  userDetailsService.loadUserByUsername(user.getId());

        System.out.println(userDetails);
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                userDetails,                // Principal (인증된 사용자 정보)
                user.getPw(),                       // Credential (보통은 비밀번호인데, 인증 후엔 null로 처리)
                userDetails.getAuthorities()); // 권한 목록 (ROLE_USER, ROLE_ADMIN 등)
        //인증 검사에 사용할 정보로 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);
        String jwt=jwtUtil.generateToken(userDetails.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();
        String userDetailsJson=objectMapper.writeValueAsString((CustomUserDetails)userDetails);
        Map<String,String> map=new HashMap<>();
        map.put("jwt",jwt);
        map.put("user", userDetailsJson);



        return ResponseEntity.ok(map);
    }

    @GetMapping("/check.do")
    public ResponseEntity<CustomUserDetails> check(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails);
        return ResponseEntity.ok((CustomUserDetails) userDetails);
    }
    @PostMapping("/oauth/google/login.do")
    public ResponseEntity<LoginDto> googleLogin(
            @RequestBody GoogleUser googleUser
    ) {
        System.out.println(googleUser);
        LoginDto loginDto=new LoginDto();

        User user=userService.findById(googleUser.getEmail());
        if(user==null) {
            User signupUser=User.builder()
                .name(googleUser.getName())
                .id(googleUser.getEmail())
                .role("GUEST")
                .build();
            loginDto.setUser(signupUser);
            return ResponseEntity.status(409).body(loginDto); //회원가입 페이지 유도
        }
        String jwt=jwtUtil.generateToken(user.getId());
        loginDto.setJwt(jwt);
        loginDto.setUser(user);
        return ResponseEntity.ok(loginDto);
    }

}
