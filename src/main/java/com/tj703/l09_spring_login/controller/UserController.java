package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.dto.CustomUserDetails;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.service.UserService;
import com.tj703.l09_spring_login.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/login.do")
    public String login(
             HttpSession session,
             //@SessionAttribute(required = false) User loginUser,//required=true 자동으로 로그인 한 사람만 이 리소스 요청가능(400)
             @AuthenticationPrincipal CustomUserDetails loginUser,
             @RequestParam(value = "error",defaultValue = "false") boolean error,
             Model model
    ) {
        //로그인되어 있는 유저는 다시 로그인 폼으로 올 수 없다.
        //Object loginUserObj=session.getAttribute("loginUser");


        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        //SecurityContext : 인증정보만 포함시켜 세셔탈취(공격자가 사용자의 세션 ID를 탈취해서, 마치 로그인한 것처럼 서버에 요청을 보내는 공격 방식) 공격을 막음
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            CustomUserDetails loginUserDetails = (CustomUserDetails) auth.getPrincipal();
        }


        if(loginUser==null) {
            if(error) {model.addAttribute("error","아이디나 비밀번호를 확인하세요.");}
            return "user/login";
        }else{
            return "redirect:/";
        }

    }
//1.form에서 사용자 ID/PW 입력 → 서버가 인증
//2.인증에 성공하면 → 서버가 JWT를 발급
//3.이후 모든 요청은 → 클라이언트가 JWT를 헤더에 실어 보냄
//4.JWT 필터에서 인증 수행 (UsernamePasswordAuthenticationToken 생성 등)
    @PostMapping("/jwt/login.do")
    public String jwtLogin(User user, HttpServletRequest request, HttpServletResponse response, HttpHeaders httpHeaders) {
        System.out.println("jwtLogin");
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getId());
        //spring security가 로그인 인증 때 사용함 (강제 로그인)
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                userDetails,                // Principal (인증된 사용자 정보)
                user.getPw(),                       // Credential ( 비밀번호인데)
                userDetails.getAuthorities()); // 권한 목록 (ROLE_USER, ROLE_ADMIN 등)
        SecurityContextHolder.getContext().setAuthentication(authToken); //스프링 시큘리티에서 저장
        String tokenStr=jwtUtil.generateToken(user);
        System.out.println("tokenStr:"+tokenStr);
        // ✅ JWT를 HttpOnly 쿠키에 저장
        Cookie jwtCookie = new Cookie("JWT", tokenStr);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60); // 1시간
        response.addCookie(jwtCookie);
        return "redirect:/";
    }

    @GetMapping("/jwt/logout.do")
    public String logoutAction(
            HttpServletResponse  response
    ) {
        SecurityContextHolder.getContext().setAuthentication(null);
        Cookie cookie = new Cookie("JWT", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
