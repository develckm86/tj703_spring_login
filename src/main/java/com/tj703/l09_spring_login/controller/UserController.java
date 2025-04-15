package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.jwt.JwtUtil;
import com.tj703.l09_spring_login.security.CustomUserDetails;
import com.tj703.l09_spring_login.security.CustomUserDetailsService;
import com.tj703.l09_spring_login.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/login.do")
    public String login(
             @AuthenticationPrincipal UserDetails loginUser //security 에서 관리하는 인증된 유저
    ) {
        if(loginUser==null) {
            return "user/login";
        }else{
            return "redirect:/";
        }

    }
    // ?id=admin1&pw=1234
    @PostMapping("/jwt/login.do")
    public String loginAction(
            @ModelAttribute User user,
            HttpServletResponse response
            ) {
        System.out.println("loginAction 중!");
        UserDetails userDetails = null;
        userDetails=customUserDetailsService.loadUserByUsername(user.getId());
        System.out.println(userDetails.getUsername());

        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                userDetails,
                user.getPw(),
                userDetails.getAuthorities()
        ); //로그인 -> 시큐리티에서 관리하는 유저 객체 토큰 발급

        SecurityContextHolder.getContext().setAuthentication(authToken);
        //시큐리티에서 관리하는 로그인 유저(UserDetails)

        Cookie jwt=new Cookie("jwt",jwtUtil.generateToken(userDetails.getUsername()));
        jwt.setHttpOnly(true); //js 쿠키탈취 못하게
        //jwt.setSecure(true); //https 통신에서만 쿠키보내겠다.
        jwt.setPath("/");
        jwt.setMaxAge(1000*60*30);
        response.addCookie(jwt);
        return "redirect:/";
    }
    @GetMapping("/logout.do")
    public String logoutAction(  ) {
        return "redirect:/";
    }
}
